package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.SonarInstance;
import com.gamecraft.service.SonarInstanceService;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.SonarInstanceCriteria;
import com.gamecraft.service.SonarInstanceQueryService;
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
 * REST controller for managing SonarInstance.
 */
@RestController
@RequestMapping("/api")
public class SonarInstanceResource {

    private final Logger log = LoggerFactory.getLogger(SonarInstanceResource.class);

    private static final String ENTITY_NAME = "sonarInstance";

    private final SonarInstanceService sonarInstanceService;

    private final SonarInstanceQueryService sonarInstanceQueryService;

    public SonarInstanceResource(SonarInstanceService sonarInstanceService, SonarInstanceQueryService sonarInstanceQueryService) {
        this.sonarInstanceService = sonarInstanceService;
        this.sonarInstanceQueryService = sonarInstanceQueryService;
    }

    /**
     * POST  /sonar-instances : Create a new sonarInstance.
     *
     * @param sonarInstance the sonarInstance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sonarInstance, or with status 400 (Bad Request) if the sonarInstance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sonar-instances")
    @Timed
    public ResponseEntity<SonarInstance> createSonarInstance(@Valid @RequestBody SonarInstance sonarInstance) throws URISyntaxException {
        log.debug("REST request to save SonarInstance : {}", sonarInstance);
        if (sonarInstance.getId() != null) {
            throw new BadRequestAlertException("A new sonarInstance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SonarInstance result = sonarInstanceService.save(sonarInstance);
        return ResponseEntity.created(new URI("/api/sonar-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sonar-instances : Updates an existing sonarInstance.
     *
     * @param sonarInstance the sonarInstance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sonarInstance,
     * or with status 400 (Bad Request) if the sonarInstance is not valid,
     * or with status 500 (Internal Server Error) if the sonarInstance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sonar-instances")
    @Timed
    public ResponseEntity<SonarInstance> updateSonarInstance(@Valid @RequestBody SonarInstance sonarInstance) throws URISyntaxException {
        log.debug("REST request to update SonarInstance : {}", sonarInstance);
        if (sonarInstance.getId() == null) {
            return createSonarInstance(sonarInstance);
        }
        SonarInstance result = sonarInstanceService.save(sonarInstance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sonarInstance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sonar-instances : get all the sonarInstances.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sonarInstances in body
     */
    @GetMapping("/sonar-instances")
    @Timed
    public ResponseEntity<List<SonarInstance>> getAllSonarInstances(SonarInstanceCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get SonarInstances by criteria: {}", criteria);
        Page<SonarInstance> page = sonarInstanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sonar-instances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sonar-instances/:id : get the "id" sonarInstance.
     *
     * @param id the id of the sonarInstance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sonarInstance, or with status 404 (Not Found)
     */
    @GetMapping("/sonar-instances/{id}")
    @Timed
    public ResponseEntity<SonarInstance> getSonarInstance(@PathVariable Long id) {
        log.debug("REST request to get SonarInstance : {}", id);
        SonarInstance sonarInstance = sonarInstanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sonarInstance));
    }

    /**
     * DELETE  /sonar-instances/:id : delete the "id" sonarInstance.
     *
     * @param id the id of the sonarInstance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sonar-instances/{id}")
    @Timed
    public ResponseEntity<Void> deleteSonarInstance(@PathVariable Long id) {
        log.debug("REST request to delete SonarInstance : {}", id);
        sonarInstanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sonar-instances?query=:query : search for the sonarInstance corresponding
     * to the query.
     *
     * @param query the query of the sonarInstance search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sonar-instances")
    @Timed
    public ResponseEntity<List<SonarInstance>> searchSonarInstances(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of SonarInstances for query {}", query);
        Page<SonarInstance> page = sonarInstanceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sonar-instances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
