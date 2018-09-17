package com.gamecraft.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.gamecraft.domain.Report;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.ReportRepository;
import com.gamecraft.repository.search.ReportSearchRepository;
import com.gamecraft.service.dto.ReportCriteria;

import com.gamecraft.domain.enumeration.ReportStatus;

/**
 * Service for executing complex queries for Report entities in the database.
 * The main input is a {@link ReportCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Report} or a {@link Page} of {@link Report} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportQueryService extends QueryService<Report> {

    private final Logger log = LoggerFactory.getLogger(ReportQueryService.class);


    private final ReportRepository reportRepository;

    private final ReportSearchRepository reportSearchRepository;

    public ReportQueryService(ReportRepository reportRepository, ReportSearchRepository reportSearchRepository) {
        this.reportRepository = reportRepository;
        this.reportSearchRepository = reportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Report} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Report> findByCriteria(ReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Report> specification = createSpecification(criteria);
        return reportRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Report} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Report> findByCriteria(ReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Report> specification = createSpecification(criteria);
        return reportRepository.findAll(specification, page);
    }

    /**
     * Function to convert ReportCriteria to a {@link Specifications}
     */
    private Specifications<Report> createSpecification(ReportCriteria criteria) {
        Specifications<Report> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Report_.id));
            }
            if (criteria.getPipelineId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPipelineId(), Report_.pipelineId));
            }
            if (criteria.getReportDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportDate(), Report_.reportDate));
            }
            if (criteria.getReportContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportContent(), Report_.reportContent));
            }
            if (criteria.getReportStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getReportStatus(), Report_.reportStatus));
            }
        }
        return specification;
    }

}
