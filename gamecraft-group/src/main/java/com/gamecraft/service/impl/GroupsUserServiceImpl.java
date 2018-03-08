package com.gamecraft.service.impl;

import com.gamecraft.service.GroupsUserService;
import com.gamecraft.domain.GroupsUser;
import com.gamecraft.repository.GroupsUserRepository;
import com.gamecraft.repository.search.GroupsUserSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing GroupsUser.
 */
@Service
@Transactional
public class GroupsUserServiceImpl implements GroupsUserService {

    private final Logger log = LoggerFactory.getLogger(GroupsUserServiceImpl.class);

    private final GroupsUserRepository groupsUserRepository;

    private final GroupsUserSearchRepository groupsUserSearchRepository;

    public GroupsUserServiceImpl(GroupsUserRepository groupsUserRepository, GroupsUserSearchRepository groupsUserSearchRepository) {
        this.groupsUserRepository = groupsUserRepository;
        this.groupsUserSearchRepository = groupsUserSearchRepository;
    }

    /**
     * Save a groupsUser.
     *
     * @param groupsUser the entity to save
     * @return the persisted entity
     */
    @Override
    public GroupsUser save(GroupsUser groupsUser) {
        log.debug("Request to save GroupsUser : {}", groupsUser);
        GroupsUser result = groupsUserRepository.save(groupsUser);
        groupsUserSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the groupsUsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsUser> findAll(Pageable pageable) {
        log.debug("Request to get all GroupsUsers");
        return groupsUserRepository.findAll(pageable);
    }

    /**
     * Get one groupsUser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GroupsUser findOne(Long id) {
        log.debug("Request to get GroupsUser : {}", id);
        return groupsUserRepository.findOne(id);
    }

    /**
     * Delete the groupsUser by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GroupsUser : {}", id);
        groupsUserRepository.delete(id);
        groupsUserSearchRepository.delete(id);
    }

    /**
     * Search for the groupsUser corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GroupsUsers for query {}", query);
        Page<GroupsUser> result = groupsUserSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
