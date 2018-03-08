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

import com.gamecraft.domain.SonarInstance;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.SonarInstanceRepository;
import com.gamecraft.repository.search.SonarInstanceSearchRepository;
import com.gamecraft.service.dto.SonarInstanceCriteria;


/**
 * Service for executing complex queries for SonarInstance entities in the database.
 * The main input is a {@link SonarInstanceCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link SonarInstance} or a {@link Page} of {%link SonarInstance} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class SonarInstanceQueryService extends QueryService<SonarInstance> {

    private final Logger log = LoggerFactory.getLogger(SonarInstanceQueryService.class);


    private final SonarInstanceRepository sonarInstanceRepository;

    private final SonarInstanceSearchRepository sonarInstanceSearchRepository;

    public SonarInstanceQueryService(SonarInstanceRepository sonarInstanceRepository, SonarInstanceSearchRepository sonarInstanceSearchRepository) {
        this.sonarInstanceRepository = sonarInstanceRepository;
        this.sonarInstanceSearchRepository = sonarInstanceSearchRepository;
    }

    /**
     * Return a {@link List} of {%link SonarInstance} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SonarInstance> findByCriteria(SonarInstanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SonarInstance> specification = createSpecification(criteria);
        return sonarInstanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link SonarInstance} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SonarInstance> findByCriteria(SonarInstanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SonarInstance> specification = createSpecification(criteria);
        return sonarInstanceRepository.findAll(specification, page);
    }

    /**
     * Function to convert SonarInstanceCriteria to a {@link Specifications}
     */
    private Specifications<SonarInstance> createSpecification(SonarInstanceCriteria criteria) {
        Specifications<SonarInstance> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SonarInstance_.id));
            }
            if (criteria.getSonarInstanceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSonarInstanceName(), SonarInstance_.sonarInstanceName));
            }
            if (criteria.getSonarInstanceDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSonarInstanceDescription(), SonarInstance_.sonarInstanceDescription));
            }
            if (criteria.getSonarInstanceRunnerPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSonarInstanceRunnerPath(), SonarInstance_.sonarInstanceRunnerPath));
            }
            if (criteria.getSonarInstanceEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getSonarInstanceEnabled(), SonarInstance_.sonarInstanceEnabled));
            }
        }
        return specification;
    }

}
