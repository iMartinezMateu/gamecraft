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

import com.gamecraft.domain.IrcBot;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.IrcBotRepository;
import com.gamecraft.repository.search.IrcBotSearchRepository;
import com.gamecraft.service.dto.IrcBotCriteria;


/**
 * Service for executing complex queries for IrcBot entities in the database.
 * The main input is a {@link IrcBotCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link IrcBot} or a {@link Page} of {%link IrcBot} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class IrcBotQueryService extends QueryService<IrcBot> {

    private final Logger log = LoggerFactory.getLogger(IrcBotQueryService.class);


    private final IrcBotRepository ircBotRepository;

    private final IrcBotSearchRepository ircBotSearchRepository;

    public IrcBotQueryService(IrcBotRepository ircBotRepository, IrcBotSearchRepository ircBotSearchRepository) {
        this.ircBotRepository = ircBotRepository;
        this.ircBotSearchRepository = ircBotSearchRepository;
    }

    /**
     * Return a {@link List} of {%link IrcBot} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IrcBot> findByCriteria(IrcBotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<IrcBot> specification = createSpecification(criteria);
        return ircBotRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link IrcBot} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IrcBot> findByCriteria(IrcBotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<IrcBot> specification = createSpecification(criteria);
        return ircBotRepository.findAll(specification, page);
    }

    /**
     * Function to convert IrcBotCriteria to a {@link Specifications}
     */
    private Specifications<IrcBot> createSpecification(IrcBotCriteria criteria) {
        Specifications<IrcBot> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), IrcBot_.id));
            }
            if (criteria.getIrcBotName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIrcBotName(), IrcBot_.ircBotName));
            }
            if (criteria.getIrcBotDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIrcBotDescription(), IrcBot_.ircBotDescription));
            }
            if (criteria.getIrcBotEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIrcBotEnabled(), IrcBot_.ircBotEnabled));
            }
            if (criteria.getIrcServerAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIrcServerAddress(), IrcBot_.ircServerAddress));
            }
            if (criteria.getIrcServerPort() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIrcServerPort(), IrcBot_.ircServerPort));
            }
            if (criteria.getIrcBotNickname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIrcBotNickname(), IrcBot_.ircBotNickname));
            }
        }
        return specification;
    }

}
