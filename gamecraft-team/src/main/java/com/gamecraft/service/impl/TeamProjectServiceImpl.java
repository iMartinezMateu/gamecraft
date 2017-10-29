package com.gamecraft.service.impl;

import com.gamecraft.service.TeamProjectService;
import com.gamecraft.domain.TeamProject;
import com.gamecraft.repository.TeamProjectRepository;
import com.gamecraft.repository.search.TeamProjectSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TeamProject.
 */
@Service
@Transactional
public class TeamProjectServiceImpl implements TeamProjectService{

    private final Logger log = LoggerFactory.getLogger(TeamProjectServiceImpl.class);

    private final TeamProjectRepository teamProjectRepository;

    private final TeamProjectSearchRepository teamProjectSearchRepository;

    public TeamProjectServiceImpl(TeamProjectRepository teamProjectRepository, TeamProjectSearchRepository teamProjectSearchRepository) {
        this.teamProjectRepository = teamProjectRepository;
        this.teamProjectSearchRepository = teamProjectSearchRepository;
    }

    /**
     * Save a teamProject.
     *
     * @param teamProject the entity to save
     * @return the persisted entity
     */
    @Override
    public TeamProject save(TeamProject teamProject) {
        log.debug("Request to save TeamProject : {}", teamProject);
        TeamProject result = teamProjectRepository.save(teamProject);
        teamProjectSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the teamProjects.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamProject> findAll(Pageable pageable) {
        log.debug("Request to get all TeamProjects");
        return teamProjectRepository.findAll(pageable);
    }

    /**
     *  Get one teamProject by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TeamProject findOne(Long id) {
        log.debug("Request to get TeamProject : {}", id);
        return teamProjectRepository.findOne(id);
    }

    /**
     *  Delete the  teamProject by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TeamProject : {}", id);
        teamProjectRepository.delete(id);
        teamProjectSearchRepository.delete(id);
    }

    /**
     * Search for the teamProject corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamProject> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TeamProjects for query {}", query);
        Page<TeamProject> result = teamProjectSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
