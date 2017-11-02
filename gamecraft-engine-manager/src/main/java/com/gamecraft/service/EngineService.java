package com.gamecraft.service;

import com.gamecraft.domain.Engine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Engine.
 */
public interface EngineService {

    /**
     * Save a engine.
     *
     * @param engine the entity to save
     * @return the persisted entity
     */
    Engine save(Engine engine);

    /**
     *  Get all the engines.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Engine> findAll(Pageable pageable);

    /**
     *  Get the "id" engine.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Engine findOne(Long id);

    /**
     *  Delete the "id" engine.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the engine corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Engine> search(String query, Pageable pageable);
}
