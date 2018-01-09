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

import com.gamecraft.domain.TwitterBot;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.TwitterBotRepository;
import com.gamecraft.repository.search.TwitterBotSearchRepository;
import com.gamecraft.service.dto.TwitterBotCriteria;


/**
 * Service for executing complex queries for TwitterBot entities in the database.
 * The main input is a {@link TwitterBotCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link TwitterBot} or a {@link Page} of {%link TwitterBot} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class TwitterBotQueryService extends QueryService<TwitterBot> {

    private final Logger log = LoggerFactory.getLogger(TwitterBotQueryService.class);


    private final TwitterBotRepository twitterBotRepository;

    private final TwitterBotSearchRepository twitterBotSearchRepository;

    public TwitterBotQueryService(TwitterBotRepository twitterBotRepository, TwitterBotSearchRepository twitterBotSearchRepository) {
        this.twitterBotRepository = twitterBotRepository;
        this.twitterBotSearchRepository = twitterBotSearchRepository;
    }

    /**
     * Return a {@link List} of {%link TwitterBot} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TwitterBot> findByCriteria(TwitterBotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TwitterBot> specification = createSpecification(criteria);
        return twitterBotRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link TwitterBot} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TwitterBot> findByCriteria(TwitterBotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TwitterBot> specification = createSpecification(criteria);
        return twitterBotRepository.findAll(specification, page);
    }

    /**
     * Function to convert TwitterBotCriteria to a {@link Specifications}
     */
    private Specifications<TwitterBot> createSpecification(TwitterBotCriteria criteria) {
        Specifications<TwitterBot> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TwitterBot_.id));
            }
            if (criteria.getTwitterBotName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTwitterBotName(), TwitterBot_.twitterBotName));
            }
            if (criteria.getTwitterBotDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTwitterBotDescription(), TwitterBot_.twitterBotDescription));
            }
            if (criteria.getTwitterBotConsumerKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTwitterBotConsumerKey(), TwitterBot_.twitterBotConsumerKey));
            }
            if (criteria.getTwitterBotConsumerSecret() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTwitterBotConsumerSecret(), TwitterBot_.twitterBotConsumerSecret));
            }
            if (criteria.getTwitterBotAccessToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTwitterBotAccessToken(), TwitterBot_.twitterBotAccessToken));
            }
            if (criteria.getTwitterBotAccessTokenSecret() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTwitterBotAccessTokenSecret(), TwitterBot_.twitterBotAccessTokenSecret));
            }
            if (criteria.getTwitterBotEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getTwitterBotEnabled(), TwitterBot_.twitterBotEnabled));
            }
        }
        return specification;
    }

}
