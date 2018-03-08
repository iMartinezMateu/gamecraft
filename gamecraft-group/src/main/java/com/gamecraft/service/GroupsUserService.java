package com.gamecraft.service;

import com.gamecraft.domain.GroupsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing GroupsUser.
 */
public interface GroupsUserService {

    /**
     * Save a groupsUser.
     *
     * @param groupsUser the entity to save
     * @return the persisted entity
     */
    GroupsUser save(GroupsUser groupsUser);

    /**
     * Get all the groupsUsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GroupsUser> findAll(Pageable pageable);

    /**
     * Get the "id" groupsUser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    GroupsUser findOne(Long id);

    /**
     * Delete the "id" groupsUser.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the groupsUser corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GroupsUser> search(String query, Pageable pageable);
}
