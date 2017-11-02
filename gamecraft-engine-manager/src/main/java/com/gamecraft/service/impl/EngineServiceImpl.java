package com.gamecraft.service.impl;

import com.gamecraft.service.EngineService;
import com.gamecraft.domain.Engine;
import com.gamecraft.repository.EngineRepository;
import com.gamecraft.repository.search.EngineSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Engine.
 */
@Service
@Transactional
public class EngineServiceImpl implements EngineService{

    private final Logger log = LoggerFactory.getLogger(EngineServiceImpl.class);

    private final EngineRepository engineRepository;

    private final EngineSearchRepository engineSearchRepository;

    public EngineServiceImpl(EngineRepository engineRepository, EngineSearchRepository engineSearchRepository) {
        this.engineRepository = engineRepository;
        this.engineSearchRepository = engineSearchRepository;
    }

    /**
     * Save a engine.
     *
     * @param engine the entity to save
     * @return the persisted entity
     */
    @Override
    public Engine save(Engine engine) {
        log.debug("Request to save Engine : {}", engine);
        Engine result = engineRepository.save(engine);
        engineSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the engines.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Engine> findAll(Pageable pageable) {
        log.debug("Request to get all Engines");
        return engineRepository.findAll(pageable);
    }

    /**
     *  Get one engine by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Engine findOne(Long id) {
        log.debug("Request to get Engine : {}", id);
        return engineRepository.findOne(id);
    }

    /**
     *  Delete the  engine by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Engine : {}", id);
        engineRepository.delete(id);
        engineSearchRepository.delete(id);
    }

    /**
     * Search for the engine corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Engine> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Engines for query {}", query);
        Page<Engine> result = engineSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
