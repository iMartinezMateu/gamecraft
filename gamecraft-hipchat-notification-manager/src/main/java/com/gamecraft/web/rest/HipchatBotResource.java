package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.HipchatBot;
import com.gamecraft.service.HipchatBotService;
import com.gamecraft.service.dto.HipchatMessage;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.HipchatBotCriteria;
import com.gamecraft.service.HipchatBotQueryService;
import io.evanwong.oss.hipchat.v2.HipChatClient;
import io.evanwong.oss.hipchat.v2.commons.NoContent;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import io.evanwong.oss.hipchat.v2.rooms.SendRoomNotificationRequestBuilder;
import io.evanwong.oss.hipchat.v2.users.PrivateMessageUserRequestBuilder;
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
import java.util.concurrent.Future;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing HipchatBot.
 */
@RestController
@RequestMapping("/api")
public class HipchatBotResource {

    private final Logger log = LoggerFactory.getLogger(HipchatBotResource.class);

    private static final String ENTITY_NAME = "hipchatBot";

    private final HipchatBotService hipchatBotService;

    private final HipchatBotQueryService hipchatBotQueryService;

    public HipchatBotResource(HipchatBotService hipchatBotService, HipchatBotQueryService hipchatBotQueryService) {
        this.hipchatBotService = hipchatBotService;
        this.hipchatBotQueryService = hipchatBotQueryService;
    }

    /**
     * POST  /hipchat-bots : Create a new hipchatBot.
     *
     * @param hipchatBot the hipchatBot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hipchatBot, or with status 400 (Bad Request) if the hipchatBot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hipchat-bots")
    @Timed
    public ResponseEntity<HipchatBot> createHipchatBot(@Valid @RequestBody HipchatBot hipchatBot) throws URISyntaxException {
        log.debug("REST request to save HipchatBot : {}", hipchatBot);
        if (hipchatBot.getId() != null) {
            throw new BadRequestAlertException("A new hipchatBot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HipchatBot result = hipchatBotService.save(hipchatBot);
        return ResponseEntity.created(new URI("/api/hipchat-bots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hipchat-bots : Updates an existing hipchatBot.
     *
     * @param hipchatBot the hipchatBot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hipchatBot,
     * or with status 400 (Bad Request) if the hipchatBot is not valid,
     * or with status 500 (Internal Server Error) if the hipchatBot couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hipchat-bots")
    @Timed
    public ResponseEntity<HipchatBot> updateHipchatBot(@Valid @RequestBody HipchatBot hipchatBot) throws URISyntaxException {
        log.debug("REST request to update HipchatBot : {}", hipchatBot);
        if (hipchatBot.getId() == null) {
            return createHipchatBot(hipchatBot);
        }
        HipchatBot result = hipchatBotService.save(hipchatBot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hipchatBot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hipchat-bots : get all the hipchatBots.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of hipchatBots in body
     */
    @GetMapping("/hipchat-bots")
    @Timed
    public ResponseEntity<List<HipchatBot>> getAllHipchatBots(HipchatBotCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get HipchatBots by criteria: {}", criteria);
        Page<HipchatBot> page = hipchatBotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hipchat-bots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hipchat-bots/:id : get the "id" hipchatBot.
     *
     * @param id the id of the hipchatBot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hipchatBot, or with status 404 (Not Found)
     */
    @GetMapping("/hipchat-bots/{id}")
    @Timed
    public ResponseEntity<HipchatBot> getHipchatBot(@PathVariable Long id) {
        log.debug("REST request to get HipchatBot : {}", id);
        HipchatBot hipchatBot = hipchatBotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hipchatBot));
    }

    /**
     * DELETE  /hipchat-bots/:id : delete the "id" hipchatBot.
     *
     * @param id the id of the hipchatBot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hipchat-bots/{id}")
    @Timed
    public ResponseEntity<Void> deleteHipchatBot(@PathVariable Long id) {
        log.debug("REST request to delete HipchatBot : {}", id);
        hipchatBotService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/hipchat-bots?query=:query : search for the hipchatBot corresponding
     * to the query.
     *
     * @param query the query of the hipchatBot search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/hipchat-bots")
    @Timed
    public ResponseEntity<List<HipchatBot>> searchHipchatBots(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of HipchatBots for query {}", query);
        Page<HipchatBot> page = hipchatBotService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/hipchat-bots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST  /hipchat-bots/:id/send : send a message using the provided hipchatBot.
     *
     * @param id the id of the hipchatBot
     * @return the ResponseEntity with status 200 (OK) or with status 404 (Not Found)
     */
    @PostMapping("/hipchat-bots/{id}/send")
    @Timed
    public ResponseEntity<HipchatBot> sendMessage(@PathVariable Long id, @RequestBody HipchatMessage hipchatMessage) throws IOException {
        log.debug("REST request to send HipchatBot : {}", id);
        HipchatBot hipchatBot = hipchatBotService.findOne(id);
        if (hipchatBot == null)
            return ResponseEntity.notFound().build();
        if (hipchatBot.isHipchatBotEnabled())
        {
            HipChatClient client = new HipChatClient(hipchatBot.getHipchatBotToken());
            // Send message to provided channels or rooms
            if (hipchatMessage.getChannels().length != 0 && hipchatMessage.getChannels().length < 10) {
                for (String channel : hipchatMessage.getChannels()) {
                    SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder(channel, hipchatMessage.getMessage());
                    builder.setNotify(true).build().execute();
                }
            }
            else if (hipchatMessage.getChannels().length > 10) {
                log.error("The message can not be sent to more than ten channels");
                client.close();
                return ResponseEntity.badRequest().build();
            }
            // Send direct message to provided user accounts (e-mails)
            if (hipchatMessage.getUsers().length != 0 && hipchatMessage.getUsers().length < 10) {
                for (String user : hipchatMessage.getUsers()) {
                    PrivateMessageUserRequestBuilder builder = client.preparePrivateMessageUserRequestBuilder(user, hipchatMessage.getMessage());
                    builder.setNotify(true).build().execute();
                }
            }
            else if (hipchatMessage.getUsers().length > 10){
                log.error("The message can not be sent to more than ten users");
                client.close();
                return ResponseEntity.badRequest().build();
            }
            client.close();
        }
        else {
            log.error("Hipchat Bot is disabled!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
