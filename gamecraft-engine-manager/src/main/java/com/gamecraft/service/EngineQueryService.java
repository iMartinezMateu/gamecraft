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

import com.gamecraft.domain.Engine;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.EngineRepository;
import com.gamecraft.repository.search.EngineSearchRepository;
import com.gamecraft.service.dto.EngineCriteria;


/**
 * Service for executing complex queries for Engine entities in the database.
 * The main input is a {@link EngineCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link Engine} or a {@link Page} of {%link Engine} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class EngineQueryService extends QueryService<Engine> {

    private final Logger log = LoggerFactory.getLogger(EngineQueryService.class);


    private final EngineRepository engineRepository;

    private final EngineSearchRepository engineSearchRepository;

    public EngineQueryService(EngineRepository engineRepository, EngineSearchRepository engineSearchRepository) {
        this.engineRepository = engineRepository;
        this.engineSearchRepository = engineSearchRepository;
    }

    /**
     * Return a {@link List} of {%link Engine} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Engine> findByCriteria(EngineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Engine> specification = createSpecification(criteria);
        return engineRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link Engine} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Engine> findByCriteria(EngineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Engine> specification = createSpecification(criteria);
        return engineRepository.findAll(specification, page);
    }

    /**
     * Function to convert EngineCriteria to a {@link Specifications}
     */
    private Specifications<Engine> createSpecification(EngineCriteria criteria) {
        Specifications<Engine> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Engine_.id));
            }
            if (criteria.getEngineName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEngineName(), Engine_.engineName));
            }
            if (criteria.getEngineDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEngineDescription(), Engine_.engineDescription));
            }
            if (criteria.getEngineCompilerPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEngineCompilerPath(), Engine_.engineCompilerPath));
            }
            if (criteria.getEngineCompilerArguments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEngineCompilerArguments(), Engine_.engineCompilerArguments));
            }
        }
        return specification;
    }

}
