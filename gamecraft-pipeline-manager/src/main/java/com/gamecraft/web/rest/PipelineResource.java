package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.Pipeline;
import com.gamecraft.service.PipelineService;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.PipelineCriteria;
import com.gamecraft.service.PipelineQueryService;
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

/**
 * REST controller for managing Pipeline.
 */
@RestController
@RequestMapping("/api")
public class PipelineResource {

    private final Logger log = LoggerFactory.getLogger(PipelineResource.class);

    private static final String ENTITY_NAME = "pipeline";

    private final PipelineService pipelineService;

    private final PipelineQueryService pipelineQueryService;

    public PipelineResource(PipelineService pipelineService, PipelineQueryService pipelineQueryService) {
        this.pipelineService = pipelineService;
        this.pipelineQueryService = pipelineQueryService;
    }

    /**
     * POST  /pipelines : Create a new pipeline.
     *
     * @param pipeline the pipeline to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pipeline, or with status 400 (Bad Request) if the pipeline has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pipelines")
    @Timed
    public ResponseEntity<Pipeline> createPipeline(@Valid @RequestBody Pipeline pipeline) throws URISyntaxException {
        log.debug("REST request to save Pipeline : {}", pipeline);
        if (pipeline.getId() != null) {
            throw new BadRequestAlertException("A new pipeline cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pipeline result = pipelineService.save(pipeline);
        return ResponseEntity.created(new URI("/api/pipelines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pipelines : Updates an existing pipeline.
     *
     * @param pipeline the pipeline to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pipeline,
     * or with status 400 (Bad Request) if the pipeline is not valid,
     * or with status 500 (Internal Server Error) if the pipeline couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pipelines")
    @Timed
    public ResponseEntity<Pipeline> updatePipeline(@Valid @RequestBody Pipeline pipeline) throws URISyntaxException {
        log.debug("REST request to update Pipeline : {}", pipeline);
        if (pipeline.getId() == null) {
            return createPipeline(pipeline);
        }
        Pipeline result = pipelineService.save(pipeline);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pipeline.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pipelines : get all the pipelines.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of pipelines in body
     */
    @GetMapping("/pipelines")
    @Timed
    public ResponseEntity<List<Pipeline>> getAllPipelines(PipelineCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get Pipelines by criteria: {}", criteria);
        Page<Pipeline> page = pipelineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pipelines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pipelines/:id : get the "id" pipeline.
     *
     * @param id the id of the pipeline to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pipeline, or with status 404 (Not Found)
     */
    @GetMapping("/pipelines/{id}")
    @Timed
    public ResponseEntity<Pipeline> getPipeline(@PathVariable Long id) {
        log.debug("REST request to get Pipeline : {}", id);
        Pipeline pipeline = pipelineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pipeline));
    }

    /**
     * GET  /pipelines/:id/execute : execute the "id" pipeline.
     *
     * @param id the id of the pipeline to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pipeline, or with status 404 (Not Found)
     */
    @GetMapping("/pipelines/{id}/execute")
    @Timed
    public ResponseEntity<Void> executePipeline(@PathVariable Long id) {
        log.debug("REST request to execute Pipeline : {}", id);
        pipelineService.execute(id);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /pipelines/:id/stop : stop the "id" pipeline.
     *
     * @param id the id of the pipeline to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pipeline, or with status 404 (Not Found)
     */
    @GetMapping("/pipelines/{id}/stop")
    @Timed
    public ResponseEntity<Void> stopPipeline(@PathVariable Long id) {
        log.debug("REST request to execute Pipeline : {}", id);
        pipelineService.stop(id);
        return ResponseEntity.ok().build();
    }


    /**
     * DELETE  /pipelines/:id : delete the "id" pipeline.
     *
     * @param id the id of the pipeline to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pipelines/{id}")
    @Timed
    public ResponseEntity<Void> deletePipeline(@PathVariable Long id) {
        log.debug("REST request to delete Pipeline : {}", id);
        pipelineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pipelines?query=:query : search for the pipeline corresponding
     * to the query.
     *
     * @param query the query of the pipeline search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/pipelines")
    @Timed
    public ResponseEntity<List<Pipeline>> searchPipelines(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Pipelines for query {}", query);
        Page<Pipeline> page = pipelineService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pipelines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
