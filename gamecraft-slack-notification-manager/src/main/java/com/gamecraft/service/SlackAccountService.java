package com.gamecraft.service;

import com.gamecraft.domain.SlackAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SlackAccount.
 */
public interface SlackAccountService {

    /**
     * Save a slackAccount.
     *
     * @param slackAccount the entity to save
     * @return the persisted entity
     */
    SlackAccount save(SlackAccount slackAccount);

    /**
     *  Get all the slackAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SlackAccount> findAll(Pageable pageable);

    /**
     *  Get the "id" slackAccount.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SlackAccount findOne(Long id);

    /**
     *  Delete the "id" slackAccount.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the slackAccount corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SlackAccount> search(String query, Pageable pageable);
}
