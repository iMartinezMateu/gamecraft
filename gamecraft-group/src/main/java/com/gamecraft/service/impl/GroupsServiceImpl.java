package com.gamecraft.service.impl;

import com.gamecraft.service.GroupsService;
import com.gamecraft.domain.Groups;
import com.gamecraft.repository.GroupsRepository;
import com.gamecraft.repository.search.GroupsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Groups.
 */
@Service
@Transactional
public class GroupsServiceImpl implements GroupsService {

    private final Logger log = LoggerFactory.getLogger(GroupsServiceImpl.class);

    private final GroupsRepository groupsRepository;

    private final GroupsSearchRepository groupsSearchRepository;

    public GroupsServiceImpl(GroupsRepository groupsRepository, GroupsSearchRepository groupsSearchRepository) {
        this.groupsRepository = groupsRepository;
        this.groupsSearchRepository = groupsSearchRepository;
    }

    /**
     * Save a groups.
     *
     * @param groups the entity to save
     * @return the persisted entity
     */
    @Override
    public Groups save(Groups groups) {
        log.debug("Request to save Groups : {}", groups);
        Groups result = groupsRepository.save(groups);
        groupsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Groups> findAll(Pageable pageable) {
        log.debug("Request to get all Groups");
        return groupsRepository.findAll(pageable);
    }

    /**
     * Get one groups by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Groups findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        return groupsRepository.findOne(id);
    }

    /**
     * Delete the groups by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.delete(id);
        groupsSearchRepository.delete(id);
    }

    /**
     * Search for the groups corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Groups> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Groups for query {}", query);
        Page<Groups> result = groupsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
