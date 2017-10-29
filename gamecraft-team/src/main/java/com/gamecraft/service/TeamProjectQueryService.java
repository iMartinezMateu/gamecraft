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

import com.gamecraft.domain.TeamProject;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.TeamProjectRepository;
import com.gamecraft.repository.search.TeamProjectSearchRepository;
import com.gamecraft.service.dto.TeamProjectCriteria;


/**
 * Service for executing complex queries for TeamProject entities in the database.
 * The main input is a {@link TeamProjectCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link TeamProject} or a {@link Page} of {%link TeamProject} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class TeamProjectQueryService extends QueryService<TeamProject> {

    private final Logger log = LoggerFactory.getLogger(TeamProjectQueryService.class);


    private final TeamProjectRepository teamProjectRepository;

    private final TeamProjectSearchRepository teamProjectSearchRepository;

    public TeamProjectQueryService(TeamProjectRepository teamProjectRepository, TeamProjectSearchRepository teamProjectSearchRepository) {
        this.teamProjectRepository = teamProjectRepository;
        this.teamProjectSearchRepository = teamProjectSearchRepository;
    }

    /**
     * Return a {@link List} of {%link TeamProject} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamProject> findByCriteria(TeamProjectCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TeamProject> specification = createSpecification(criteria);
        return teamProjectRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link TeamProject} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamProject> findByCriteria(TeamProjectCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TeamProject> specification = createSpecification(criteria);
        return teamProjectRepository.findAll(specification, page);
    }

    /**
     * Function to convert TeamProjectCriteria to a {@link Specifications}
     */
    private Specifications<TeamProject> createSpecification(TeamProjectCriteria criteria) {
        Specifications<TeamProject> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TeamProject_.id));
            }
            if (criteria.getTeamId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTeamId(), TeamProject_.teamId));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProjectId(), TeamProject_.projectId));
            }
        }
        return specification;
    }

}
