package com.gamecraft.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v1.DbxWriteMode;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.gamecraft.domain.Pipeline;
import com.gamecraft.domain.enumeration.PipelinePublicationService;
import com.gamecraft.domain.enumeration.PipelineStatus;
import com.gamecraft.domain.enumeration.ReportStatus;
import com.gamecraft.security.jwt.JWTConfigurer;
import com.gamecraft.util.FtpClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PipelineTask implements Job {
    private final Logger log = LoggerFactory.getLogger(PipelineTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Pipeline pipeline = (Pipeline) jobExecutionContext.getJobDetail().getJobDataMap().get("pipeline");
        File workDirectory = (File) jobExecutionContext.getJobDetail().getJobDataMap().get("workDirectory");
        if (workDirectory.exists()) {
            try {
                FileUtils.deleteDirectory(workDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String token = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("jwt");
        String reportBody = "Starting new report for pipeline:" + pipeline.getPipelineName() + " and project:" + pipeline.getPipelineProjectName() + " at " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " \n";
        reportBody += "Work directory is " + workDirectory.getAbsolutePath() + " \n";
        long startTime = System.currentTimeMillis();
        try {
            reportBody += "Downloading source code at repository URI: " + pipeline.getPipelineRepositoryAddress() + " and branch called: " + pipeline.getPipelineRepositoryBranch() + " \n";

            if (!pipeline.getPipelineRepositoryUsername().isEmpty()) {
                reportBody += "Using account for repository: " + pipeline.getPipelineRepositoryUsername() + " \n";
            }

            Git git = Git.cloneRepository()
                .setURI(pipeline.getPipelineRepositoryAddress())
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(pipeline.getPipelineRepositoryUsername(),pipeline.getPipelineRepositoryPassword()))
                .setDirectory(workDirectory)
                .call();


            reportBody += "Executing engine at path : " + pipeline.getPipelineEngineCompilerPath() + " " + pipeline.getPipelineEngineCompilerArguments() + " \n";

            List<String> list = new ArrayList<String>();

            list.add(pipeline.getPipelineEngineCompilerPath());
            String args = pipeline.getPipelineEngineCompilerArguments().replace("%path%",workDirectory.getAbsolutePath());
            for (String arg : args.split("\\s+")) {
                list.add(arg);
            }

            ProcessBuilder pb = new ProcessBuilder(list);

            Process p = pb.start();

            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

            StringBuffer output = new StringBuffer();

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

            reportBody +=  output.toString() + " \n";



           if (!pipeline.getPipelineFtpAddress().isEmpty()) {
               reportBody += "Publicate pipeline results to FTP with address:" + pipeline.getPipelineFtpAddress() + ":" + pipeline.getPipelineFtpPort() + " and using account called " + pipeline.getPipelineFtpUsername() + " \n";
               FtpClient ftpClient = new FtpClient(pipeline.getPipelineFtpAddress(), pipeline.getPipelineFtpPort(), pipeline.getPipelineFtpUsername(), pipeline.getPipelineFtpPassword());
               ftpClient.open();
               String name =  Long.toString(Calendar.getInstance().getTimeInMillis());
               ftpClient.pack(workDirectory.getAbsolutePath(),workDirectory.getAbsolutePath() + "/" + name + ".zip");
               ftpClient.putFileToPath(new File(workDirectory.getAbsolutePath() + "/" + name + ".zip"),name + ".zip");
               ftpClient.close();
           }

           if (!pipeline.getPipelineDropboxToken().isEmpty()) {
                    reportBody +=  "Publicate pipeline results to Dropbox with token:"+ pipeline.getPipelineDropboxToken() + " \n";
               String name =  Long.toString(Calendar.getInstance().getTimeInMillis());
               DbxRequestConfig config = DbxRequestConfig.newBuilder(pipeline.getPipelineDropboxAppKey()).build();
                    DbxClientV2 client = new DbxClientV2(config, pipeline.getPipelineDropboxToken());
                    pack(workDirectory.getAbsolutePath(),workDirectory.getAbsolutePath() +"/"+ name + ".zip");
               try (InputStream in = new FileInputStream(workDirectory.getAbsolutePath() +"/"+ name + ".zip")) {
                   log.info("File exists?" + new File(workDirectory.getAbsolutePath() +"/"+ name + ".zip").exists());
                   FileMetadata metadata = client.files().uploadBuilder("/" + name + ".zip")
                       .uploadAndFinish(in);
               }
            }

            if (workDirectory.exists()) {
                try {
                    FileUtils.deleteDirectory(workDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (GitAPIException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            e.printStackTrace();
            workDirectory.delete();
            long stopTime = System.currentTimeMillis();
            reportBody += "Failed " + ExceptionUtils.getStackTrace(e) + "\n";
            reportBody +=  "[ERROR] Pipeline not executed successfully. Time spent: " + Long.toString((stopTime - startTime)) + "ms.\n" ;
            processNotificator(pipeline, "[ERROR] Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed because of a repository problem at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),token,reportBody);

        } catch (IOException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            e.printStackTrace();
            workDirectory.delete();
            long stopTime = System.currentTimeMillis();
            reportBody += "Failed " + ExceptionUtils.getStackTrace(e) + "\n";
            reportBody +=  "[ERROR] Pipeline not executed successfully. Time spent: " + Long.toString((stopTime - startTime)) + "ms.\n" ;
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed because of data read or storage problem at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),token,reportBody);

        } catch (UploadErrorException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            e.printStackTrace();
            workDirectory.delete();
            reportBody += "Failed " + ExceptionUtils.getStackTrace(e) + "\n";
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed while publishing to Dropbox at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),token,reportBody);

            long stopTime = System.currentTimeMillis();
            reportBody +=  "[ERROR] Pipeline not executed successfully. Time spent: " + Long.toString((stopTime - startTime)) + "ms.\n" ;
        } catch (DbxException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            e.printStackTrace();
            workDirectory.delete();
            reportBody += "Failed " + ExceptionUtils.getStackTrace(e) + "\n";
            long stopTime = System.currentTimeMillis();
            reportBody +=  "[ERROR] Pipeline not executed successfully. Time spent: " + Long.toString((stopTime - startTime)) + " ms.\n" ;
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed while publishing to Dropbox at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),token,reportBody);

        }
        finally {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post;
            HttpPut put;
            String json;
            StringEntity entity = null;

            put = new HttpPut("http://0.0.0.0:8080/gamecraftpipelinemanager/api/pipelines/");
            put.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + token);
            json = "{" +
                "        \"id\": "+ pipeline.getId() + "," +
                "        \"pipelineName\": \""+pipeline.getPipelineName() + "\"," +
                "        \"pipelineDescription\": \""+pipeline.getPipelineDescription() +"\"," +
                "        \"pipelineProjectId\": " + pipeline.getPipelineProjectId() + "," +
                "        \"pipelineProjectName\": \""+pipeline.getPipelineProjectName() +"\"," +
                "        \"pipelineDropboxAppKey\": \""+pipeline.getPipelineDropboxAppKey() +"\"," +
                "        \"pipelineDropboxToken\": \""+pipeline.getPipelineDropboxToken() +"\"," +
                "        \"pipelineEngineCompilerArguments\": \""+pipeline.getPipelineEngineCompilerArguments() +"\"," +
                "        \"pipelineEngineCompilerPath\": \""+pipeline.getPipelineEngineCompilerPath() +"\"," +
                "        \"pipelineFtpAddress\": \""+pipeline.getPipelineFtpAddress() +"\"," +
                "        \"pipelineFtpPassword\": \""+pipeline.getPipelineFtpPassword() +"\"," +
                "        \"pipelineFtpPort\": "+ pipeline.getPipelineFtpPort()+"," +
                "        \"pipelineFtpUsername\": \""+pipeline.getPipelineFtpUsername() +"\"," +
                "        \"pipelineNotificatorDetails\": \""+pipeline.getPipelineNotificatorDetails() +"\"," +
                "        \"pipelineNotificatorType\": \""+pipeline.getPipelineNotificatorType() +"\"," +
                "        \"pipelinePublicationService\": \""+ PipelinePublicationService.FTP +"\"," +
                "        \"pipelineRepositoryAddress\": \""+pipeline.getPipelineRepositoryAddress() +"\"," +
                "        \"pipelineRepositoryPassword\": \""+pipeline.getPipelineRepositoryPassword() +"\"," +
                "        \"pipelineRepositoryType\": \""+pipeline.getPipelineRepositoryType() +"\"," +
                "        \"pipelineRepositoryUsername\": \""+pipeline.getPipelineRepositoryUsername() +"\"," +
                "        \"pipelineRepositoryBranch\": \""+pipeline.getPipelineRepositoryBranch() +"\"," +
                "        \"pipelineScheduleCronJob\": \""+pipeline.getPipelineScheduleCronJob() +"\"," +
                "        \"pipelineScheduleType\": \""+pipeline.getPipelineScheduleType() +"\"," +
                "        \"pipelineStatus\": \""+PipelineStatus.IDLE +"\"," +
                "        \"pipelineNotificatorRecipient\": \""+pipeline.getPipelineNotificatorRecipient() +"\"}";
            try {
                log.info(json);
                entity = new StringEntity(json);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            put.setEntity(entity);
            put.setHeader("Accept", "application/json");
            put.setHeader("Content-type", "application/json");
            try {
                client.execute(put);
            } catch (IOException e) {
                e.printStackTrace();
            }

            post = new HttpPost("http://0.0.0.0:8080/gamecraftpipelinemanager/api/reports/");
            post.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + token);
            long stopTime = System.currentTimeMillis();
            if (reportBody.contains("Failed") ) {
                reportBody +=  "[ERROR] Pipeline not executed successfully. Time spent: " + Long.toString((stopTime - startTime)) + "ms.\n" ;
                processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),token,reportBody);
            }
            else {
                reportBody +=  "Pipeline executed successfully. Time spent: " + Long.toString((stopTime - startTime)) + "ms.\n" ;
                processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " executed successfully at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),token,reportBody);
            }
            if (reportBody.contains("[ERROR]")) {
                json = "{\"pipelineId\":\"" + pipeline.getId() + "\",\"reportContent\":\"" + JSONObject.escape(reportBody) + "\",\"reportDate\":\"" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\", \"reportStatus\": \""+ ReportStatus.FAIL +"\"}";
            }
            else {
                json = "{\"pipelineId\":\"" + pipeline.getId() + "\",\"reportContent\":\"" + JSONObject.escape(reportBody) + "\",\"reportDate\":\"" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\", \"reportStatus\": \""+ ReportStatus.GOOD +"\"}";
            }
            try {
                entity = new StringEntity(json);
                post.setEntity(entity);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");
                client.execute(post);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private  void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                    try {
                        zs.putNextEntry(zipEntry);
                        Files.copy(path, zs);
                        zs.closeEntry();
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                });
        }
    }
    private void processNotificator(Pipeline pipeline, String message,String token, String reportBody) {

        try {
            String notificatorId = pipeline.getPipelineNotificatorDetails();
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post;
            String json;
            StringEntity entity;
            switch (pipeline.getPipelineNotificatorType()) {
                case TELEGRAM:
                    post = new HttpPost("http://0.0.0.0:8080/gamecrafttelegramnotificationmanager/api/telegram-bots/" + notificatorId + "/send");
                    post.setHeader("Authorization", "Bearer " + token);
                    json = "{\"chatId\":\""+pipeline.getPipelineNotificatorRecipient()+"\",\"webPagePreviewDisabled\":\"false\",\"notificationDisabled\":\"false\" ,\"message\":\""+message + JSONObject.escape(reportBody)+"\"}";
                    entity = new StringEntity(json);
                    post.setEntity(entity);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    client.execute(post);
                    break;
                case TWITTER:
                    post = new HttpPost("http://0.0.0.0:8080/gamecrafttwitternotificationmanager/api/twitter-bots/" + notificatorId + "/sendTweet");
                    post.setHeader("Authorization", "Bearer " + token);
                    json = "{\"message\":\""+message+"\"}";
                    entity = new StringEntity(json);
                    post.setEntity(entity);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    client.execute(post);
                    break;
                case SLACK:
                    post = new HttpPost("http://0.0.0.0:8080/gamecraftslacknotificationmanager/api/slack-accounts/" + notificatorId + "/send");
                    post.setHeader("Authorization", "Bearer " + token);
                    json = "{\"channels\":\""+pipeline.getPipelineNotificatorRecipient()+"\",\"users\":\""+pipeline.getPipelineNotificatorRecipient()+"\" ,\"message\":\""+message + JSONObject.escape(reportBody)+"\"}";
                    entity = new StringEntity(json);
                    post.setEntity(entity);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    client.execute(post);
                    break;
                case IRC:
                    post = new HttpPost("http://0.0.0.0:8080/gamecraftircnotificationmanager/api/irc-bots/" + notificatorId + "/send");
                    post.setHeader("Authorization", "Bearer " + token);
                    json = "{\"ircChannel\":\""+pipeline.getPipelineNotificatorRecipient()+"\",\"message\":\""+message + JSONObject.escape(reportBody)+"\"}";
                    entity = new StringEntity(json);
                    post.setEntity(entity);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    client.execute(post);
                    break;
                case EMAIL:
                    post = new HttpPost("http://0.0.0.0:8080/gamecraftemailnotificationmanager/api/email-accounts/" + notificatorId + "/send");
                    post.setHeader("Authorization", "Bearer " + token);
                    json = "{\"toEmailAddress\":\""+pipeline.getPipelineNotificatorRecipient()+"\",\"subject\": \""+message+"\",\"body\":\""+JSONObject.escape(reportBody) +"\"}";
                    entity = new StringEntity(json);
                    post.setEntity(entity);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    client.execute(post);
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

