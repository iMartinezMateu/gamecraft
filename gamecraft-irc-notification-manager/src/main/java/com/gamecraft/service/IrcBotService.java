package com.gamecraft.service;

import com.gamecraft.domain.IrcBot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing IrcBot.
 */
public interface IrcBotService {

    /**
     * Save a ircBot.
     *
     * @param ircBot the entity to save
     * @return the persisted entity
     */
    IrcBot save(IrcBot ircBot);

    /**
     *  Get all the ircBots.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IrcBot> findAll(Pageable pageable);

    /**
     *  Get the "id" ircBot.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IrcBot findOne(Long id);

    /**
     *  Delete the "id" ircBot.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ircBot corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IrcBot> search(String query, Pageable pageable);
}
