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

import com.gamecraft.domain.SlackAccount;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.SlackAccountRepository;
import com.gamecraft.repository.search.SlackAccountSearchRepository;
import com.gamecraft.service.dto.SlackAccountCriteria;


/**
 * Service for executing complex queries for SlackAccount entities in the database.
 * The main input is a {@link SlackAccountCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link SlackAccount} or a {@link Page} of {%link SlackAccount} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class SlackAccountQueryService extends QueryService<SlackAccount> {

    private final Logger log = LoggerFactory.getLogger(SlackAccountQueryService.class);


    private final SlackAccountRepository slackAccountRepository;

    private final SlackAccountSearchRepository slackAccountSearchRepository;

    public SlackAccountQueryService(SlackAccountRepository slackAccountRepository, SlackAccountSearchRepository slackAccountSearchRepository) {
        this.slackAccountRepository = slackAccountRepository;
        this.slackAccountSearchRepository = slackAccountSearchRepository;
    }

    /**
     * Return a {@link List} of {%link SlackAccount} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SlackAccount> findByCriteria(SlackAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SlackAccount> specification = createSpecification(criteria);
        return slackAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link SlackAccount} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SlackAccount> findByCriteria(SlackAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SlackAccount> specification = createSpecification(criteria);
        return slackAccountRepository.findAll(specification, page);
    }

    /**
     * Function to convert SlackAccountCriteria to a {@link Specifications}
     */
    private Specifications<SlackAccount> createSpecification(SlackAccountCriteria criteria) {
        Specifications<SlackAccount> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SlackAccount_.id));
            }
            if (criteria.getSlackAccountName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlackAccountName(), SlackAccount_.slackAccountName));
            }
            if (criteria.getSlackAccountDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlackAccountDescription(), SlackAccount_.slackAccountDescription));
            }
            if (criteria.getSlackAccountToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlackAccountToken(), SlackAccount_.slackAccountToken));
            }
            if (criteria.getSlackAccountEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getSlackAccountEnabled(), SlackAccount_.slackAccountEnabled));
            }
        }
        return specification;
    }

}
