package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.Groups;
import com.gamecraft.service.GroupsService;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.GroupsCriteria;
import com.gamecraft.service.GroupsQueryService;
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
 * REST controller for managing Groups.
 */
@RestController
@RequestMapping("/api")
public class GroupsResource {

    private final Logger log = LoggerFactory.getLogger(GroupsResource.class);

    private static final String ENTITY_NAME = "groups";

    private final GroupsService groupsService;

    private final GroupsQueryService groupsQueryService;

    public GroupsResource(GroupsService groupsService, GroupsQueryService groupsQueryService) {
        this.groupsService = groupsService;
        this.groupsQueryService = groupsQueryService;
    }

    /**
     * POST  /groups : Create a new groups.
     *
     * @param groups the groups to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groups, or with status 400 (Bad Request) if the groups has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groups")
    @Timed
    public ResponseEntity<Groups> createGroups(@Valid @RequestBody Groups groups) throws URISyntaxException {
        log.debug("REST request to save Groups : {}", groups);
        if (groups.getId() != null) {
            throw new BadRequestAlertException("A new groups cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Groups result = groupsService.save(groups);
        return ResponseEntity.created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups : Updates an existing groups.
     *
     * @param groups the groups to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groups,
     * or with status 400 (Bad Request) if the groups is not valid,
     * or with status 500 (Internal Server Error) if the groups couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups")
    @Timed
    public ResponseEntity<Groups> updateGroups(@Valid @RequestBody Groups groups) throws URISyntaxException {
        log.debug("REST request to update Groups : {}", groups);
        if (groups.getId() == null) {
            return createGroups(groups);
        }
        Groups result = groupsService.save(groups);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, groups.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups : get all the groups.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @GetMapping("/groups")
    @Timed
    public ResponseEntity<List<Groups>> getAllGroups(GroupsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Groups by criteria: {}", criteria);
        Page<Groups> page = groupsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /groups/:id : get the "id" groups.
     *
     * @param id the id of the groups to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groups, or with status 404 (Not Found)
     */
    @GetMapping("/groups/{id}")
    @Timed
    public ResponseEntity<Groups> getGroups(@PathVariable Long id) {
        log.debug("REST request to get Groups : {}", id);
        Groups groups = groupsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(groups));
    }

    /**
     * DELETE  /groups/:id : delete the "id" groups.
     *
     * @param id the id of the groups to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteGroups(@PathVariable Long id) {
        log.debug("REST request to delete Groups : {}", id);
        groupsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/groups?query=:query : search for the groups corresponding
     * to the query.
     *
     * @param query the query of the groups search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/groups")
    @Timed
    public ResponseEntity<List<Groups>> searchGroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Groups for query {}", query);
        Page<Groups> page = groupsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
