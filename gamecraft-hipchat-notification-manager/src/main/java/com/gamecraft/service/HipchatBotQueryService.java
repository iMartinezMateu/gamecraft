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

import com.gamecraft.domain.HipchatBot;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.HipchatBotRepository;
import com.gamecraft.repository.search.HipchatBotSearchRepository;
import com.gamecraft.service.dto.HipchatBotCriteria;


/**
 * Service for executing complex queries for HipchatBot entities in the database.
 * The main input is a {@link HipchatBotCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link HipchatBot} or a {@link Page} of {%link HipchatBot} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class HipchatBotQueryService extends QueryService<HipchatBot> {

    private final Logger log = LoggerFactory.getLogger(HipchatBotQueryService.class);


    private final HipchatBotRepository hipchatBotRepository;

    private final HipchatBotSearchRepository hipchatBotSearchRepository;

    public HipchatBotQueryService(HipchatBotRepository hipchatBotRepository, HipchatBotSearchRepository hipchatBotSearchRepository) {
        this.hipchatBotRepository = hipchatBotRepository;
        this.hipchatBotSearchRepository = hipchatBotSearchRepository;
    }

    /**
     * Return a {@link List} of {%link HipchatBot} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HipchatBot> findByCriteria(HipchatBotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<HipchatBot> specification = createSpecification(criteria);
        return hipchatBotRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link HipchatBot} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HipchatBot> findByCriteria(HipchatBotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<HipchatBot> specification = createSpecification(criteria);
        return hipchatBotRepository.findAll(specification, page);
    }

    /**
     * Function to convert HipchatBotCriteria to a {@link Specifications}
     */
    private Specifications<HipchatBot> createSpecification(HipchatBotCriteria criteria) {
        Specifications<HipchatBot> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), HipchatBot_.id));
            }
            if (criteria.getHipchatBotName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHipchatBotName(), HipchatBot_.hipchatBotName));
            }
            if (criteria.getHipchatBotDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHipchatBotDescription(), HipchatBot_.hipchatBotDescription));
            }
            if (criteria.getHipchatBotToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHipchatBotToken(), HipchatBot_.hipchatBotToken));
            }
            if (criteria.getHipchatBotEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getHipchatBotEnabled(), HipchatBot_.hipchatBotEnabled));
            }
        }
        return specification;
    }

}
