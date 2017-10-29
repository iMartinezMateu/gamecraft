package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.TeamUser;
import com.gamecraft.service.TeamUserService;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.TeamUserCriteria;
import com.gamecraft.service.TeamUserQueryService;
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
 * REST controller for managing TeamUser.
 */
@RestController
@RequestMapping("/api")
public class TeamUserResource {

    private final Logger log = LoggerFactory.getLogger(TeamUserResource.class);

    private static final String ENTITY_NAME = "teamUser";

    private final TeamUserService teamUserService;

    private final TeamUserQueryService teamUserQueryService;

    public TeamUserResource(TeamUserService teamUserService, TeamUserQueryService teamUserQueryService) {
        this.teamUserService = teamUserService;
        this.teamUserQueryService = teamUserQueryService;
    }

    /**
     * POST  /team-users : Create a new teamUser.
     *
     * @param teamUser the teamUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamUser, or with status 400 (Bad Request) if the teamUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/team-users")
    @Timed
    public ResponseEntity<TeamUser> createTeamUser(@Valid @RequestBody TeamUser teamUser) throws URISyntaxException {
        log.debug("REST request to save TeamUser : {}", teamUser);
        if (teamUser.getId() != null) {
            throw new BadRequestAlertException("A new teamUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamUser result = teamUserService.save(teamUser);
        return ResponseEntity.created(new URI("/api/team-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /team-users : Updates an existing teamUser.
     *
     * @param teamUser the teamUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teamUser,
     * or with status 400 (Bad Request) if the teamUser is not valid,
     * or with status 500 (Internal Server Error) if the teamUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/team-users")
    @Timed
    public ResponseEntity<TeamUser> updateTeamUser(@Valid @RequestBody TeamUser teamUser) throws URISyntaxException {
        log.debug("REST request to update TeamUser : {}", teamUser);
        if (teamUser.getId() == null) {
            return createTeamUser(teamUser);
        }
        TeamUser result = teamUserService.save(teamUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teamUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /team-users : get all the teamUsers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of teamUsers in body
     */
    @GetMapping("/team-users")
    @Timed
    public ResponseEntity<List<TeamUser>> getAllTeamUsers(TeamUserCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get TeamUsers by criteria: {}", criteria);
        Page<TeamUser> page = teamUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/team-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /team-users/:id : get the "id" teamUser.
     *
     * @param id the id of the teamUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamUser, or with status 404 (Not Found)
     */
    @GetMapping("/team-users/{id}")
    @Timed
    public ResponseEntity<TeamUser> getTeamUser(@PathVariable Long id) {
        log.debug("REST request to get TeamUser : {}", id);
        TeamUser teamUser = teamUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teamUser));
    }

    /**
     * DELETE  /team-users/:id : delete the "id" teamUser.
     *
     * @param id the id of the teamUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/team-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteTeamUser(@PathVariable Long id) {
        log.debug("REST request to delete TeamUser : {}", id);
        teamUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/team-users?query=:query : search for the teamUser corresponding
     * to the query.
     *
     * @param query the query of the teamUser search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/team-users")
    @Timed
    public ResponseEntity<List<TeamUser>> searchTeamUsers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TeamUsers for query {}", query);
        Page<TeamUser> page = teamUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/team-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
