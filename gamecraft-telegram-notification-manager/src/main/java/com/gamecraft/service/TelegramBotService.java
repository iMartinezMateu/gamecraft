package com.gamecraft.service;

import com.gamecraft.domain.TelegramBot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TelegramBot.
 */
public interface TelegramBotService {

    /**
     * Save a telegramBot.
     *
     * @param telegramBot the entity to save
     * @return the persisted entity
     */
    TelegramBot save(TelegramBot telegramBot);

    /**
     *  Get all the telegramBots.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TelegramBot> findAll(Pageable pageable);

    /**
     *  Get the "id" telegramBot.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TelegramBot findOne(Long id);

    /**
     *  Delete the "id" telegramBot.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the telegramBot corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TelegramBot> search(String query, Pageable pageable);
}
