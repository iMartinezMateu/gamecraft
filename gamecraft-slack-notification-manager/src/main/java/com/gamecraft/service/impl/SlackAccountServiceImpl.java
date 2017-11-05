package com.gamecraft.service.impl;

import com.gamecraft.service.SlackAccountService;
import com.gamecraft.domain.SlackAccount;
import com.gamecraft.repository.SlackAccountRepository;
import com.gamecraft.repository.search.SlackAccountSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SlackAccount.
 */
@Service
@Transactional
public class SlackAccountServiceImpl implements SlackAccountService{

    private final Logger log = LoggerFactory.getLogger(SlackAccountServiceImpl.class);

    private final SlackAccountRepository slackAccountRepository;

    private final SlackAccountSearchRepository slackAccountSearchRepository;

    public SlackAccountServiceImpl(SlackAccountRepository slackAccountRepository, SlackAccountSearchRepository slackAccountSearchRepository) {
        this.slackAccountRepository = slackAccountRepository;
        this.slackAccountSearchRepository = slackAccountSearchRepository;
    }

    /**
     * Save a slackAccount.
     *
     * @param slackAccount the entity to save
     * @return the persisted entity
     */
    @Override
    public SlackAccount save(SlackAccount slackAccount) {
        log.debug("Request to save SlackAccount : {}", slackAccount);
        SlackAccount result = slackAccountRepository.save(slackAccount);
        slackAccountSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the slackAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SlackAccount> findAll(Pageable pageable) {
        log.debug("Request to get all SlackAccounts");
        return slackAccountRepository.findAll(pageable);
    }

    /**
     *  Get one slackAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SlackAccount findOne(Long id) {
        log.debug("Request to get SlackAccount : {}", id);
        return slackAccountRepository.findOne(id);
    }

    /**
     *  Delete the  slackAccount by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SlackAccount : {}", id);
        slackAccountRepository.delete(id);
        slackAccountSearchRepository.delete(id);
    }

    /**
     * Search for the slackAccount corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SlackAccount> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SlackAccounts for query {}", query);
        Page<SlackAccount> result = slackAccountSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
