package com.gamecraft.service.impl;

import com.gamecraft.service.EmailAccountService;
import com.gamecraft.domain.EmailAccount;
import com.gamecraft.repository.EmailAccountRepository;
import com.gamecraft.repository.search.EmailAccountSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EmailAccount.
 */
@Service
@Transactional
public class EmailAccountServiceImpl implements EmailAccountService{

    private final Logger log = LoggerFactory.getLogger(EmailAccountServiceImpl.class);

    private final EmailAccountRepository emailAccountRepository;

    private final EmailAccountSearchRepository emailAccountSearchRepository;

    public EmailAccountServiceImpl(EmailAccountRepository emailAccountRepository, EmailAccountSearchRepository emailAccountSearchRepository) {
        this.emailAccountRepository = emailAccountRepository;
        this.emailAccountSearchRepository = emailAccountSearchRepository;
    }

    /**
     * Save a emailAccount.
     *
     * @param emailAccount the entity to save
     * @return the persisted entity
     */
    @Override
    public EmailAccount save(EmailAccount emailAccount) {
        log.debug("Request to save EmailAccount : {}", emailAccount);
        EmailAccount result = emailAccountRepository.save(emailAccount);
        emailAccountSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the emailAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmailAccount> findAll(Pageable pageable) {
        log.debug("Request to get all EmailAccounts");
        return emailAccountRepository.findAll(pageable);
    }

    /**
     *  Get one emailAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EmailAccount findOne(Long id) {
        log.debug("Request to get EmailAccount : {}", id);
        return emailAccountRepository.findOne(id);
    }

    /**
     *  Delete the  emailAccount by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmailAccount : {}", id);
        emailAccountRepository.delete(id);
        emailAccountSearchRepository.delete(id);
    }

    /**
     * Search for the emailAccount corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmailAccount> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EmailAccounts for query {}", query);
        Page<EmailAccount> result = emailAccountSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
