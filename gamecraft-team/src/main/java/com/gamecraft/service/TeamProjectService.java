package com.gamecraft.service;

import com.gamecraft.domain.TeamProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TeamProject.
 */
public interface TeamProjectService {

    /**
     * Save a teamProject.
     *
     * @param teamProject the entity to save
     * @return the persisted entity
     */
    TeamProject save(TeamProject teamProject);

    /**
     *  Get all the teamProjects.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TeamProject> findAll(Pageable pageable);

    /**
     *  Get the "id" teamProject.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TeamProject findOne(Long id);

    /**
     *  Delete the "id" teamProject.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the teamProject corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TeamProject> search(String query, Pageable pageable);
}
