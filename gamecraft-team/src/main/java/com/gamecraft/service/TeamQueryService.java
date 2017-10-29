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

import com.gamecraft.domain.Team;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.TeamRepository;
import com.gamecraft.repository.search.TeamSearchRepository;
import com.gamecraft.service.dto.TeamCriteria;


/**
 * Service for executing complex queries for Team entities in the database.
 * The main input is a {@link TeamCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link Team} or a {@link Page} of {%link Team} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class TeamQueryService extends QueryService<Team> {

    private final Logger log = LoggerFactory.getLogger(TeamQueryService.class);


    private final TeamRepository teamRepository;

    private final TeamSearchRepository teamSearchRepository;

    public TeamQueryService(TeamRepository teamRepository, TeamSearchRepository teamSearchRepository) {
        this.teamRepository = teamRepository;
        this.teamSearchRepository = teamSearchRepository;
    }

    /**
     * Return a {@link List} of {%link Team} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Team> findByCriteria(TeamCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Team> specification = createSpecification(criteria);
        return teamRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link Team} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Team> findByCriteria(TeamCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Team> specification = createSpecification(criteria);
        return teamRepository.findAll(specification, page);
    }

    /**
     * Function to convert TeamCriteria to a {@link Specifications}
     */
    private Specifications<Team> createSpecification(TeamCriteria criteria) {
        Specifications<Team> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Team_.id));
            }
            if (criteria.getTeamName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeamName(), Team_.teamName));
            }
            if (criteria.getTeamDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeamDescription(), Team_.teamDescription));
            }
        }
        return specification;
    }

}
