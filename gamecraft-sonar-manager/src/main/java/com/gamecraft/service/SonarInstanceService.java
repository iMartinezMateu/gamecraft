package com.gamecraft.service;

import com.gamecraft.domain.SonarInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SonarInstance.
 */
public interface SonarInstanceService {

    /**
     * Save a sonarInstance.
     *
     * @param sonarInstance the entity to save
     * @return the persisted entity
     */
    SonarInstance save(SonarInstance sonarInstance);

    /**
     *  Get all the sonarInstances.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SonarInstance> findAll(Pageable pageable);

    /**
     *  Get the "id" sonarInstance.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SonarInstance findOne(Long id);

    /**
     *  Delete the "id" sonarInstance.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sonarInstance corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SonarInstance> search(String query, Pageable pageable);
}
