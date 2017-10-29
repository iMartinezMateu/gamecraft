package com.gamecraft.service.impl;

import com.gamecraft.service.TeamUserService;
import com.gamecraft.domain.TeamUser;
import com.gamecraft.repository.TeamUserRepository;
import com.gamecraft.repository.search.TeamUserSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TeamUser.
 */
@Service
@Transactional
public class TeamUserServiceImpl implements TeamUserService{

    private final Logger log = LoggerFactory.getLogger(TeamUserServiceImpl.class);

    private final TeamUserRepository teamUserRepository;

    private final TeamUserSearchRepository teamUserSearchRepository;

    public TeamUserServiceImpl(TeamUserRepository teamUserRepository, TeamUserSearchRepository teamUserSearchRepository) {
        this.teamUserRepository = teamUserRepository;
        this.teamUserSearchRepository = teamUserSearchRepository;
    }

    /**
     * Save a teamUser.
     *
     * @param teamUser the entity to save
     * @return the persisted entity
     */
    @Override
    public TeamUser save(TeamUser teamUser) {
        log.debug("Request to save TeamUser : {}", teamUser);
        TeamUser result = teamUserRepository.save(teamUser);
        teamUserSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the teamUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamUser> findAll(Pageable pageable) {
        log.debug("Request to get all TeamUsers");
        return teamUserRepository.findAll(pageable);
    }

    /**
     *  Get one teamUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TeamUser findOne(Long id) {
        log.debug("Request to get TeamUser : {}", id);
        return teamUserRepository.findOne(id);
    }

    /**
     *  Delete the  teamUser by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TeamUser : {}", id);
        teamUserRepository.delete(id);
        teamUserSearchRepository.delete(id);
    }

    /**
     * Search for the teamUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TeamUsers for query {}", query);
        Page<TeamUser> result = teamUserSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
