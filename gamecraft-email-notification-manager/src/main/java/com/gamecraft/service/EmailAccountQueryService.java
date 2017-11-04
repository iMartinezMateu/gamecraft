package com.gamecraft.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.gamecraft.domain.EmailAccount;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.EmailAccountRepository;
import com.gamecraft.repository.search.EmailAccountSearchRepository;
import com.gamecraft.service.dto.EmailAccountCriteria;


/**
 * Service for executing complex queries for EmailAccount entities in the database.
 * The main input is a {@link EmailAccountCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link EmailAccount} or a {@link Page} of {%link EmailAccount} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class EmailAccountQueryService extends QueryService<EmailAccount> {

    private final Logger log = LoggerFactory.getLogger(EmailAccountQueryService.class);


    private final EmailAccountRepository emailAccountRepository;

    private final EmailAccountSearchRepository emailAccountSearchRepository;

    public EmailAccountQueryService(EmailAccountRepository emailAccountRepository, EmailAccountSearchRepository emailAccountSearchRepository) {
        this.emailAccountRepository = emailAccountRepository;
        this.emailAccountSearchRepository = emailAccountSearchRepository;
    }

    /**
     * Return a {@link List} of {%link EmailAccount} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmailAccount> findByCriteria(EmailAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<EmailAccount> specification = createSpecification(criteria);
        return emailAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link EmailAccount} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailAccount> findByCriteria(EmailAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<EmailAccount> specification = createSpecification(criteria);
        return emailAccountRepository.findAll(specification, page);
    }

    /**
     * Function to convert EmailAccountCriteria to a {@link Specifications}
     */
    private Specifications<EmailAccount> createSpecification(EmailAccountCriteria criteria) {
        Specifications<EmailAccount> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), EmailAccount_.id));
            }
            if (criteria.getEmailAccountName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAccountName(), EmailAccount_.emailAccountName));
            }
            if (criteria.getEmailSmtpServer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailSmtpServer(), EmailAccount_.emailSmtpServer));
            }
            if (criteria.getEmailSmtpUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailSmtpUsername(), EmailAccount_.emailSmtpUsername));
            }
            if (criteria.getEmailSmtpPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailSmtpPassword(), EmailAccount_.emailSmtpPassword));
            }
            if (criteria.getEmailSmtpUseSSL() != null) {
                specification = specification.and(buildSpecification(criteria.getEmailSmtpUseSSL(), EmailAccount_.emailSmtpUseSSL));
            }
            if (criteria.getEmailSmtpPort() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmailSmtpPort(), EmailAccount_.emailSmtpPort));
            }
            if (criteria.getEmailAccountDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAccountDescription(), EmailAccount_.emailAccountDescription));
            }
            if (criteria.getEmailAccountEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEmailAccountEnabled(), EmailAccount_.emailAccountEnabled));
            }
        }
        return specification;
    }

}
