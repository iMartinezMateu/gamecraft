package com.gamecraft.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.gamecraft.domain.Pipeline;
import com.gamecraft.domain.enumeration.PipelineStatus;
import com.gamecraft.security.SecurityUtils;
import com.gamecraft.util.FtpClient;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PipelineTask implements Job {
    private final Logger log = LoggerFactory.getLogger(PipelineTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Pipeline pipeline = (Pipeline) jobExecutionContext.getJobDetail().getJobDataMap().get("pipeline");
        File workDirectory = (File) jobExecutionContext.getJobDetail().getJobDataMap().get("workDirectory");

        log.info("Work directory is " + workDirectory.getAbsolutePath());

        try {
            Git git = Git.cloneRepository()
                .setURI(pipeline.getPipelineRepositoryAddress())
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(pipeline.getPipelineRepositoryUsername(),pipeline.getPipelineRepositoryPassword()))
                .setDirectory(workDirectory)
                .call();


            ProcessBuilder pb = new ProcessBuilder(pipeline.getPipelineEngineCompilerPath() + " " + pipeline.getPipelineEngineCompilerArguments());

            pb.directory(workDirectory);
            Process p = pb.start();
            p.waitFor();

            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

            StringBuffer output = new StringBuffer();

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

            log.debug(output.toString());

            switch (pipeline.getPipelinePublicationService()) {
                case FTP:
                    FtpClient ftpClient = new FtpClient(pipeline.getPipelineFtpAddress(),pipeline.getPipelineFtpPort(),pipeline.getPipelineFtpUsername(),pipeline.getPipelineFtpPassword());
                    ftpClient.open();
                    ftpClient.putFileToPath(workDirectory,"/gamecraft/" + LocalDateTime.now());
                    ftpClient.close();
                    break;
                case DROPBOX:
                    DbxRequestConfig config = DbxRequestConfig.newBuilder(pipeline.getPipelineDropboxAppKey()).build();
                    DbxClientV2 client = new DbxClientV2(config, pipeline.getPipelineDropboxToken());
                    InputStream in = new FileInputStream(workDirectory);
                    FileMetadata metadata = client.files().uploadBuilder("/gamecraft/" + LocalDateTime.now() + "/" + workDirectory.getName())
                        .uploadAndFinish(in);
                    break;
            }

            workDirectory.delete();

        } catch (GitAPIException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed because of a repository problem at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
            workDirectory.delete();
        } catch (InterruptedException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed because of an engine execution problem at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
            workDirectory.delete();
        } catch (IOException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed because of data read or storage problem at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
            workDirectory.delete();
        } catch (UploadErrorException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed while publishing to Dropbox at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
            workDirectory.delete();
        } catch (DbxException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed while publishing to Dropbox at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
            workDirectory.delete();
        }
    }
    private void processNotificator(Pipeline pipeline, String message) {

        try {
            String token = String.valueOf(SecurityUtils.getCurrentUserJWT());
            String notificatorId = pipeline.getPipelineNotificatorDetails();
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post;
            String json;
            StringEntity entity;
            switch (pipeline.getPipelineNotificatorType()) {
                case TELEGRAM:
                    post = new HttpPost("http://0.0.0.0:8080/gamecrafttelegramnotificationmanager/api/telegram-bots/" + notificatorId + "/send");
                    post.setHeader("Authorization", "Bearer " + token);
                    json = "{\"chatId\":\""+pipeline.getPipelineNotificatorRecipient()+"\",\"webPagePreviewDisabled\":\"false\",\"notificationDisabled\":\"false\" ,\"message\":\""+message+"\"}";
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
                    json = "{\"channels\":\""+pipeline.getPipelineNotificatorRecipient()+"\",\"users\":\""+pipeline.getPipelineNotificatorRecipient()+"\" ,\"message\":\""+message+"\"}";
                    entity = new StringEntity(json);
                    post.setEntity(entity);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    client.execute(post);
                    break;
                case IRC:
                    post = new HttpPost("http://0.0.0.0:8080/gamecraftircnotificationmanager/api/irc-bots/" + notificatorId + "/send");
                    post.setHeader("Authorization", "Bearer " + token);
                    json = "{\"ircChannel\":\""+pipeline.getPipelineNotificatorRecipient()+"\",\"message\":\""+message+"\"}";
                    entity = new StringEntity(json);
                    post.setEntity(entity);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    client.execute(post);
                    break;
                case EMAIL:
                    post = new HttpPost("http://0.0.0.0:8080/gamecraftemailnotificationmanager/api/email-accounts/" + notificatorId + "/send");
                    post.setHeader("Authorization", "Bearer " + token);
                    json = "{\"toEmailAddress\":\""+pipeline.getPipelineNotificatorRecipient()+"\",\"subject\": \"GameCraft Notification\",\"body\":\""+message+"\"}";
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

