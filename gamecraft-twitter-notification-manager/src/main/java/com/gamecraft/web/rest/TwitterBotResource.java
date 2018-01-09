package com.gamecraft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gamecraft.domain.TwitterBot;
import com.gamecraft.service.TwitterBotService;
import com.gamecraft.service.dto.DirectMessage;
import com.gamecraft.service.dto.Tweet;
import com.gamecraft.web.rest.errors.BadRequestAlertException;
import com.gamecraft.web.rest.util.HeaderUtil;
import com.gamecraft.web.rest.util.PaginationUtil;
import com.gamecraft.service.dto.TwitterBotCriteria;
import com.gamecraft.service.TwitterBotQueryService;
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
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TwitterBot.
 */
@RestController
@RequestMapping("/api")
public class TwitterBotResource {

    private final Logger log = LoggerFactory.getLogger(TwitterBotResource.class);

    private static final String ENTITY_NAME = "twitterBot";

    private final TwitterBotService twitterBotService;

    private final TwitterBotQueryService twitterBotQueryService;

    public TwitterBotResource(TwitterBotService twitterBotService, TwitterBotQueryService twitterBotQueryService) {
        this.twitterBotService = twitterBotService;
        this.twitterBotQueryService = twitterBotQueryService;
    }

    /**
     * POST  /twitter-bots : Create a new twitterBot.
     *
     * @param twitterBot the twitterBot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new twitterBot, or with status 400 (Bad Request) if the twitterBot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/twitter-bots")
    @Timed
    public ResponseEntity<TwitterBot> createTwitterBot(@Valid @RequestBody TwitterBot twitterBot) throws URISyntaxException {
        log.debug("REST request to save TwitterBot : {}", twitterBot);
        if (twitterBot.getId() != null) {
            throw new BadRequestAlertException("A new twitterBot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TwitterBot result = twitterBotService.save(twitterBot);
        return ResponseEntity.created(new URI("/api/twitter-bots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /twitter-bots : Updates an existing twitterBot.
     *
     * @param twitterBot the twitterBot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated twitterBot,
     * or with status 400 (Bad Request) if the twitterBot is not valid,
     * or with status 500 (Internal Server Error) if the twitterBot couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/twitter-bots")
    @Timed
    public ResponseEntity<TwitterBot> updateTwitterBot(@Valid @RequestBody TwitterBot twitterBot) throws URISyntaxException {
        log.debug("REST request to update TwitterBot : {}", twitterBot);
        if (twitterBot.getId() == null) {
            return createTwitterBot(twitterBot);
        }
        TwitterBot result = twitterBotService.save(twitterBot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, twitterBot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /twitter-bots : get all the twitterBots.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of twitterBots in body
     */
    @GetMapping("/twitter-bots")
    @Timed
    public ResponseEntity<List<TwitterBot>> getAllTwitterBots(TwitterBotCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get TwitterBots by criteria: {}", criteria);
        Page<TwitterBot> page = twitterBotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/twitter-bots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /twitter-bots/:id : get the "id" twitterBot.
     *
     * @param id the id of the twitterBot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the twitterBot, or with status 404 (Not Found)
     */
    @GetMapping("/twitter-bots/{id}")
    @Timed
    public ResponseEntity<TwitterBot> getTwitterBot(@PathVariable Long id) {
        log.debug("REST request to get TwitterBot : {}", id);
        TwitterBot twitterBot = twitterBotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(twitterBot));
    }

    /**
     * DELETE  /twitter-bots/:id : delete the "id" twitterBot.
     *
     * @param id the id of the twitterBot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/twitter-bots/{id}")
    @Timed
    public ResponseEntity<Void> deleteTwitterBot(@PathVariable Long id) {
        log.debug("REST request to delete TwitterBot : {}", id);
        twitterBotService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/twitter-bots?query=:query : search for the twitterBot corresponding
     * to the query.
     *
     * @param query the query of the twitterBot search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/twitter-bots")
    @Timed
    public ResponseEntity<List<TwitterBot>> searchTwitterBots(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TwitterBots for query {}", query);
        Page<TwitterBot> page = twitterBotService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/twitter-bots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * POST  /twitter-bots/:id/send : send a tweet using the provided twitterBot.
     *
     * @param id the id of the twitterBot
     * @return the ResponseEntity with status 200 (OK) or with status 404 (Not Found)
     */
    @PostMapping("/twitter-bots/{id}/send")
    @Timed
    public ResponseEntity<TwitterBot> sendMessage(@PathVariable Long id, @RequestBody Tweet tweet)  {
        log.debug("REST request to send TwitterBot : {}", id);
        TwitterBot twitterBot = twitterBotService.findOne(id);
        if (twitterBot == null)
            return ResponseEntity.notFound().build();
        if (twitterBot.isTwitterBotEnabled())
        {
           ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterBot.getTwitterBotConsumerKey())
                .setOAuthConsumerSecret(twitterBot.getTwitterBotConsumerSecret())
                .setOAuthAccessToken(twitterBot.getTwitterBotAccessToken())
                .setOAuthAccessTokenSecret(twitterBot.getTwitterBotAccessTokenSecret());
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            try {
                twitter.updateStatus(tweet.getMessage());
            } catch (TwitterException e) {
                e.printStackTrace();
                log.error(e.getErrorMessage());
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            log.error("Telegram bot is disabled!");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * POST  /twitter-bots/:id/send : send a direct message using the provided twitterBot.
     *
     * @param id the id of the twitterBot
     * @return the ResponseEntity with status 200 (OK) or with status 404 (Not Found)
     */
    @PostMapping("/twitter-bots/{id}/send")
    @Timed
    public ResponseEntity<TwitterBot> sendMessage(@PathVariable Long id, @RequestBody DirectMessage directMessage)  {
        log.debug("REST request to send TwitterBot : {}", id);
        TwitterBot twitterBot = twitterBotService.findOne(id);
        if (twitterBot == null)
            return ResponseEntity.notFound().build();
        if (twitterBot.isTwitterBotEnabled())
        {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterBot.getTwitterBotConsumerKey())
                .setOAuthConsumerSecret(twitterBot.getTwitterBotConsumerSecret())
                .setOAuthAccessToken(twitterBot.getTwitterBotAccessToken())
                .setOAuthAccessTokenSecret(twitterBot.getTwitterBotAccessTokenSecret());
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            try {
                twitter.sendDirectMessage(directMessage.getReceiver(),directMessage.getMessage());
            } catch (TwitterException e) {
                log.error("Telegram bot is disabled!");
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
