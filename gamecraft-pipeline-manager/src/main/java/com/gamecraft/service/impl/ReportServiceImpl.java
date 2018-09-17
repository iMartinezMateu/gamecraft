package com.gamecraft.service.impl;

import com.gamecraft.service.ReportService;
import com.gamecraft.domain.Report;
import com.gamecraft.repository.ReportRepository;
import com.gamecraft.repository.search.ReportSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Report.
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepository reportRepository;

    private final ReportSearchRepository reportSearchRepository;

    public ReportServiceImpl(ReportRepository reportRepository, ReportSearchRepository reportSearchRepository) {
        this.reportRepository = reportRepository;
        this.reportSearchRepository = reportSearchRepository;
    }

    /**
     * Save a report.
     *
     * @param report the entity to save
     * @return the persisted entity
     */
    @Override
    public Report save(Report report) {
        log.debug("Request to save Report : {}", report);
        Report result = reportRepository.save(report);
        reportSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the reports.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Report> findAll(Pageable pageable) {
        log.debug("Request to get all Reports");
        return reportRepository.findAll(pageable);
    }

    /**
     * Get one report by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Report findOne(Long id) {
        log.debug("Request to get Report : {}", id);
        return reportRepository.findOne(id);
    }

    /**
     * Delete the report by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        reportRepository.delete(id);
        reportSearchRepository.delete(id);
    }

    /**
     * Search for the report corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Report> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Reports for query {}", query);
        Page<Report> result = reportSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
