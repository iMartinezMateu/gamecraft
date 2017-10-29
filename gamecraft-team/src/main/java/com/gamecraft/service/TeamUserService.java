package com.gamecraft.service;

import com.gamecraft.domain.TeamUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TeamUser.
 */
public interface TeamUserService {

    /**
     * Save a teamUser.
     *
     * @param teamUser the entity to save
     * @return the persisted entity
     */
    TeamUser save(TeamUser teamUser);

    /**
     *  Get all the teamUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TeamUser> findAll(Pageable pageable);

    /**
     *  Get the "id" teamUser.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TeamUser findOne(Long id);

    /**
     *  Delete the "id" teamUser.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the teamUser corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TeamUser> search(String query, Pageable pageable);
}
