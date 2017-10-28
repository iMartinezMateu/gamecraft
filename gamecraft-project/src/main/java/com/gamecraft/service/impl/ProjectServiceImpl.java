package com.gamecraft.service.impl;

import com.gamecraft.service.ProjectService;
import com.gamecraft.domain.Project;
import com.gamecraft.repository.ProjectRepository;
import com.gamecraft.repository.search.ProjectSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    private final ProjectSearchRepository projectSearchRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectSearchRepository projectSearchRepository) {
        this.projectRepository = projectRepository;
        this.projectSearchRepository = projectSearchRepository;
    }

    /**
     * Save a project.
     *
     * @param project the entity to save
     * @return the persisted entity
     */
    @Override
    public Project save(Project project) {
        log.debug("Request to save Project : {}", project);
        Project result = projectRepository.save(project);
        projectSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the projects.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Project> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(pageable);
    }

    /**
     *  Get one project by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Project findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findOne(id);
    }

    /**
     *  Delete the  project by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.delete(id);
        projectSearchRepository.delete(id);
    }

    /**
     * Search for the project corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Project> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Projects for query {}", query);
        Page<Project> result = projectSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
