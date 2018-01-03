package com.gamecraft.service.impl;

import com.gamecraft.service.TelegramBotService;
import com.gamecraft.domain.TelegramBot;
import com.gamecraft.repository.TelegramBotRepository;
import com.gamecraft.repository.search.TelegramBotSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TelegramBot.
 */
@Service
@Transactional
public class TelegramBotServiceImpl implements TelegramBotService{

    private final Logger log = LoggerFactory.getLogger(TelegramBotServiceImpl.class);

    private final TelegramBotRepository telegramBotRepository;

    private final TelegramBotSearchRepository telegramBotSearchRepository;

    public TelegramBotServiceImpl(TelegramBotRepository telegramBotRepository, TelegramBotSearchRepository telegramBotSearchRepository) {
        this.telegramBotRepository = telegramBotRepository;
        this.telegramBotSearchRepository = telegramBotSearchRepository;
    }

    /**
     * Save a telegramBot.
     *
     * @param telegramBot the entity to save
     * @return the persisted entity
     */
    @Override
    public TelegramBot save(TelegramBot telegramBot) {
        log.debug("Request to save TelegramBot : {}", telegramBot);
        TelegramBot result = telegramBotRepository.save(telegramBot);
        telegramBotSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the telegramBots.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TelegramBot> findAll(Pageable pageable) {
        log.debug("Request to get all TelegramBots");
        return telegramBotRepository.findAll(pageable);
    }

    /**
     *  Get one telegramBot by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TelegramBot findOne(Long id) {
        log.debug("Request to get TelegramBot : {}", id);
        return telegramBotRepository.findOne(id);
    }

    /**
     *  Delete the  telegramBot by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TelegramBot : {}", id);
        telegramBotRepository.delete(id);
        telegramBotSearchRepository.delete(id);
    }

    /**
     * Search for the telegramBot corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TelegramBot> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TelegramBots for query {}", query);
        Page<TelegramBot> result = telegramBotSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
