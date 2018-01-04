package com.gamecraft.service.impl;

import com.gamecraft.service.IrcBotService;
import com.gamecraft.domain.IrcBot;
import com.gamecraft.repository.IrcBotRepository;
import com.gamecraft.repository.search.IrcBotSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing IrcBot.
 */
@Service
@Transactional
public class IrcBotServiceImpl implements IrcBotService{

    private final Logger log = LoggerFactory.getLogger(IrcBotServiceImpl.class);

    private final IrcBotRepository ircBotRepository;

    private final IrcBotSearchRepository ircBotSearchRepository;

    public IrcBotServiceImpl(IrcBotRepository ircBotRepository, IrcBotSearchRepository ircBotSearchRepository) {
        this.ircBotRepository = ircBotRepository;
        this.ircBotSearchRepository = ircBotSearchRepository;
    }

    /**
     * Save a ircBot.
     *
     * @param ircBot the entity to save
     * @return the persisted entity
     */
    @Override
    public IrcBot save(IrcBot ircBot) {
        log.debug("Request to save IrcBot : {}", ircBot);
        IrcBot result = ircBotRepository.save(ircBot);
        ircBotSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the ircBots.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IrcBot> findAll(Pageable pageable) {
        log.debug("Request to get all IrcBots");
        return ircBotRepository.findAll(pageable);
    }

    /**
     *  Get one ircBot by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IrcBot findOne(Long id) {
        log.debug("Request to get IrcBot : {}", id);
        return ircBotRepository.findOne(id);
    }

    /**
     *  Delete the  ircBot by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IrcBot : {}", id);
        ircBotRepository.delete(id);
        ircBotSearchRepository.delete(id);
    }

    /**
     * Search for the ircBot corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IrcBot> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IrcBots for query {}", query);
        Page<IrcBot> result = ircBotSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
