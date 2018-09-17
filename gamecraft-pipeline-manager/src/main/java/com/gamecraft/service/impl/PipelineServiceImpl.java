package com.gamecraft.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.gamecraft.domain.enumeration.PipelineStatus;
import com.gamecraft.security.SecurityUtils;
import com.gamecraft.service.PipelineService;
import com.gamecraft.domain.Pipeline;
import com.gamecraft.repository.PipelineRepository;
import com.gamecraft.repository.search.PipelineSearchRepository;
import com.gamecraft.util.FtpClient;
import com.google.common.io.Files;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobKey.jobKey;

/**
 * Service Implementation for managing Pipeline.
 */
@Service
@Transactional
public class PipelineServiceImpl implements PipelineService {

    private final Logger log = LoggerFactory.getLogger(PipelineServiceImpl.class);

    private final PipelineRepository pipelineRepository;

    private final PipelineSearchRepository pipelineSearchRepository;

    public PipelineServiceImpl(PipelineRepository pipelineRepository, PipelineSearchRepository pipelineSearchRepository) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineSearchRepository = pipelineSearchRepository;
    }

    /**
     * Save a pipeline.
     *
     * @param pipeline the entity to save
     * @return the persisted entity
     */
    @Override
    public Pipeline save(Pipeline pipeline) {
        log.debug("Request to save Pipeline : {}", pipeline);
        Pipeline result = pipelineRepository.save(pipeline);
        pipelineSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the pipelines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pipeline> findAll(Pageable pageable) {
        log.debug("Request to get all Pipelines");
        return pipelineRepository.findAll(pageable);
    }

    /**
     * Get one pipeline by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Pipeline findOne(Long id) {
        log.debug("Request to get Pipeline : {}", id);
        return pipelineRepository.findOne(id);
    }

    /**
     * Delete the pipeline by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pipeline : {}", id);
        pipelineRepository.delete(id);
        pipelineSearchRepository.delete(id);

    }

    /**
     * Stop the pipeline by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void stop(Long id) {
        log.debug("Request to stop Pipeline : {}", id);
        Pipeline pipeline = pipelineRepository.findOne(id);
        try {
            SchedulerFactory schedulerFactory=new StdSchedulerFactory();
            Scheduler scheduler= schedulerFactory.getScheduler();
            scheduler.deleteJob(jobKey(pipeline.getId().toString(), "group"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        pipeline.setPipelineStatus(PipelineStatus.IDLE);
            save(pipeline);
    }

    /**
     * Execute the pipeline by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void execute(Long id) {
        log.debug("Request to execute Pipeline : {}", id);
        File workDirectory = Files.createTempDir();
        Pipeline pipeline = pipelineRepository.findOne(id);
        if (pipeline.getPipelineStatus() == PipelineStatus.IDLE) {
            pipeline.setPipelineStatus(PipelineStatus.RUNNING);
            save(pipeline);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " is running at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            switch (pipeline.getPipelineScheduleType()) {
                case CRONJOB:
                    try {
                        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
                        Scheduler scheduler = schedulerFactory.getScheduler();
                        JobDetail job = JobBuilder.newJob(PipelineTask.class).withIdentity(pipeline.getId().toString(), "group").build();
                        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(pipeline.getId().toString(), "group")
                            .withSchedule(
                                CronScheduleBuilder.cronSchedule(pipeline.getPipelineScheduleCronJob()))
                            .build();
                        job.getJobDataMap().put("pipeline", pipeline);
                        job.getJobDataMap().put("workDirectory", workDirectory);
                        scheduler.scheduleJob(job, trigger);
                        if (!scheduler.isStarted())
                            scheduler.start();
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                    }
                    break;

                case WEBHOOK:
                        try {
                            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
                            Scheduler scheduler = schedulerFactory.getScheduler();
                            JobDetail job = JobBuilder.newJob(PipelineTask.class).withIdentity(pipeline.getId().toString(), "group").build();
                            Trigger trigger = TriggerBuilder.newTrigger().startNow()
                                .build();
                            job.getJobDataMap().put("pipeline", pipeline);
                            job.getJobDataMap().put("workDirectory", workDirectory);
                            scheduler.scheduleJob(job, trigger);
                            if (!scheduler.isStarted())
                                scheduler.start();
                        } catch (SchedulerException e) {
                            e.printStackTrace();
                        }
                    break;
            }

            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " worked succesfully at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pipeline.setPipelineStatus(PipelineStatus.IDLE);
            save(pipeline);
        }
        else {
            log.error("Pipeline is already running!");
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
            log.info("TOKEN IS " + token);
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

    /**
     * Search for the pipeline corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pipeline> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Pipelines for query {}", query);
        Page<Pipeline> result = pipelineSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
