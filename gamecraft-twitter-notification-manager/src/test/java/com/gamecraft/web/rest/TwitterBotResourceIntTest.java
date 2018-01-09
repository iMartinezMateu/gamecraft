package com.gamecraft.web.rest;

import com.gamecraft.GamecrafttwitternotificationmanagerApp;

import com.gamecraft.domain.TwitterBot;
import com.gamecraft.repository.TwitterBotRepository;
import com.gamecraft.service.TwitterBotService;
import com.gamecraft.repository.search.TwitterBotSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.TwitterBotCriteria;
import com.gamecraft.service.TwitterBotQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.gamecraft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TwitterBotResource REST controller.
 *
 * @see TwitterBotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecrafttwitternotificationmanagerApp.class)
public class TwitterBotResourceIntTest {

    private static final String DEFAULT_TWITTER_BOT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER_BOT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER_BOT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER_BOT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER_BOT_CONSUMER_KEY = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER_BOT_CONSUMER_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER_BOT_CONSUMER_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER_BOT_CONSUMER_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER_BOT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER_BOT_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER_BOT_ACCESS_TOKEN_SECRET = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TWITTER_BOT_ENABLED = false;
    private static final Boolean UPDATED_TWITTER_BOT_ENABLED = true;

    @Autowired
    private TwitterBotRepository twitterBotRepository;

    @Autowired
    private TwitterBotService twitterBotService;

    @Autowired
    private TwitterBotSearchRepository twitterBotSearchRepository;

    @Autowired
    private TwitterBotQueryService twitterBotQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTwitterBotMockMvc;

    private TwitterBot twitterBot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TwitterBotResource twitterBotResource = new TwitterBotResource(twitterBotService, twitterBotQueryService);
        this.restTwitterBotMockMvc = MockMvcBuilders.standaloneSetup(twitterBotResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TwitterBot createEntity(EntityManager em) {
        TwitterBot twitterBot = new TwitterBot()
            .twitterBotName(DEFAULT_TWITTER_BOT_NAME)
            .twitterBotDescription(DEFAULT_TWITTER_BOT_DESCRIPTION)
            .twitterBotConsumerKey(DEFAULT_TWITTER_BOT_CONSUMER_KEY)
            .twitterBotConsumerSecret(DEFAULT_TWITTER_BOT_CONSUMER_SECRET)
            .twitterBotAccessToken(DEFAULT_TWITTER_BOT_ACCESS_TOKEN)
            .twitterBotAccessTokenSecret(DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET)
            .twitterBotEnabled(DEFAULT_TWITTER_BOT_ENABLED);
        return twitterBot;
    }

    @Before
    public void initTest() {
        twitterBotSearchRepository.deleteAll();
        twitterBot = createEntity(em);
    }

    @Test
    @Transactional
    public void createTwitterBot() throws Exception {
        int databaseSizeBeforeCreate = twitterBotRepository.findAll().size();

        // Create the TwitterBot
        restTwitterBotMockMvc.perform(post("/api/twitter-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterBot)))
            .andExpect(status().isCreated());

        // Validate the TwitterBot in the database
        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeCreate + 1);
        TwitterBot testTwitterBot = twitterBotList.get(twitterBotList.size() - 1);
        assertThat(testTwitterBot.getTwitterBotName()).isEqualTo(DEFAULT_TWITTER_BOT_NAME);
        assertThat(testTwitterBot.getTwitterBotDescription()).isEqualTo(DEFAULT_TWITTER_BOT_DESCRIPTION);
        assertThat(testTwitterBot.getTwitterBotConsumerKey()).isEqualTo(DEFAULT_TWITTER_BOT_CONSUMER_KEY);
        assertThat(testTwitterBot.getTwitterBotConsumerSecret()).isEqualTo(DEFAULT_TWITTER_BOT_CONSUMER_SECRET);
        assertThat(testTwitterBot.getTwitterBotAccessToken()).isEqualTo(DEFAULT_TWITTER_BOT_ACCESS_TOKEN);
        assertThat(testTwitterBot.getTwitterBotAccessTokenSecret()).isEqualTo(DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET);
        assertThat(testTwitterBot.isTwitterBotEnabled()).isEqualTo(DEFAULT_TWITTER_BOT_ENABLED);

        // Validate the TwitterBot in Elasticsearch
        TwitterBot twitterBotEs = twitterBotSearchRepository.findOne(testTwitterBot.getId());
        assertThat(twitterBotEs).isEqualToComparingFieldByField(testTwitterBot);
    }

    @Test
    @Transactional
    public void createTwitterBotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = twitterBotRepository.findAll().size();

        // Create the TwitterBot with an existing ID
        twitterBot.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTwitterBotMockMvc.perform(post("/api/twitter-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterBot)))
            .andExpect(status().isBadRequest());

        // Validate the TwitterBot in the database
        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTwitterBotNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterBotRepository.findAll().size();
        // set the field null
        twitterBot.setTwitterBotName(null);

        // Create the TwitterBot, which fails.

        restTwitterBotMockMvc.perform(post("/api/twitter-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterBot)))
            .andExpect(status().isBadRequest());

        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTwitterBotConsumerKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterBotRepository.findAll().size();
        // set the field null
        twitterBot.setTwitterBotConsumerKey(null);

        // Create the TwitterBot, which fails.

        restTwitterBotMockMvc.perform(post("/api/twitter-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterBot)))
            .andExpect(status().isBadRequest());

        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTwitterBotConsumerSecretIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterBotRepository.findAll().size();
        // set the field null
        twitterBot.setTwitterBotConsumerSecret(null);

        // Create the TwitterBot, which fails.

        restTwitterBotMockMvc.perform(post("/api/twitter-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterBot)))
            .andExpect(status().isBadRequest());

        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTwitterBotAccessTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterBotRepository.findAll().size();
        // set the field null
        twitterBot.setTwitterBotAccessToken(null);

        // Create the TwitterBot, which fails.

        restTwitterBotMockMvc.perform(post("/api/twitter-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterBot)))
            .andExpect(status().isBadRequest());

        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTwitterBotAccessTokenSecretIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterBotRepository.findAll().size();
        // set the field null
        twitterBot.setTwitterBotAccessTokenSecret(null);

        // Create the TwitterBot, which fails.

        restTwitterBotMockMvc.perform(post("/api/twitter-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterBot)))
            .andExpect(status().isBadRequest());

        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTwitterBots() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList
        restTwitterBotMockMvc.perform(get("/api/twitter-bots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].twitterBotName").value(hasItem(DEFAULT_TWITTER_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].twitterBotDescription").value(hasItem(DEFAULT_TWITTER_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].twitterBotConsumerKey").value(hasItem(DEFAULT_TWITTER_BOT_CONSUMER_KEY.toString())))
            .andExpect(jsonPath("$.[*].twitterBotConsumerSecret").value(hasItem(DEFAULT_TWITTER_BOT_CONSUMER_SECRET.toString())))
            .andExpect(jsonPath("$.[*].twitterBotAccessToken").value(hasItem(DEFAULT_TWITTER_BOT_ACCESS_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].twitterBotAccessTokenSecret").value(hasItem(DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET.toString())))
            .andExpect(jsonPath("$.[*].twitterBotEnabled").value(hasItem(DEFAULT_TWITTER_BOT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getTwitterBot() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get the twitterBot
        restTwitterBotMockMvc.perform(get("/api/twitter-bots/{id}", twitterBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(twitterBot.getId().intValue()))
            .andExpect(jsonPath("$.twitterBotName").value(DEFAULT_TWITTER_BOT_NAME.toString()))
            .andExpect(jsonPath("$.twitterBotDescription").value(DEFAULT_TWITTER_BOT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.twitterBotConsumerKey").value(DEFAULT_TWITTER_BOT_CONSUMER_KEY.toString()))
            .andExpect(jsonPath("$.twitterBotConsumerSecret").value(DEFAULT_TWITTER_BOT_CONSUMER_SECRET.toString()))
            .andExpect(jsonPath("$.twitterBotAccessToken").value(DEFAULT_TWITTER_BOT_ACCESS_TOKEN.toString()))
            .andExpect(jsonPath("$.twitterBotAccessTokenSecret").value(DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET.toString()))
            .andExpect(jsonPath("$.twitterBotEnabled").value(DEFAULT_TWITTER_BOT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotNameIsEqualToSomething() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotName equals to DEFAULT_TWITTER_BOT_NAME
        defaultTwitterBotShouldBeFound("twitterBotName.equals=" + DEFAULT_TWITTER_BOT_NAME);

        // Get all the twitterBotList where twitterBotName equals to UPDATED_TWITTER_BOT_NAME
        defaultTwitterBotShouldNotBeFound("twitterBotName.equals=" + UPDATED_TWITTER_BOT_NAME);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotNameIsInShouldWork() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotName in DEFAULT_TWITTER_BOT_NAME or UPDATED_TWITTER_BOT_NAME
        defaultTwitterBotShouldBeFound("twitterBotName.in=" + DEFAULT_TWITTER_BOT_NAME + "," + UPDATED_TWITTER_BOT_NAME);

        // Get all the twitterBotList where twitterBotName equals to UPDATED_TWITTER_BOT_NAME
        defaultTwitterBotShouldNotBeFound("twitterBotName.in=" + UPDATED_TWITTER_BOT_NAME);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotName is not null
        defaultTwitterBotShouldBeFound("twitterBotName.specified=true");

        // Get all the twitterBotList where twitterBotName is null
        defaultTwitterBotShouldNotBeFound("twitterBotName.specified=false");
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotDescription equals to DEFAULT_TWITTER_BOT_DESCRIPTION
        defaultTwitterBotShouldBeFound("twitterBotDescription.equals=" + DEFAULT_TWITTER_BOT_DESCRIPTION);

        // Get all the twitterBotList where twitterBotDescription equals to UPDATED_TWITTER_BOT_DESCRIPTION
        defaultTwitterBotShouldNotBeFound("twitterBotDescription.equals=" + UPDATED_TWITTER_BOT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotDescription in DEFAULT_TWITTER_BOT_DESCRIPTION or UPDATED_TWITTER_BOT_DESCRIPTION
        defaultTwitterBotShouldBeFound("twitterBotDescription.in=" + DEFAULT_TWITTER_BOT_DESCRIPTION + "," + UPDATED_TWITTER_BOT_DESCRIPTION);

        // Get all the twitterBotList where twitterBotDescription equals to UPDATED_TWITTER_BOT_DESCRIPTION
        defaultTwitterBotShouldNotBeFound("twitterBotDescription.in=" + UPDATED_TWITTER_BOT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotDescription is not null
        defaultTwitterBotShouldBeFound("twitterBotDescription.specified=true");

        // Get all the twitterBotList where twitterBotDescription is null
        defaultTwitterBotShouldNotBeFound("twitterBotDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotConsumerKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotConsumerKey equals to DEFAULT_TWITTER_BOT_CONSUMER_KEY
        defaultTwitterBotShouldBeFound("twitterBotConsumerKey.equals=" + DEFAULT_TWITTER_BOT_CONSUMER_KEY);

        // Get all the twitterBotList where twitterBotConsumerKey equals to UPDATED_TWITTER_BOT_CONSUMER_KEY
        defaultTwitterBotShouldNotBeFound("twitterBotConsumerKey.equals=" + UPDATED_TWITTER_BOT_CONSUMER_KEY);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotConsumerKeyIsInShouldWork() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotConsumerKey in DEFAULT_TWITTER_BOT_CONSUMER_KEY or UPDATED_TWITTER_BOT_CONSUMER_KEY
        defaultTwitterBotShouldBeFound("twitterBotConsumerKey.in=" + DEFAULT_TWITTER_BOT_CONSUMER_KEY + "," + UPDATED_TWITTER_BOT_CONSUMER_KEY);

        // Get all the twitterBotList where twitterBotConsumerKey equals to UPDATED_TWITTER_BOT_CONSUMER_KEY
        defaultTwitterBotShouldNotBeFound("twitterBotConsumerKey.in=" + UPDATED_TWITTER_BOT_CONSUMER_KEY);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotConsumerKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotConsumerKey is not null
        defaultTwitterBotShouldBeFound("twitterBotConsumerKey.specified=true");

        // Get all the twitterBotList where twitterBotConsumerKey is null
        defaultTwitterBotShouldNotBeFound("twitterBotConsumerKey.specified=false");
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotConsumerSecretIsEqualToSomething() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotConsumerSecret equals to DEFAULT_TWITTER_BOT_CONSUMER_SECRET
        defaultTwitterBotShouldBeFound("twitterBotConsumerSecret.equals=" + DEFAULT_TWITTER_BOT_CONSUMER_SECRET);

        // Get all the twitterBotList where twitterBotConsumerSecret equals to UPDATED_TWITTER_BOT_CONSUMER_SECRET
        defaultTwitterBotShouldNotBeFound("twitterBotConsumerSecret.equals=" + UPDATED_TWITTER_BOT_CONSUMER_SECRET);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotConsumerSecretIsInShouldWork() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotConsumerSecret in DEFAULT_TWITTER_BOT_CONSUMER_SECRET or UPDATED_TWITTER_BOT_CONSUMER_SECRET
        defaultTwitterBotShouldBeFound("twitterBotConsumerSecret.in=" + DEFAULT_TWITTER_BOT_CONSUMER_SECRET + "," + UPDATED_TWITTER_BOT_CONSUMER_SECRET);

        // Get all the twitterBotList where twitterBotConsumerSecret equals to UPDATED_TWITTER_BOT_CONSUMER_SECRET
        defaultTwitterBotShouldNotBeFound("twitterBotConsumerSecret.in=" + UPDATED_TWITTER_BOT_CONSUMER_SECRET);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotConsumerSecretIsNullOrNotNull() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotConsumerSecret is not null
        defaultTwitterBotShouldBeFound("twitterBotConsumerSecret.specified=true");

        // Get all the twitterBotList where twitterBotConsumerSecret is null
        defaultTwitterBotShouldNotBeFound("twitterBotConsumerSecret.specified=false");
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotAccessTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotAccessToken equals to DEFAULT_TWITTER_BOT_ACCESS_TOKEN
        defaultTwitterBotShouldBeFound("twitterBotAccessToken.equals=" + DEFAULT_TWITTER_BOT_ACCESS_TOKEN);

        // Get all the twitterBotList where twitterBotAccessToken equals to UPDATED_TWITTER_BOT_ACCESS_TOKEN
        defaultTwitterBotShouldNotBeFound("twitterBotAccessToken.equals=" + UPDATED_TWITTER_BOT_ACCESS_TOKEN);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotAccessTokenIsInShouldWork() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotAccessToken in DEFAULT_TWITTER_BOT_ACCESS_TOKEN or UPDATED_TWITTER_BOT_ACCESS_TOKEN
        defaultTwitterBotShouldBeFound("twitterBotAccessToken.in=" + DEFAULT_TWITTER_BOT_ACCESS_TOKEN + "," + UPDATED_TWITTER_BOT_ACCESS_TOKEN);

        // Get all the twitterBotList where twitterBotAccessToken equals to UPDATED_TWITTER_BOT_ACCESS_TOKEN
        defaultTwitterBotShouldNotBeFound("twitterBotAccessToken.in=" + UPDATED_TWITTER_BOT_ACCESS_TOKEN);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotAccessTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotAccessToken is not null
        defaultTwitterBotShouldBeFound("twitterBotAccessToken.specified=true");

        // Get all the twitterBotList where twitterBotAccessToken is null
        defaultTwitterBotShouldNotBeFound("twitterBotAccessToken.specified=false");
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotAccessTokenSecretIsEqualToSomething() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotAccessTokenSecret equals to DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET
        defaultTwitterBotShouldBeFound("twitterBotAccessTokenSecret.equals=" + DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET);

        // Get all the twitterBotList where twitterBotAccessTokenSecret equals to UPDATED_TWITTER_BOT_ACCESS_TOKEN_SECRET
        defaultTwitterBotShouldNotBeFound("twitterBotAccessTokenSecret.equals=" + UPDATED_TWITTER_BOT_ACCESS_TOKEN_SECRET);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotAccessTokenSecretIsInShouldWork() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotAccessTokenSecret in DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET or UPDATED_TWITTER_BOT_ACCESS_TOKEN_SECRET
        defaultTwitterBotShouldBeFound("twitterBotAccessTokenSecret.in=" + DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET + "," + UPDATED_TWITTER_BOT_ACCESS_TOKEN_SECRET);

        // Get all the twitterBotList where twitterBotAccessTokenSecret equals to UPDATED_TWITTER_BOT_ACCESS_TOKEN_SECRET
        defaultTwitterBotShouldNotBeFound("twitterBotAccessTokenSecret.in=" + UPDATED_TWITTER_BOT_ACCESS_TOKEN_SECRET);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotAccessTokenSecretIsNullOrNotNull() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotAccessTokenSecret is not null
        defaultTwitterBotShouldBeFound("twitterBotAccessTokenSecret.specified=true");

        // Get all the twitterBotList where twitterBotAccessTokenSecret is null
        defaultTwitterBotShouldNotBeFound("twitterBotAccessTokenSecret.specified=false");
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotEnabled equals to DEFAULT_TWITTER_BOT_ENABLED
        defaultTwitterBotShouldBeFound("twitterBotEnabled.equals=" + DEFAULT_TWITTER_BOT_ENABLED);

        // Get all the twitterBotList where twitterBotEnabled equals to UPDATED_TWITTER_BOT_ENABLED
        defaultTwitterBotShouldNotBeFound("twitterBotEnabled.equals=" + UPDATED_TWITTER_BOT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotEnabled in DEFAULT_TWITTER_BOT_ENABLED or UPDATED_TWITTER_BOT_ENABLED
        defaultTwitterBotShouldBeFound("twitterBotEnabled.in=" + DEFAULT_TWITTER_BOT_ENABLED + "," + UPDATED_TWITTER_BOT_ENABLED);

        // Get all the twitterBotList where twitterBotEnabled equals to UPDATED_TWITTER_BOT_ENABLED
        defaultTwitterBotShouldNotBeFound("twitterBotEnabled.in=" + UPDATED_TWITTER_BOT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllTwitterBotsByTwitterBotEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        twitterBotRepository.saveAndFlush(twitterBot);

        // Get all the twitterBotList where twitterBotEnabled is not null
        defaultTwitterBotShouldBeFound("twitterBotEnabled.specified=true");

        // Get all the twitterBotList where twitterBotEnabled is null
        defaultTwitterBotShouldNotBeFound("twitterBotEnabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTwitterBotShouldBeFound(String filter) throws Exception {
        restTwitterBotMockMvc.perform(get("/api/twitter-bots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].twitterBotName").value(hasItem(DEFAULT_TWITTER_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].twitterBotDescription").value(hasItem(DEFAULT_TWITTER_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].twitterBotConsumerKey").value(hasItem(DEFAULT_TWITTER_BOT_CONSUMER_KEY.toString())))
            .andExpect(jsonPath("$.[*].twitterBotConsumerSecret").value(hasItem(DEFAULT_TWITTER_BOT_CONSUMER_SECRET.toString())))
            .andExpect(jsonPath("$.[*].twitterBotAccessToken").value(hasItem(DEFAULT_TWITTER_BOT_ACCESS_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].twitterBotAccessTokenSecret").value(hasItem(DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET.toString())))
            .andExpect(jsonPath("$.[*].twitterBotEnabled").value(hasItem(DEFAULT_TWITTER_BOT_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTwitterBotShouldNotBeFound(String filter) throws Exception {
        restTwitterBotMockMvc.perform(get("/api/twitter-bots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTwitterBot() throws Exception {
        // Get the twitterBot
        restTwitterBotMockMvc.perform(get("/api/twitter-bots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTwitterBot() throws Exception {
        // Initialize the database
        twitterBotService.save(twitterBot);

        int databaseSizeBeforeUpdate = twitterBotRepository.findAll().size();

        // Update the twitterBot
        TwitterBot updatedTwitterBot = twitterBotRepository.findOne(twitterBot.getId());
        updatedTwitterBot
            .twitterBotName(UPDATED_TWITTER_BOT_NAME)
            .twitterBotDescription(UPDATED_TWITTER_BOT_DESCRIPTION)
            .twitterBotConsumerKey(UPDATED_TWITTER_BOT_CONSUMER_KEY)
            .twitterBotConsumerSecret(UPDATED_TWITTER_BOT_CONSUMER_SECRET)
            .twitterBotAccessToken(UPDATED_TWITTER_BOT_ACCESS_TOKEN)
            .twitterBotAccessTokenSecret(UPDATED_TWITTER_BOT_ACCESS_TOKEN_SECRET)
            .twitterBotEnabled(UPDATED_TWITTER_BOT_ENABLED);

        restTwitterBotMockMvc.perform(put("/api/twitter-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTwitterBot)))
            .andExpect(status().isOk());

        // Validate the TwitterBot in the database
        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeUpdate);
        TwitterBot testTwitterBot = twitterBotList.get(twitterBotList.size() - 1);
        assertThat(testTwitterBot.getTwitterBotName()).isEqualTo(UPDATED_TWITTER_BOT_NAME);
        assertThat(testTwitterBot.getTwitterBotDescription()).isEqualTo(UPDATED_TWITTER_BOT_DESCRIPTION);
        assertThat(testTwitterBot.getTwitterBotConsumerKey()).isEqualTo(UPDATED_TWITTER_BOT_CONSUMER_KEY);
        assertThat(testTwitterBot.getTwitterBotConsumerSecret()).isEqualTo(UPDATED_TWITTER_BOT_CONSUMER_SECRET);
        assertThat(testTwitterBot.getTwitterBotAccessToken()).isEqualTo(UPDATED_TWITTER_BOT_ACCESS_TOKEN);
        assertThat(testTwitterBot.getTwitterBotAccessTokenSecret()).isEqualTo(UPDATED_TWITTER_BOT_ACCESS_TOKEN_SECRET);
        assertThat(testTwitterBot.isTwitterBotEnabled()).isEqualTo(UPDATED_TWITTER_BOT_ENABLED);

        // Validate the TwitterBot in Elasticsearch
        TwitterBot twitterBotEs = twitterBotSearchRepository.findOne(testTwitterBot.getId());
        assertThat(twitterBotEs).isEqualToComparingFieldByField(testTwitterBot);
    }

    @Test
    @Transactional
    public void updateNonExistingTwitterBot() throws Exception {
        int databaseSizeBeforeUpdate = twitterBotRepository.findAll().size();

        // Create the TwitterBot

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTwitterBotMockMvc.perform(put("/api/twitter-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterBot)))
            .andExpect(status().isCreated());

        // Validate the TwitterBot in the database
        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTwitterBot() throws Exception {
        // Initialize the database
        twitterBotService.save(twitterBot);

        int databaseSizeBeforeDelete = twitterBotRepository.findAll().size();

        // Get the twitterBot
        restTwitterBotMockMvc.perform(delete("/api/twitter-bots/{id}", twitterBot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean twitterBotExistsInEs = twitterBotSearchRepository.exists(twitterBot.getId());
        assertThat(twitterBotExistsInEs).isFalse();

        // Validate the database is empty
        List<TwitterBot> twitterBotList = twitterBotRepository.findAll();
        assertThat(twitterBotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTwitterBot() throws Exception {
        // Initialize the database
        twitterBotService.save(twitterBot);

        // Search the twitterBot
        restTwitterBotMockMvc.perform(get("/api/_search/twitter-bots?query=id:" + twitterBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].twitterBotName").value(hasItem(DEFAULT_TWITTER_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].twitterBotDescription").value(hasItem(DEFAULT_TWITTER_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].twitterBotConsumerKey").value(hasItem(DEFAULT_TWITTER_BOT_CONSUMER_KEY.toString())))
            .andExpect(jsonPath("$.[*].twitterBotConsumerSecret").value(hasItem(DEFAULT_TWITTER_BOT_CONSUMER_SECRET.toString())))
            .andExpect(jsonPath("$.[*].twitterBotAccessToken").value(hasItem(DEFAULT_TWITTER_BOT_ACCESS_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].twitterBotAccessTokenSecret").value(hasItem(DEFAULT_TWITTER_BOT_ACCESS_TOKEN_SECRET.toString())))
            .andExpect(jsonPath("$.[*].twitterBotEnabled").value(hasItem(DEFAULT_TWITTER_BOT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TwitterBot.class);
        TwitterBot twitterBot1 = new TwitterBot();
        twitterBot1.setId(1L);
        TwitterBot twitterBot2 = new TwitterBot();
        twitterBot2.setId(twitterBot1.getId());
        assertThat(twitterBot1).isEqualTo(twitterBot2);
        twitterBot2.setId(2L);
        assertThat(twitterBot1).isNotEqualTo(twitterBot2);
        twitterBot1.setId(null);
        assertThat(twitterBot1).isNotEqualTo(twitterBot2);
    }
}
