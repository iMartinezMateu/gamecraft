package com.gamecraft.service.impl;

import com.gamecraft.service.PipelineService;
import com.gamecraft.domain.Pipeline;
import com.gamecraft.repository.PipelineRepository;
import com.gamecraft.repository.search.PipelineSearchRepository;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
        try {
            Git git = Git.cloneRepository()
                .setURI(pipeline.getPipelineRepositoryAddress())
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(pipeline.getPipelineRepositoryUsername(),pipeline.getPipelineRepositoryPassword()))
                .setDirectory(workDirectory)
                .call();

            Process p = Runtime.getRuntime().exec(pipeline.getPipelineEngineCompilerPath() + " " + pipeline.getPipelineEngineCompilerArguments());
            p.waitFor();

            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

            StringBuffer output = new StringBuffer();

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

            log.debug(output.toString());


        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
