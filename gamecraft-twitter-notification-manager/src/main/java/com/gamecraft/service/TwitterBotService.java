package com.gamecraft.service;

import com.gamecraft.domain.TwitterBot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TwitterBot.
 */
public interface TwitterBotService {

    /**
     * Save a twitterBot.
     *
     * @param twitterBot the entity to save
     * @return the persisted entity
     */
    TwitterBot save(TwitterBot twitterBot);

    /**
     *  Get all the twitterBots.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TwitterBot> findAll(Pageable pageable);

    /**
     *  Get the "id" twitterBot.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TwitterBot findOne(Long id);

    /**
     *  Delete the "id" twitterBot.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the twitterBot corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TwitterBot> search(String query, Pageable pageable);
}
