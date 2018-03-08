package com.gamecraft.service;

import com.gamecraft.domain.Groups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Groups.
 */
public interface GroupsService {

    /**
     * Save a groups.
     *
     * @param groups the entity to save
     * @return the persisted entity
     */
    Groups save(Groups groups);

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Groups> findAll(Pageable pageable);

    /**
     * Get the "id" groups.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Groups findOne(Long id);

    /**
     * Delete the "id" groups.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the groups corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Groups> search(String query, Pageable pageable);
}
