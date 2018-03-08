package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.GroupsUser;
import com.gamecraft.service.GroupsUserService;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.GroupsUserCriteria;
import com.gamecraft.service.GroupsUserQueryService;
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
 * REST controller for managing GroupsUser.
 */
@RestController
@RequestMapping("/api")
public class GroupsUserResource {

    private final Logger log = LoggerFactory.getLogger(GroupsUserResource.class);

    private static final String ENTITY_NAME = "groupsUser";

    private final GroupsUserService groupsUserService;

    private final GroupsUserQueryService groupsUserQueryService;

    public GroupsUserResource(GroupsUserService groupsUserService, GroupsUserQueryService groupsUserQueryService) {
        this.groupsUserService = groupsUserService;
        this.groupsUserQueryService = groupsUserQueryService;
    }

    /**
     * POST  /groups-users : Create a new groupsUser.
     *
     * @param groupsUser the groupsUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupsUser, or with status 400 (Bad Request) if the groupsUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groups-users")
    @Timed
    public ResponseEntity<GroupsUser> createGroupsUser(@Valid @RequestBody GroupsUser groupsUser) throws URISyntaxException {
        log.debug("REST request to save GroupsUser : {}", groupsUser);
        if (groupsUser.getId() != null) {
            throw new BadRequestAlertException("A new groupsUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GroupsUser result = groupsUserService.save(groupsUser);
        return ResponseEntity.created(new URI("/api/groups-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups-users : Updates an existing groupsUser.
     *
     * @param groupsUser the groupsUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupsUser,
     * or with status 400 (Bad Request) if the groupsUser is not valid,
     * or with status 500 (Internal Server Error) if the groupsUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups-users")
    @Timed
    public ResponseEntity<GroupsUser> updateGroupsUser(@Valid @RequestBody GroupsUser groupsUser) throws URISyntaxException {
        log.debug("REST request to update GroupsUser : {}", groupsUser);
        if (groupsUser.getId() == null) {
            return createGroupsUser(groupsUser);
        }
        GroupsUser result = groupsUserService.save(groupsUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, groupsUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups-users : get all the groupsUsers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of groupsUsers in body
     */
    @GetMapping("/groups-users")
    @Timed
    public ResponseEntity<List<GroupsUser>> getAllGroupsUsers(GroupsUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GroupsUsers by criteria: {}", criteria);
        Page<GroupsUser> page = groupsUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groups-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /groups-users/:id : get the "id" groupsUser.
     *
     * @param id the id of the groupsUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupsUser, or with status 404 (Not Found)
     */
    @GetMapping("/groups-users/{id}")
    @Timed
    public ResponseEntity<GroupsUser> getGroupsUser(@PathVariable Long id) {
        log.debug("REST request to get GroupsUser : {}", id);
        GroupsUser groupsUser = groupsUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(groupsUser));
    }

    /**
     * DELETE  /groups-users/:id : delete the "id" groupsUser.
     *
     * @param id the id of the groupsUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groups-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteGroupsUser(@PathVariable Long id) {
        log.debug("REST request to delete GroupsUser : {}", id);
        groupsUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/groups-users?query=:query : search for the groupsUser corresponding
     * to the query.
     *
     * @param query the query of the groupsUser search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/groups-users")
    @Timed
    public ResponseEntity<List<GroupsUser>> searchGroupsUsers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GroupsUsers for query {}", query);
        Page<GroupsUser> page = groupsUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/groups-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
