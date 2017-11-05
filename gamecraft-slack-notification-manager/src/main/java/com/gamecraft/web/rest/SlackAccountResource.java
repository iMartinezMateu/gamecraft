package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.SlackAccount;
import com.gamecraft.service.SlackAccountService;
import com.gamecraft.service.dto.SlackMessage;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.SlackAccountCriteria;
import com.gamecraft.service.SlackAccountQueryService;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SlackAccount.
 */
@RestController
@RequestMapping("/api")
public class SlackAccountResource {

    private final Logger log = LoggerFactory.getLogger(SlackAccountResource.class);

    private static final String ENTITY_NAME = "slackAccount";

    private final SlackAccountService slackAccountService;

    private final SlackAccountQueryService slackAccountQueryService;

    public SlackAccountResource(SlackAccountService slackAccountService, SlackAccountQueryService slackAccountQueryService) {
        this.slackAccountService = slackAccountService;
        this.slackAccountQueryService = slackAccountQueryService;
    }

    /**
     * POST  /slack-accounts : Create a new slackAccount.
     *
     * @param slackAccount the slackAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new slackAccount, or with status 400 (Bad Request) if the slackAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/slack-accounts")
    @Timed
    public ResponseEntity<SlackAccount> createSlackAccount(@Valid @RequestBody SlackAccount slackAccount) throws URISyntaxException {
        log.debug("REST request to save SlackAccount : {}", slackAccount);
        if (slackAccount.getId() != null) {
            throw new BadRequestAlertException("A new slackAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SlackAccount result = slackAccountService.save(slackAccount);
        return ResponseEntity.created(new URI("/api/slack-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /slack-accounts : Updates an existing slackAccount.
     *
     * @param slackAccount the slackAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated slackAccount,
     * or with status 400 (Bad Request) if the slackAccount is not valid,
     * or with status 500 (Internal Server Error) if the slackAccount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/slack-accounts")
    @Timed
    public ResponseEntity<SlackAccount> updateSlackAccount(@Valid @RequestBody SlackAccount slackAccount) throws URISyntaxException {
        log.debug("REST request to update SlackAccount : {}", slackAccount);
        if (slackAccount.getId() == null) {
            return createSlackAccount(slackAccount);
        }
        SlackAccount result = slackAccountService.save(slackAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, slackAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /slack-accounts : get all the slackAccounts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of slackAccounts in body
     */
    @GetMapping("/slack-accounts")
    @Timed
    public ResponseEntity<List<SlackAccount>> getAllSlackAccounts(SlackAccountCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get SlackAccounts by criteria: {}", criteria);
        Page<SlackAccount> page = slackAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/slack-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /slack-accounts/:id : get the "id" slackAccount.
     *
     * @param id the id of the slackAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the slackAccount, or with status 404 (Not Found)
     */
    @GetMapping("/slack-accounts/{id}")
    @Timed
    public ResponseEntity<SlackAccount> getSlackAccount(@PathVariable Long id) {
        log.debug("REST request to get SlackAccount : {}", id);
        SlackAccount slackAccount = slackAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(slackAccount));
    }

    /**
     * DELETE  /slack-accounts/:id : delete the "id" slackAccount.
     *
     * @param id the id of the slackAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/slack-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteSlackAccount(@PathVariable Long id) {
        log.debug("REST request to delete SlackAccount : {}", id);
        slackAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/slack-accounts?query=:query : search for the slackAccount corresponding
     * to the query.
     *
     * @param query the query of the slackAccount search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/slack-accounts")
    @Timed
    public ResponseEntity<List<SlackAccount>> searchSlackAccounts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of SlackAccounts for query {}", query);
        Page<SlackAccount> page = slackAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/slack-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST  /slack-accounts/:id/send : send a message using the provided slackAccount.
     *
     * @param id the id of the slackAccount
     * @return the ResponseEntity with status 200 (OK) or with status 404 (Not Found)
     */
    @PostMapping("/slack-accounts/{id}/send")
    @Timed
    public ResponseEntity<SlackAccount> sendMessage(@PathVariable Long id, @RequestBody SlackMessage slackMessage) {
        log.debug("REST request to send SlackAccount : {}", id);
        log.debug(slackMessage.toString());
        SlackAccount slackAccount = slackAccountService.findOne(id);
        if (slackAccount == null)
            return ResponseEntity.notFound().build();
        if (slackAccount.isSlackAccountEnabled())
        {
            SlackSession session = SlackSessionFactory.createWebSocketSlackSession(slackAccount.getSlackAccountToken());
            try {
                session.connect();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return ResponseEntity.badRequest().build();
            }
            // Send message to provided channels (public channel, private group or direct message channel)
            if (slackMessage.getChannels().length != 0 && slackMessage.getChannels().length < 10) {
                for (String channel : slackMessage.getChannels()) {
                    session.sendMessage(session.findChannelByName(channel), slackMessage.getMessage());
                }
            }
            else if (slackMessage.getChannels().length > 10) {
                log.error("The message can not be sent to more than ten channels");
                return ResponseEntity.badRequest().build();
            }
            // Send direct message to provided user accounts
            if (slackMessage.getUsers().length != 0 && slackMessage.getUsers().length < 10) {
                for (String user : slackMessage.getUsers()) {
                    session.sendMessageToUser(user, slackMessage.getMessage(), null);
                }
            }
            else if (slackMessage.getUsers().length > 10){
                log.error("The message can not be sent to more than ten users");
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            log.error("Slack account is disabled!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
