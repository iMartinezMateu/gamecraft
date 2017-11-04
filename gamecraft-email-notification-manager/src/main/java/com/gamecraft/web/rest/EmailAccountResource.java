package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.EmailAccount;
import com.gamecraft.service.EmailAccountService;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.EmailAccountCriteria;
import com.gamecraft.service.EmailAccountQueryService;
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
 * REST controller for managing EmailAccount.
 */
@RestController
@RequestMapping("/api")
public class EmailAccountResource {

    private final Logger log = LoggerFactory.getLogger(EmailAccountResource.class);

    private static final String ENTITY_NAME = "emailAccount";

    private final EmailAccountService emailAccountService;

    private final EmailAccountQueryService emailAccountQueryService;

    public EmailAccountResource(EmailAccountService emailAccountService, EmailAccountQueryService emailAccountQueryService) {
        this.emailAccountService = emailAccountService;
        this.emailAccountQueryService = emailAccountQueryService;
    }

    /**
     * POST  /email-accounts : Create a new emailAccount.
     *
     * @param emailAccount the emailAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new emailAccount, or with status 400 (Bad Request) if the emailAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/email-accounts")
    @Timed
    public ResponseEntity<EmailAccount> createEmailAccount(@Valid @RequestBody EmailAccount emailAccount) throws URISyntaxException {
        log.debug("REST request to save EmailAccount : {}", emailAccount);
        if (emailAccount.getId() != null) {
            throw new BadRequestAlertException("A new emailAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmailAccount result = emailAccountService.save(emailAccount);
        return ResponseEntity.created(new URI("/api/email-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /email-accounts : Updates an existing emailAccount.
     *
     * @param emailAccount the emailAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated emailAccount,
     * or with status 400 (Bad Request) if the emailAccount is not valid,
     * or with status 500 (Internal Server Error) if the emailAccount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/email-accounts")
    @Timed
    public ResponseEntity<EmailAccount> updateEmailAccount(@Valid @RequestBody EmailAccount emailAccount) throws URISyntaxException {
        log.debug("REST request to update EmailAccount : {}", emailAccount);
        if (emailAccount.getId() == null) {
            return createEmailAccount(emailAccount);
        }
        EmailAccount result = emailAccountService.save(emailAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, emailAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /email-accounts : get all the emailAccounts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of emailAccounts in body
     */
    @GetMapping("/email-accounts")
    @Timed
    public ResponseEntity<List<EmailAccount>> getAllEmailAccounts(EmailAccountCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get EmailAccounts by criteria: {}", criteria);
        Page<EmailAccount> page = emailAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/email-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /email-accounts/:id : get the "id" emailAccount.
     *
     * @param id the id of the emailAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the emailAccount, or with status 404 (Not Found)
     */
    @GetMapping("/email-accounts/{id}")
    @Timed
    public ResponseEntity<EmailAccount> getEmailAccount(@PathVariable Long id) {
        log.debug("REST request to get EmailAccount : {}", id);
        EmailAccount emailAccount = emailAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(emailAccount));
    }

    /**
     * DELETE  /email-accounts/:id : delete the "id" emailAccount.
     *
     * @param id the id of the emailAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/email-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmailAccount(@PathVariable Long id) {
        log.debug("REST request to delete EmailAccount : {}", id);
        emailAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/email-accounts?query=:query : search for the emailAccount corresponding
     * to the query.
     *
     * @param query the query of the emailAccount search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/email-accounts")
    @Timed
    public ResponseEntity<List<EmailAccount>> searchEmailAccounts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of EmailAccounts for query {}", query);
        Page<EmailAccount> page = emailAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/email-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
