package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.TelegramBot;
import com.gamecraft.service.TelegramBotService;
import com.gamecraft.service.dto.TelegramMessage;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.TelegramBotCriteria;
import com.gamecraft.service.TelegramBotQueryService;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
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
 * REST controller for managing TelegramBot.
 */
@RestController
@RequestMapping("/api")
public class TelegramBotResource {

    private final Logger log = LoggerFactory.getLogger(TelegramBotResource.class);

    private static final String ENTITY_NAME = "telegramBot";

    private final TelegramBotService telegramBotService;

    private final TelegramBotQueryService telegramBotQueryService;

    public TelegramBotResource(TelegramBotService telegramBotService, TelegramBotQueryService telegramBotQueryService) {
        this.telegramBotService = telegramBotService;
        this.telegramBotQueryService = telegramBotQueryService;
    }

    /**
     * POST  /telegram-bots : Create a new telegramBot.
     *
     * @param telegramBot the telegramBot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new telegramBot, or with status 400 (Bad Request) if the telegramBot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/telegram-bots")
    @Timed
    public ResponseEntity<TelegramBot> createTelegramBot(@Valid @RequestBody TelegramBot telegramBot) throws URISyntaxException {
        log.debug("REST request to save TelegramBot : {}", telegramBot);
        if (telegramBot.getId() != null) {
            throw new BadRequestAlertException("A new telegramBot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TelegramBot result = telegramBotService.save(telegramBot);
        return ResponseEntity.created(new URI("/api/telegram-bots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /telegram-bots : Updates an existing telegramBot.
     *
     * @param telegramBot the telegramBot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated telegramBot,
     * or with status 400 (Bad Request) if the telegramBot is not valid,
     * or with status 500 (Internal Server Error) if the telegramBot couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/telegram-bots")
    @Timed
    public ResponseEntity<TelegramBot> updateTelegramBot(@Valid @RequestBody TelegramBot telegramBot) throws URISyntaxException {
        log.debug("REST request to update TelegramBot : {}", telegramBot);
        if (telegramBot.getId() == null) {
            return createTelegramBot(telegramBot);
        }
        TelegramBot result = telegramBotService.save(telegramBot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telegramBot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /telegram-bots : get all the telegramBots.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of telegramBots in body
     */
    @GetMapping("/telegram-bots")
    @Timed
    public ResponseEntity<List<TelegramBot>> getAllTelegramBots(TelegramBotCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get TelegramBots by criteria: {}", criteria);
        Page<TelegramBot> page = telegramBotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/telegram-bots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /telegram-bots/:id : get the "id" telegramBot.
     *
     * @param id the id of the telegramBot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the telegramBot, or with status 404 (Not Found)
     */
    @GetMapping("/telegram-bots/{id}")
    @Timed
    public ResponseEntity<TelegramBot> getTelegramBot(@PathVariable Long id) {
        log.debug("REST request to get TelegramBot : {}", id);
        TelegramBot telegramBot = telegramBotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(telegramBot));
    }

    /**
     * DELETE  /telegram-bots/:id : delete the "id" telegramBot.
     *
     * @param id the id of the telegramBot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/telegram-bots/{id}")
    @Timed
    public ResponseEntity<Void> deleteTelegramBot(@PathVariable Long id) {
        log.debug("REST request to delete TelegramBot : {}", id);
        telegramBotService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/telegram-bots?query=:query : search for the telegramBot corresponding
     * to the query.
     *
     * @param query the query of the telegramBot search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/telegram-bots")
    @Timed
    public ResponseEntity<List<TelegramBot>> searchTelegramBots(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TelegramBots for query {}", query);
        Page<TelegramBot> page = telegramBotService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/telegram-bots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST  /telegram-bots/:id/send : send a message using the provided telegramBot.
     *
     * @param id the id of the telegramBot
     * @return the ResponseEntity with status 200 (OK) or with status 404 (Not Found)
     */
    @PostMapping("/telegram-bots/{id}/send")
    @Timed
    public ResponseEntity<TelegramBot> sendMessage(@PathVariable Long id, @RequestBody TelegramMessage telegramMessage)  {
        log.debug("REST request to send TelegramBot : {}", id);
        log.debug(telegramMessage.toString());
        TelegramBot telegramBot = telegramBotService.findOne(id);
        if (telegramBot == null)
            return ResponseEntity.notFound().build();
        if (telegramBot.isTelegramBotEnabled())
        {
            com.pengrad.telegrambot.TelegramBot bot = new com.pengrad.telegrambot.TelegramBot(telegramBot.getTelegramBotToken());
            SendMessage request = new SendMessage(telegramMessage.getChatId(), telegramMessage.getMessage())
                .parseMode(ParseMode.Markdown)
                .disableWebPagePreview(telegramMessage.isWebPagePreviewDisabled())
                .disableNotification(telegramMessage.isNotificationDisabled());

            SendResponse sendResponse = bot.execute(request);
            boolean ok = sendResponse.isOk();
            if (ok == false) {
                log.error("Error code is " + sendResponse.errorCode());
                log.debug(request.getParameters().toString());
                log.debug(sendResponse.description());
                log.debug(sendResponse.toString());
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            log.error("Telegram bot is disabled!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


}
