package com.gamecraft.service;

import com.gamecraft.domain.Pipeline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Pipeline.
 */
public interface PipelineService {

    /**
     * Save a pipeline.
     *
     * @param pipeline the entity to save
     * @return the persisted entity
     */
    Pipeline save(Pipeline pipeline);

    /**
     * Get all the pipelines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Pipeline> findAll(Pageable pageable);

    /**
     * Get the "id" pipeline.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Pipeline findOne(Long id);

    /**
     * Delete the "id" pipeline.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Stop the "id" pipeline.
     *
     * @param id the id of the entity
     */
    void stop(Long id);

    /**
     * Execute the "id" pipeline.
     *
     * @param id the id of the entity
     */
    void execute(Long id);

    /**
     * Search for the pipeline corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Pipeline> search(String query, Pageable pageable);
}
