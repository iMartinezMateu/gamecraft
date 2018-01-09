package com.gamecraft.service.impl;

import com.gamecraft.service.TwitterBotService;
import com.gamecraft.domain.TwitterBot;
import com.gamecraft.repository.TwitterBotRepository;
import com.gamecraft.repository.search.TwitterBotSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TwitterBot.
 */
@Service
@Transactional
public class TwitterBotServiceImpl implements TwitterBotService{

    private final Logger log = LoggerFactory.getLogger(TwitterBotServiceImpl.class);

    private final TwitterBotRepository twitterBotRepository;

    private final TwitterBotSearchRepository twitterBotSearchRepository;

    public TwitterBotServiceImpl(TwitterBotRepository twitterBotRepository, TwitterBotSearchRepository twitterBotSearchRepository) {
        this.twitterBotRepository = twitterBotRepository;
        this.twitterBotSearchRepository = twitterBotSearchRepository;
    }

    /**
     * Save a twitterBot.
     *
     * @param twitterBot the entity to save
     * @return the persisted entity
     */
    @Override
    public TwitterBot save(TwitterBot twitterBot) {
        log.debug("Request to save TwitterBot : {}", twitterBot);
        TwitterBot result = twitterBotRepository.save(twitterBot);
        twitterBotSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the twitterBots.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TwitterBot> findAll(Pageable pageable) {
        log.debug("Request to get all TwitterBots");
        return twitterBotRepository.findAll(pageable);
    }

    /**
     *  Get one twitterBot by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TwitterBot findOne(Long id) {
        log.debug("Request to get TwitterBot : {}", id);
        return twitterBotRepository.findOne(id);
    }

    /**
     *  Delete the  twitterBot by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TwitterBot : {}", id);
        twitterBotRepository.delete(id);
        twitterBotSearchRepository.delete(id);
    }

    /**
     * Search for the twitterBot corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TwitterBot> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TwitterBots for query {}", query);
        Page<TwitterBot> result = twitterBotSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
