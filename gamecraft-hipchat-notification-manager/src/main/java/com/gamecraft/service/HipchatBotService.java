package com.gamecraft.service;

import com.gamecraft.domain.HipchatBot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing HipchatBot.
 */
public interface HipchatBotService {

    /**
     * Save a hipchatBot.
     *
     * @param hipchatBot the entity to save
     * @return the persisted entity
     */
    HipchatBot save(HipchatBot hipchatBot);

    /**
     *  Get all the hipchatBots.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<HipchatBot> findAll(Pageable pageable);

    /**
     *  Get the "id" hipchatBot.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    HipchatBot findOne(Long id);

    /**
     *  Delete the "id" hipchatBot.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the hipchatBot corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<HipchatBot> search(String query, Pageable pageable);
}
