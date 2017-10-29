package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.TeamProject;
import com.gamecraft.service.TeamProjectService;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.TeamProjectCriteria;
import com.gamecraft.service.TeamProjectQueryService;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TeamProject.
 */
@RestController
@RequestMapping("/api")
public class TeamProjectResource {

    private final Logger log = LoggerFactory.getLogger(TeamProjectResource.class);

    private static final String ENTITY_NAME = "teamProject";

    private final TeamProjectService teamProjectService;

    private final TeamProjectQueryService teamProjectQueryService;

    public TeamProjectResource(TeamProjectService teamProjectService, TeamProjectQueryService teamProjectQueryService) {
        this.teamProjectService = teamProjectService;
        this.teamProjectQueryService = teamProjectQueryService;
    }

    /**
     * POST  /team-projects : Create a new teamProject.
     *
     * @param teamProject the teamProject to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamProject, or with status 400 (Bad Request) if the teamProject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/team-projects")
    @Timed
    public ResponseEntity<TeamProject> createTeamProject(@Valid @RequestBody TeamProject teamProject) throws URISyntaxException {
        log.debug("REST request to save TeamProject : {}", teamProject);
        if (teamProject.getId() != null) {
            throw new BadRequestAlertException("A new teamProject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamProject result = teamProjectService.save(teamProject);
        return ResponseEntity.created(new URI("/api/team-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /team-projects : Updates an existing teamProject.
     *
     * @param teamProject the teamProject to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teamProject,
     * or with status 400 (Bad Request) if the teamProject is not valid,
     * or with status 500 (Internal Server Error) if the teamProject couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/team-projects")
    @Timed
    public ResponseEntity<TeamProject> updateTeamProject(@Valid @RequestBody TeamProject teamProject) throws URISyntaxException {
        log.debug("REST request to update TeamProject : {}", teamProject);
        if (teamProject.getId() == null) {
            return createTeamProject(teamProject);
        }
        TeamProject result = teamProjectService.save(teamProject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teamProject.getId().toString()))
            .body(result);
    }

    /**
     * GET  /team-projects : get all the teamProjects.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of teamProjects in body
     */
    @GetMapping("/team-projects")
    @Timed
    public ResponseEntity<List<TeamProject>> getAllTeamProjects(TeamProjectCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get TeamProjects by criteria: {}", criteria);
        Page<TeamProject> page = teamProjectQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/team-projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /team-projects/:id : get the "id" teamProject.
     *
     * @param id the id of the teamProject to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamProject, or with status 404 (Not Found)
     */
    @GetMapping("/team-projects/{id}")
    @Timed
    public ResponseEntity<TeamProject> getTeamProject(@PathVariable Long id) {
        log.debug("REST request to get TeamProject : {}", id);
        TeamProject teamProject = teamProjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teamProject));
    }

    /**
     * DELETE  /team-projects/:id : delete the "id" teamProject.
     *
     * @param id the id of the teamProject to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/team-projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteTeamProject(@PathVariable Long id) {
        log.debug("REST request to delete TeamProject : {}", id);
        teamProjectService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/team-projects?query=:query : search for the teamProject corresponding
     * to the query.
     *
     * @param query the query of the teamProject search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/team-projects")
    @Timed
    public ResponseEntity<List<TeamProject>> searchTeamProjects(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TeamProjects for query {}", query);
        Page<TeamProject> page = teamProjectService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/team-projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
