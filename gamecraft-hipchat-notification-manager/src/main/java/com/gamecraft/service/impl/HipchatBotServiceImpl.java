package com.gamecraft.service.impl;

import com.gamecraft.service.HipchatBotService;
import com.gamecraft.domain.HipchatBot;
import com.gamecraft.repository.HipchatBotRepository;
import com.gamecraft.repository.search.HipchatBotSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing HipchatBot.
 */
@Service
@Transactional
public class HipchatBotServiceImpl implements HipchatBotService{

    private final Logger log = LoggerFactory.getLogger(HipchatBotServiceImpl.class);

    private final HipchatBotRepository hipchatBotRepository;

    private final HipchatBotSearchRepository hipchatBotSearchRepository;

    public HipchatBotServiceImpl(HipchatBotRepository hipchatBotRepository, HipchatBotSearchRepository hipchatBotSearchRepository) {
        this.hipchatBotRepository = hipchatBotRepository;
        this.hipchatBotSearchRepository = hipchatBotSearchRepository;
    }

    /**
     * Save a hipchatBot.
     *
     * @param hipchatBot the entity to save
     * @return the persisted entity
     */
    @Override
    public HipchatBot save(HipchatBot hipchatBot) {
        log.debug("Request to save HipchatBot : {}", hipchatBot);
        HipchatBot result = hipchatBotRepository.save(hipchatBot);
        hipchatBotSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the hipchatBots.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HipchatBot> findAll(Pageable pageable) {
        log.debug("Request to get all HipchatBots");
        return hipchatBotRepository.findAll(pageable);
    }

    /**
     *  Get one hipchatBot by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public HipchatBot findOne(Long id) {
        log.debug("Request to get HipchatBot : {}", id);
        return hipchatBotRepository.findOne(id);
    }

    /**
     *  Delete the  hipchatBot by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HipchatBot : {}", id);
        hipchatBotRepository.delete(id);
        hipchatBotSearchRepository.delete(id);
    }

    /**
     * Search for the hipchatBot corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HipchatBot> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HipchatBots for query {}", query);
        Page<HipchatBot> result = hipchatBotSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
