package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.IrcBot;
import com.gamecraft.service.IrcBotService;
import com.gamecraft.service.dto.IrcMessage;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.IrcBotCriteria;
import com.gamecraft.service.IrcBotQueryService;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.kitteh.irc.client.library.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

import static com.gamecraft.web.rest.util.IrcBotListener.sendString;

/**
 * REST controller for managing IrcBot.
 */
@RestController
@RequestMapping("/api")
public class IrcBotResource {

    private final Logger log = LoggerFactory.getLogger(IrcBotResource.class);

    private static final String ENTITY_NAME = "ircBot";

    private final IrcBotService ircBotService;

    private final IrcBotQueryService ircBotQueryService;

    public IrcBotResource(IrcBotService ircBotService, IrcBotQueryService ircBotQueryService) {
        this.ircBotService = ircBotService;
        this.ircBotQueryService = ircBotQueryService;
    }

    /**
     * POST  /irc-bots : Create a new ircBot.
     *
     * @param ircBot the ircBot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ircBot, or with status 400 (Bad Request) if the ircBot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/irc-bots")
    @Timed
    public ResponseEntity<IrcBot> createIrcBot(@Valid @RequestBody IrcBot ircBot) throws URISyntaxException {
        log.debug("REST request to save IrcBot : {}", ircBot);
        if (ircBot.getId() != null) {
            throw new BadRequestAlertException("A new ircBot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IrcBot result = ircBotService.save(ircBot);
        return ResponseEntity.created(new URI("/api/irc-bots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /irc-bots : Updates an existing ircBot.
     *
     * @param ircBot the ircBot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ircBot,
     * or with status 400 (Bad Request) if the ircBot is not valid,
     * or with status 500 (Internal Server Error) if the ircBot couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/irc-bots")
    @Timed
    public ResponseEntity<IrcBot> updateIrcBot(@Valid @RequestBody IrcBot ircBot) throws URISyntaxException {
        log.debug("REST request to update IrcBot : {}", ircBot);
        if (ircBot.getId() == null) {
            return createIrcBot(ircBot);
        }
        IrcBot result = ircBotService.save(ircBot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ircBot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /irc-bots : get all the ircBots.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of ircBots in body
     */
    @GetMapping("/irc-bots")
    @Timed
    public ResponseEntity<List<IrcBot>> getAllIrcBots(IrcBotCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get IrcBots by criteria: {}", criteria);
        Page<IrcBot> page = ircBotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/irc-bots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /irc-bots/:id : get the "id" ircBot.
     *
     * @param id the id of the ircBot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ircBot, or with status 404 (Not Found)
     */
    @GetMapping("/irc-bots/{id}")
    @Timed
    public ResponseEntity<IrcBot> getIrcBot(@PathVariable Long id) {
        log.debug("REST request to get IrcBot : {}", id);
        IrcBot ircBot = ircBotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ircBot));
    }

    /**
     * DELETE  /irc-bots/:id : delete the "id" ircBot.
     *
     * @param id the id of the ircBot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/irc-bots/{id}")
    @Timed
    public ResponseEntity<Void> deleteIrcBot(@PathVariable Long id) {
        log.debug("REST request to delete IrcBot : {}", id);
        ircBotService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/irc-bots?query=:query : search for the ircBot corresponding
     * to the query.
     *
     * @param query the query of the ircBot search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/irc-bots")
    @Timed
    public ResponseEntity<List<IrcBot>> searchIrcBots(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of IrcBots for query {}", query);
        Page<IrcBot> page = ircBotService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/irc-bots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST  /irc-bots/:id/send : send a message using the provided ircBot.
     *
     * @param id the id of the ircBot
     * @return the ResponseEntity with status 200 (OK) or with status 404 (Not Found)
     */
    @PostMapping("/irc-bots/{id}/send")
    @Timed
    public ResponseEntity<IrcBot> sendMessage(@PathVariable Long id, @RequestBody IrcMessage ircMessage)  {
        log.debug("REST request to send IrcBot : {}", id);
        log.debug(ircMessage.toString());
        IrcBot ircBot = ircBotService.findOne(id);
        if (ircBot == null)
            return ResponseEntity.notFound().build();
        if (ircBot.isIrcBotEnabled())
        {
            try {
                //Configure what we want our bot to do
                if (ircMessage.getIrcChannel().charAt(0) != '#') {
                    ircMessage.setIrcChannel('#' + ircMessage.getIrcChannel());
                }
                Client client = Client.builder().nick(ircBot.getIrcBotNickname()).serverHost(ircBot.getIrcServerAddress()).serverPort(ircBot.getIrcServerPort()).secure(false).buildAndConnect();

                client.addChannel(ircMessage.getIrcChannel());
                client.sendMessage(ircMessage.getIrcChannel(), ircMessage.getMessage());
                client.sendRawLine("QUIT Bye.");
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            log.error("IRC bot is disabled!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
