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

import com.gamecraft.domain.Project;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.ProjectRepository;
import com.gamecraft.repository.search.ProjectSearchRepository;
import com.gamecraft.service.dto.ProjectCriteria;


/**
 * Service for executing complex queries for Project entities in the database.
 * The main input is a {@link ProjectCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link Project} or a {@link Page} of {%link Project} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class ProjectQueryService extends QueryService<Project> {

    private final Logger log = LoggerFactory.getLogger(ProjectQueryService.class);


    private final ProjectRepository projectRepository;

    private final ProjectSearchRepository projectSearchRepository;

    public ProjectQueryService(ProjectRepository projectRepository, ProjectSearchRepository projectSearchRepository) {
        this.projectRepository = projectRepository;
        this.projectSearchRepository = projectSearchRepository;
    }

    /**
     * Return a {@link List} of {%link Project} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Project> findByCriteria(ProjectCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Project> specification = createSpecification(criteria);
        return projectRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link Project} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Project> findByCriteria(ProjectCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Project> specification = createSpecification(criteria);
        return projectRepository.findAll(specification, page);
    }

    /**
     * Function to convert ProjectCriteria to a {@link Specifications}
     */
    private Specifications<Project> createSpecification(ProjectCriteria criteria) {
        Specifications<Project> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Project_.id));
            }
            if (criteria.getProjectName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectName(), Project_.projectName));
            }
            if (criteria.getProjectDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectDescription(), Project_.projectDescription));
            }
            if (criteria.getProjectWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectWebsite(), Project_.projectWebsite));
            }
            if (criteria.getProjectLogo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectLogo(), Project_.projectLogo));
            }
            if (criteria.getProjectArchived() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectArchived(), Project_.projectArchived));
            }
        }
        return specification;
    }

}
