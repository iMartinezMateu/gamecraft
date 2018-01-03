package com.gamecraft.web.rest;

import com.gamecraft.GamecrafttelegramnotificationmanagerApp;

import com.gamecraft.domain.TelegramBot;
import com.gamecraft.repository.TelegramBotRepository;
import com.gamecraft.service.TelegramBotService;
import com.gamecraft.repository.search.TelegramBotSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.TelegramBotCriteria;
import com.gamecraft.service.TelegramBotQueryService;

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
 * Test class for the TelegramBotResource REST controller.
 *
 * @see TelegramBotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecrafttelegramnotificationmanagerApp.class)
public class TelegramBotResourceIntTest {

    private static final String DEFAULT_TELEGRAM_BOT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TELEGRAM_BOT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TELEGRAM_BOT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TELEGRAM_BOT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEGRAM_BOT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TELEGRAM_BOT_TOKEN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TELEGRAM_BOT_ENABLED = false;
    private static final Boolean UPDATED_TELEGRAM_BOT_ENABLED = true;

    @Autowired
    private TelegramBotRepository telegramBotRepository;

    @Autowired
    private TelegramBotService telegramBotService;

    @Autowired
    private TelegramBotSearchRepository telegramBotSearchRepository;

    @Autowired
    private TelegramBotQueryService telegramBotQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTelegramBotMockMvc;

    private TelegramBot telegramBot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TelegramBotResource telegramBotResource = new TelegramBotResource(telegramBotService, telegramBotQueryService);
        this.restTelegramBotMockMvc = MockMvcBuilders.standaloneSetup(telegramBotResource)
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
    public static TelegramBot createEntity(EntityManager em) {
        TelegramBot telegramBot = new TelegramBot()
            .telegramBotName(DEFAULT_TELEGRAM_BOT_NAME)
            .telegramBotDescription(DEFAULT_TELEGRAM_BOT_DESCRIPTION)
            .telegramBotToken(DEFAULT_TELEGRAM_BOT_TOKEN)
            .telegramBotEnabled(DEFAULT_TELEGRAM_BOT_ENABLED);
        return telegramBot;
    }

    @Before
    public void initTest() {
        telegramBotSearchRepository.deleteAll();
        telegramBot = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelegramBot() throws Exception {
        int databaseSizeBeforeCreate = telegramBotRepository.findAll().size();

        // Create the TelegramBot
        restTelegramBotMockMvc.perform(post("/api/telegram-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telegramBot)))
            .andExpect(status().isCreated());

        // Validate the TelegramBot in the database
        List<TelegramBot> telegramBotList = telegramBotRepository.findAll();
        assertThat(telegramBotList).hasSize(databaseSizeBeforeCreate + 1);
        TelegramBot testTelegramBot = telegramBotList.get(telegramBotList.size() - 1);
        assertThat(testTelegramBot.getTelegramBotName()).isEqualTo(DEFAULT_TELEGRAM_BOT_NAME);
        assertThat(testTelegramBot.getTelegramBotDescription()).isEqualTo(DEFAULT_TELEGRAM_BOT_DESCRIPTION);
        assertThat(testTelegramBot.getTelegramBotToken()).isEqualTo(DEFAULT_TELEGRAM_BOT_TOKEN);
        assertThat(testTelegramBot.isTelegramBotEnabled()).isEqualTo(DEFAULT_TELEGRAM_BOT_ENABLED);

        // Validate the TelegramBot in Elasticsearch
        TelegramBot telegramBotEs = telegramBotSearchRepository.findOne(testTelegramBot.getId());
        assertThat(telegramBotEs).isEqualToComparingFieldByField(testTelegramBot);
    }

    @Test
    @Transactional
    public void createTelegramBotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telegramBotRepository.findAll().size();

        // Create the TelegramBot with an existing ID
        telegramBot.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelegramBotMockMvc.perform(post("/api/telegram-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telegramBot)))
            .andExpect(status().isBadRequest());

        // Validate the TelegramBot in the database
        List<TelegramBot> telegramBotList = telegramBotRepository.findAll();
        assertThat(telegramBotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTelegramBotNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = telegramBotRepository.findAll().size();
        // set the field null
        telegramBot.setTelegramBotName(null);

        // Create the TelegramBot, which fails.

        restTelegramBotMockMvc.perform(post("/api/telegram-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telegramBot)))
            .andExpect(status().isBadRequest());

        List<TelegramBot> telegramBotList = telegramBotRepository.findAll();
        assertThat(telegramBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelegramBotTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = telegramBotRepository.findAll().size();
        // set the field null
        telegramBot.setTelegramBotToken(null);

        // Create the TelegramBot, which fails.

        restTelegramBotMockMvc.perform(post("/api/telegram-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telegramBot)))
            .andExpect(status().isBadRequest());

        List<TelegramBot> telegramBotList = telegramBotRepository.findAll();
        assertThat(telegramBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelegramBots() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList
        restTelegramBotMockMvc.perform(get("/api/telegram-bots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].telegramBotName").value(hasItem(DEFAULT_TELEGRAM_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].telegramBotDescription").value(hasItem(DEFAULT_TELEGRAM_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].telegramBotToken").value(hasItem(DEFAULT_TELEGRAM_BOT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].telegramBotEnabled").value(hasItem(DEFAULT_TELEGRAM_BOT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getTelegramBot() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get the telegramBot
        restTelegramBotMockMvc.perform(get("/api/telegram-bots/{id}", telegramBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(telegramBot.getId().intValue()))
            .andExpect(jsonPath("$.telegramBotName").value(DEFAULT_TELEGRAM_BOT_NAME.toString()))
            .andExpect(jsonPath("$.telegramBotDescription").value(DEFAULT_TELEGRAM_BOT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.telegramBotToken").value(DEFAULT_TELEGRAM_BOT_TOKEN.toString()))
            .andExpect(jsonPath("$.telegramBotEnabled").value(DEFAULT_TELEGRAM_BOT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotNameIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotName equals to DEFAULT_TELEGRAM_BOT_NAME
        defaultTelegramBotShouldBeFound("telegramBotName.equals=" + DEFAULT_TELEGRAM_BOT_NAME);

        // Get all the telegramBotList where telegramBotName equals to UPDATED_TELEGRAM_BOT_NAME
        defaultTelegramBotShouldNotBeFound("telegramBotName.equals=" + UPDATED_TELEGRAM_BOT_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotNameIsInShouldWork() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotName in DEFAULT_TELEGRAM_BOT_NAME or UPDATED_TELEGRAM_BOT_NAME
        defaultTelegramBotShouldBeFound("telegramBotName.in=" + DEFAULT_TELEGRAM_BOT_NAME + "," + UPDATED_TELEGRAM_BOT_NAME);

        // Get all the telegramBotList where telegramBotName equals to UPDATED_TELEGRAM_BOT_NAME
        defaultTelegramBotShouldNotBeFound("telegramBotName.in=" + UPDATED_TELEGRAM_BOT_NAME);
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotName is not null
        defaultTelegramBotShouldBeFound("telegramBotName.specified=true");

        // Get all the telegramBotList where telegramBotName is null
        defaultTelegramBotShouldNotBeFound("telegramBotName.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotDescription equals to DEFAULT_TELEGRAM_BOT_DESCRIPTION
        defaultTelegramBotShouldBeFound("telegramBotDescription.equals=" + DEFAULT_TELEGRAM_BOT_DESCRIPTION);

        // Get all the telegramBotList where telegramBotDescription equals to UPDATED_TELEGRAM_BOT_DESCRIPTION
        defaultTelegramBotShouldNotBeFound("telegramBotDescription.equals=" + UPDATED_TELEGRAM_BOT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotDescription in DEFAULT_TELEGRAM_BOT_DESCRIPTION or UPDATED_TELEGRAM_BOT_DESCRIPTION
        defaultTelegramBotShouldBeFound("telegramBotDescription.in=" + DEFAULT_TELEGRAM_BOT_DESCRIPTION + "," + UPDATED_TELEGRAM_BOT_DESCRIPTION);

        // Get all the telegramBotList where telegramBotDescription equals to UPDATED_TELEGRAM_BOT_DESCRIPTION
        defaultTelegramBotShouldNotBeFound("telegramBotDescription.in=" + UPDATED_TELEGRAM_BOT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotDescription is not null
        defaultTelegramBotShouldBeFound("telegramBotDescription.specified=true");

        // Get all the telegramBotList where telegramBotDescription is null
        defaultTelegramBotShouldNotBeFound("telegramBotDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotToken equals to DEFAULT_TELEGRAM_BOT_TOKEN
        defaultTelegramBotShouldBeFound("telegramBotToken.equals=" + DEFAULT_TELEGRAM_BOT_TOKEN);

        // Get all the telegramBotList where telegramBotToken equals to UPDATED_TELEGRAM_BOT_TOKEN
        defaultTelegramBotShouldNotBeFound("telegramBotToken.equals=" + UPDATED_TELEGRAM_BOT_TOKEN);
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotTokenIsInShouldWork() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotToken in DEFAULT_TELEGRAM_BOT_TOKEN or UPDATED_TELEGRAM_BOT_TOKEN
        defaultTelegramBotShouldBeFound("telegramBotToken.in=" + DEFAULT_TELEGRAM_BOT_TOKEN + "," + UPDATED_TELEGRAM_BOT_TOKEN);

        // Get all the telegramBotList where telegramBotToken equals to UPDATED_TELEGRAM_BOT_TOKEN
        defaultTelegramBotShouldNotBeFound("telegramBotToken.in=" + UPDATED_TELEGRAM_BOT_TOKEN);
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotToken is not null
        defaultTelegramBotShouldBeFound("telegramBotToken.specified=true");

        // Get all the telegramBotList where telegramBotToken is null
        defaultTelegramBotShouldNotBeFound("telegramBotToken.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotEnabled equals to DEFAULT_TELEGRAM_BOT_ENABLED
        defaultTelegramBotShouldBeFound("telegramBotEnabled.equals=" + DEFAULT_TELEGRAM_BOT_ENABLED);

        // Get all the telegramBotList where telegramBotEnabled equals to UPDATED_TELEGRAM_BOT_ENABLED
        defaultTelegramBotShouldNotBeFound("telegramBotEnabled.equals=" + UPDATED_TELEGRAM_BOT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotEnabled in DEFAULT_TELEGRAM_BOT_ENABLED or UPDATED_TELEGRAM_BOT_ENABLED
        defaultTelegramBotShouldBeFound("telegramBotEnabled.in=" + DEFAULT_TELEGRAM_BOT_ENABLED + "," + UPDATED_TELEGRAM_BOT_ENABLED);

        // Get all the telegramBotList where telegramBotEnabled equals to UPDATED_TELEGRAM_BOT_ENABLED
        defaultTelegramBotShouldNotBeFound("telegramBotEnabled.in=" + UPDATED_TELEGRAM_BOT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllTelegramBotsByTelegramBotEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        telegramBotRepository.saveAndFlush(telegramBot);

        // Get all the telegramBotList where telegramBotEnabled is not null
        defaultTelegramBotShouldBeFound("telegramBotEnabled.specified=true");

        // Get all the telegramBotList where telegramBotEnabled is null
        defaultTelegramBotShouldNotBeFound("telegramBotEnabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTelegramBotShouldBeFound(String filter) throws Exception {
        restTelegramBotMockMvc.perform(get("/api/telegram-bots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].telegramBotName").value(hasItem(DEFAULT_TELEGRAM_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].telegramBotDescription").value(hasItem(DEFAULT_TELEGRAM_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].telegramBotToken").value(hasItem(DEFAULT_TELEGRAM_BOT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].telegramBotEnabled").value(hasItem(DEFAULT_TELEGRAM_BOT_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTelegramBotShouldNotBeFound(String filter) throws Exception {
        restTelegramBotMockMvc.perform(get("/api/telegram-bots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTelegramBot() throws Exception {
        // Get the telegramBot
        restTelegramBotMockMvc.perform(get("/api/telegram-bots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelegramBot() throws Exception {
        // Initialize the database
        telegramBotService.save(telegramBot);

        int databaseSizeBeforeUpdate = telegramBotRepository.findAll().size();

        // Update the telegramBot
        TelegramBot updatedTelegramBot = telegramBotRepository.findOne(telegramBot.getId());
        updatedTelegramBot
            .telegramBotName(UPDATED_TELEGRAM_BOT_NAME)
            .telegramBotDescription(UPDATED_TELEGRAM_BOT_DESCRIPTION)
            .telegramBotToken(UPDATED_TELEGRAM_BOT_TOKEN)
            .telegramBotEnabled(UPDATED_TELEGRAM_BOT_ENABLED);

        restTelegramBotMockMvc.perform(put("/api/telegram-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTelegramBot)))
            .andExpect(status().isOk());

        // Validate the TelegramBot in the database
        List<TelegramBot> telegramBotList = telegramBotRepository.findAll();
        assertThat(telegramBotList).hasSize(databaseSizeBeforeUpdate);
        TelegramBot testTelegramBot = telegramBotList.get(telegramBotList.size() - 1);
        assertThat(testTelegramBot.getTelegramBotName()).isEqualTo(UPDATED_TELEGRAM_BOT_NAME);
        assertThat(testTelegramBot.getTelegramBotDescription()).isEqualTo(UPDATED_TELEGRAM_BOT_DESCRIPTION);
        assertThat(testTelegramBot.getTelegramBotToken()).isEqualTo(UPDATED_TELEGRAM_BOT_TOKEN);
        assertThat(testTelegramBot.isTelegramBotEnabled()).isEqualTo(UPDATED_TELEGRAM_BOT_ENABLED);

        // Validate the TelegramBot in Elasticsearch
        TelegramBot telegramBotEs = telegramBotSearchRepository.findOne(testTelegramBot.getId());
        assertThat(telegramBotEs).isEqualToComparingFieldByField(testTelegramBot);
    }

    @Test
    @Transactional
    public void updateNonExistingTelegramBot() throws Exception {
        int databaseSizeBeforeUpdate = telegramBotRepository.findAll().size();

        // Create the TelegramBot

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTelegramBotMockMvc.perform(put("/api/telegram-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telegramBot)))
            .andExpect(status().isCreated());

        // Validate the TelegramBot in the database
        List<TelegramBot> telegramBotList = telegramBotRepository.findAll();
        assertThat(telegramBotList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTelegramBot() throws Exception {
        // Initialize the database
        telegramBotService.save(telegramBot);

        int databaseSizeBeforeDelete = telegramBotRepository.findAll().size();

        // Get the telegramBot
        restTelegramBotMockMvc.perform(delete("/api/telegram-bots/{id}", telegramBot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean telegramBotExistsInEs = telegramBotSearchRepository.exists(telegramBot.getId());
        assertThat(telegramBotExistsInEs).isFalse();

        // Validate the database is empty
        List<TelegramBot> telegramBotList = telegramBotRepository.findAll();
        assertThat(telegramBotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTelegramBot() throws Exception {
        // Initialize the database
        telegramBotService.save(telegramBot);

        // Search the telegramBot
        restTelegramBotMockMvc.perform(get("/api/_search/telegram-bots?query=id:" + telegramBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].telegramBotName").value(hasItem(DEFAULT_TELEGRAM_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].telegramBotDescription").value(hasItem(DEFAULT_TELEGRAM_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].telegramBotToken").value(hasItem(DEFAULT_TELEGRAM_BOT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].telegramBotEnabled").value(hasItem(DEFAULT_TELEGRAM_BOT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelegramBot.class);
        TelegramBot telegramBot1 = new TelegramBot();
        telegramBot1.setId(1L);
        TelegramBot telegramBot2 = new TelegramBot();
        telegramBot2.setId(telegramBot1.getId());
        assertThat(telegramBot1).isEqualTo(telegramBot2);
        telegramBot2.setId(2L);
        assertThat(telegramBot1).isNotEqualTo(telegramBot2);
        telegramBot1.setId(null);
        assertThat(telegramBot1).isNotEqualTo(telegramBot2);
    }
}
