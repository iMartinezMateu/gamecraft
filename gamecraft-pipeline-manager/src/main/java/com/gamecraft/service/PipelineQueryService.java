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

import com.gamecraft.domain.Pipeline;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.PipelineRepository;
import com.gamecraft.repository.search.PipelineSearchRepository;
import com.gamecraft.service.dto.PipelineCriteria;

import com.gamecraft.domain.enumeration.PipelineRepositoryType;
import com.gamecraft.domain.enumeration.PipelineNotificatorType;
import com.gamecraft.domain.enumeration.PipelinePublicationService;
import com.gamecraft.domain.enumeration.PipelineStatus;
import com.gamecraft.domain.enumeration.PipelineScheduleType;

/**
 * Service for executing complex queries for Pipeline entities in the database.
 * The main input is a {@link PipelineCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link Pipeline} or a {@link Page} of {%link Pipeline} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class PipelineQueryService extends QueryService<Pipeline> {

    private final Logger log = LoggerFactory.getLogger(PipelineQueryService.class);


    private final PipelineRepository pipelineRepository;

    private final PipelineSearchRepository pipelineSearchRepository;

    public PipelineQueryService(PipelineRepository pipelineRepository, PipelineSearchRepository pipelineSearchRepository) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineSearchRepository = pipelineSearchRepository;
    }

    /**
     * Return a {@link List} of {%link Pipeline} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Pipeline> findByCriteria(PipelineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Pipeline> specification = createSpecification(criteria);
        return pipelineRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {%link Pipeline} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Pipeline> findByCriteria(PipelineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Pipeline> specification = createSpecification(criteria);
        return pipelineRepository.findAll(specification, page);
    }

    /**
     * Function to convert PipelineCriteria to a {@link Specifications}
     */
    private Specifications<Pipeline> createSpecification(PipelineCriteria criteria) {
        Specifications<Pipeline> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Pipeline_.id));
            }
            if (criteria.getPipelineName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineName(), Pipeline_.pipelineName));
            }
            if (criteria.getPipelineDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineDescription(), Pipeline_.pipelineDescription));
            }
            if (criteria.getPipelineProjectId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPipelineProjectId(), Pipeline_.pipelineProjectId));
            }
            if (criteria.getPipelineRepositoryAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineRepositoryAddress(), Pipeline_.pipelineRepositoryAddress));
            }
            if (criteria.getPipelineRepositoryUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineRepositoryUsername(), Pipeline_.pipelineRepositoryUsername));
            }
            if (criteria.getPipelineRepositoryPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineRepositoryPassword(), Pipeline_.pipelineRepositoryPassword));
            }
            if (criteria.getPipelineEngineCompilerPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineEngineCompilerPath(), Pipeline_.pipelineEngineCompilerPath));
            }
            if (criteria.getPipelineEngineCompilerArguments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineEngineCompilerArguments(), Pipeline_.pipelineEngineCompilerArguments));
            }
            if (criteria.getPipelineFtpAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineFtpAddress(), Pipeline_.pipelineFtpAddress));
            }
            if (criteria.getPipelineFtpUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineFtpUsername(), Pipeline_.pipelineFtpUsername));
            }
            if (criteria.getPipelineFtpPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineFtpPassword(), Pipeline_.pipelineFtpPassword));
            }
            if (criteria.getPipelineNotificatorDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineNotificatorDetails(), Pipeline_.pipelineNotificatorDetails));
            }
            if (criteria.getPipelineRepositoryType() != null) {
                specification = specification.and(buildSpecification(criteria.getPipelineRepositoryType(), Pipeline_.pipelineRepositoryType));
            }
            if (criteria.getPipelineNotificatorType() != null) {
                specification = specification.and(buildSpecification(criteria.getPipelineNotificatorType(), Pipeline_.pipelineNotificatorType));
            }
            if (criteria.getPipelineDropboxAppKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineDropboxAppKey(), Pipeline_.pipelineDropboxAppKey));
            }
            if (criteria.getPipelineDropboxToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineDropboxToken(), Pipeline_.pipelineDropboxToken));
            }
            if (criteria.getPipelinePublicationService() != null) {
                specification = specification.and(buildSpecification(criteria.getPipelinePublicationService(), Pipeline_.pipelinePublicationService));
            }
            if (criteria.getPipelineFtpPort() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPipelineFtpPort(), Pipeline_.pipelineFtpPort));
            }
            if (criteria.getPipelineStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPipelineStatus(), Pipeline_.pipelineStatus));
            }
            if (criteria.getPipelineProjectName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineProjectName(), Pipeline_.pipelineProjectName));
            }
            if (criteria.getPipelineScheduleType() != null) {
                specification = specification.and(buildSpecification(criteria.getPipelineScheduleType(), Pipeline_.pipelineScheduleType));
            }
            if (criteria.getPipelineScheduleCronJob() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineScheduleCronJob(), Pipeline_.pipelineScheduleCronJob));
            }
            if (criteria.getPipelineRepositoryBranch() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineRepositoryBranch(), Pipeline_.pipelineRepositoryBranch));
            }
        }
        return specification;
    }

}
