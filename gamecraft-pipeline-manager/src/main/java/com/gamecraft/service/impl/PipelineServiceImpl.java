package com.gamecraft.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.gamecraft.domain.enumeration.PipelineStatus;
import com.gamecraft.service.PipelineService;
import com.gamecraft.domain.Pipeline;
import com.gamecraft.repository.PipelineRepository;
import com.gamecraft.repository.search.PipelineSearchRepository;
import com.gamecraft.util.FtpClient;
import com.google.common.io.Files;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
     * Execute the pipeline by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void execute(Long id) {
        log.debug("Request to execute Pipeline : {}", id);
        File workDirectory = Files.createTempDir();
        Pipeline pipeline = pipelineRepository.findOne(id);
        pipeline.setPipelineStatus(PipelineStatus.RUNNING);
        save(pipeline);
        processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " is running at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
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

        } catch (GitAPIException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            save(pipeline);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed because of a repository problem at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
        } catch (InterruptedException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            save(pipeline);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed because of an engine execution problem at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
        } catch (IOException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            save(pipeline);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed because of data read or storage problem at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
        } catch (UploadErrorException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            save(pipeline);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed while publishing to Dropbox at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
        } catch (DbxException e) {
            pipeline.setPipelineStatus(PipelineStatus.FAILED);
            save(pipeline);
            processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " failed while publishing to Dropbox at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            e.printStackTrace();
        }
        processNotificator(pipeline, "Pipeline " + pipeline.getPipelineName() + ", executed in project " + pipeline.getPipelineProjectName() + " worked succesfully at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        pipeline.setPipelineStatus(PipelineStatus.IDLE);
        save(pipeline);

    }

    private void processNotificator(Pipeline pipeline, String message) {
        switch (pipeline.getPipelineNotificatorType()) {
            case TELEGRAM:
                break;
            case TWITTER:
                break;
            case HIPCHAT:
                break;
            case SLACK:
                break;
            case IRC:
                break;
            case EMAIL:
                break;

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
