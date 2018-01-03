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

import com.gamecraft.domain.TelegramBot;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.TelegramBotRepository;
import com.gamecraft.repository.search.TelegramBotSearchRepository;
import com.gamecraft.service.dto.TelegramBotCriteria;


/**
 * Service for executing complex queries for TelegramBot entities in the database.
 * The main input is a {@link TelegramBotCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link TelegramBot} or a {@link Page} of {%link TelegramBot} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class TelegramBotQueryService extends QueryService<TelegramBot> {

    private final Logger log = LoggerFactory.getLogger(TelegramBotQueryService.class);


    private final TelegramBotRepository telegramBotRepository;

    private final TelegramBotSearchRepository telegramBotSearchRepository;

    public TelegramBotQueryService(TelegramBotRepository telegramBotRepository, TelegramBotSearchRepository telegramBotSearchRepository) {
        this.telegramBotRepository = telegramBotRepository;
        this.telegramBotSearchRepository = telegramBotSearchRepository;
    }

    /**
     * Return a {@link List} of {%link TelegramBot} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TelegramBot> findByCriteria(TelegramBotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TelegramBot> specification = createSpecification(criteria);
        return telegramBotRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link TelegramBot} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TelegramBot> findByCriteria(TelegramBotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TelegramBot> specification = createSpecification(criteria);
        return telegramBotRepository.findAll(specification, page);
    }

    /**
     * Function to convert TelegramBotCriteria to a {@link Specifications}
     */
    private Specifications<TelegramBot> createSpecification(TelegramBotCriteria criteria) {
        Specifications<TelegramBot> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TelegramBot_.id));
            }
            if (criteria.getTelegramBotName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelegramBotName(), TelegramBot_.telegramBotName));
            }
            if (criteria.getTelegramBotDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelegramBotDescription(), TelegramBot_.telegramBotDescription));
            }
            if (criteria.getTelegramBotToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelegramBotToken(), TelegramBot_.telegramBotToken));
            }
            if (criteria.getTelegramBotEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getTelegramBotEnabled(), TelegramBot_.telegramBotEnabled));
            }
        }
        return specification;
    }

}
