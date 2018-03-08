package com.gamecraft.service.impl;

import com.gamecraft.service.SonarInstanceService;
import com.gamecraft.domain.SonarInstance;
import com.gamecraft.repository.SonarInstanceRepository;
import com.gamecraft.repository.search.SonarInstanceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SonarInstance.
 */
@Service
@Transactional
public class SonarInstanceServiceImpl implements SonarInstanceService{

    private final Logger log = LoggerFactory.getLogger(SonarInstanceServiceImpl.class);

    private final SonarInstanceRepository sonarInstanceRepository;

    private final SonarInstanceSearchRepository sonarInstanceSearchRepository;

    public SonarInstanceServiceImpl(SonarInstanceRepository sonarInstanceRepository, SonarInstanceSearchRepository sonarInstanceSearchRepository) {
        this.sonarInstanceRepository = sonarInstanceRepository;
        this.sonarInstanceSearchRepository = sonarInstanceSearchRepository;
    }

    /**
     * Save a sonarInstance.
     *
     * @param sonarInstance the entity to save
     * @return the persisted entity
     */
    @Override
    public SonarInstance save(SonarInstance sonarInstance) {
        log.debug("Request to save SonarInstance : {}", sonarInstance);
        SonarInstance result = sonarInstanceRepository.save(sonarInstance);
        sonarInstanceSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the sonarInstances.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SonarInstance> findAll(Pageable pageable) {
        log.debug("Request to get all SonarInstances");
        return sonarInstanceRepository.findAll(pageable);
    }

    /**
     *  Get one sonarInstance by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SonarInstance findOne(Long id) {
        log.debug("Request to get SonarInstance : {}", id);
        return sonarInstanceRepository.findOne(id);
    }

    /**
     *  Delete the  sonarInstance by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SonarInstance : {}", id);
        sonarInstanceRepository.delete(id);
        sonarInstanceSearchRepository.delete(id);
    }

    /**
     * Search for the sonarInstance corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SonarInstance> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SonarInstances for query {}", query);
        Page<SonarInstance> result = sonarInstanceSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
