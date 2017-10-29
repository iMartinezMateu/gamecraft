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

import com.gamecraft.domain.TeamUser;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.TeamUserRepository;
import com.gamecraft.repository.search.TeamUserSearchRepository;
import com.gamecraft.service.dto.TeamUserCriteria;


/**
 * Service for executing complex queries for TeamUser entities in the database.
 * The main input is a {@link TeamUserCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link TeamUser} or a {@link Page} of {%link TeamUser} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class TeamUserQueryService extends QueryService<TeamUser> {

    private final Logger log = LoggerFactory.getLogger(TeamUserQueryService.class);


    private final TeamUserRepository teamUserRepository;

    private final TeamUserSearchRepository teamUserSearchRepository;

    public TeamUserQueryService(TeamUserRepository teamUserRepository, TeamUserSearchRepository teamUserSearchRepository) {
        this.teamUserRepository = teamUserRepository;
        this.teamUserSearchRepository = teamUserSearchRepository;
    }

    /**
     * Return a {@link List} of {%link TeamUser} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamUser> findByCriteria(TeamUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TeamUser> specification = createSpecification(criteria);
        return teamUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link TeamUser} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamUser> findByCriteria(TeamUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TeamUser> specification = createSpecification(criteria);
        return teamUserRepository.findAll(specification, page);
    }

    /**
     * Function to convert TeamUserCriteria to a {@link Specifications}
     */
    private Specifications<TeamUser> createSpecification(TeamUserCriteria criteria) {
        Specifications<TeamUser> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TeamUser_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), TeamUser_.userId));
            }
            if (criteria.getTeamId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTeamId(), TeamUser_.teams, Team_.id));
            }
        }
        return specification;
    }

}
