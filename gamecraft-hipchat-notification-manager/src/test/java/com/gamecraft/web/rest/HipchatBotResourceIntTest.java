package com.gamecraft.web.rest;

import com.gamecraft.GamecrafthipchatnotificationmanagerApp;

import com.gamecraft.domain.HipchatBot;
import com.gamecraft.repository.HipchatBotRepository;
import com.gamecraft.service.HipchatBotService;
import com.gamecraft.repository.search.HipchatBotSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.HipchatBotCriteria;
import com.gamecraft.service.HipchatBotQueryService;

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
 * Test class for the HipchatBotResource REST controller.
 *
 * @see HipchatBotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecrafthipchatnotificationmanagerApp.class)
public class HipchatBotResourceIntTest {

    private static final String DEFAULT_HIPCHAT_BOT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_HIPCHAT_BOT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HIPCHAT_BOT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_HIPCHAT_BOT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_HIPCHAT_BOT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_HIPCHAT_BOT_TOKEN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HIPCHAT_BOT_ENABLED = false;
    private static final Boolean UPDATED_HIPCHAT_BOT_ENABLED = true;

    @Autowired
    private HipchatBotRepository hipchatBotRepository;

    @Autowired
    private HipchatBotService hipchatBotService;

    @Autowired
    private HipchatBotSearchRepository hipchatBotSearchRepository;

    @Autowired
    private HipchatBotQueryService hipchatBotQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHipchatBotMockMvc;

    private HipchatBot hipchatBot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HipchatBotResource hipchatBotResource = new HipchatBotResource(hipchatBotService, hipchatBotQueryService);
        this.restHipchatBotMockMvc = MockMvcBuilders.standaloneSetup(hipchatBotResource)
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
    public static HipchatBot createEntity(EntityManager em) {
        HipchatBot hipchatBot = new HipchatBot()
            .hipchatBotName(DEFAULT_HIPCHAT_BOT_NAME)
            .hipchatBotDescription(DEFAULT_HIPCHAT_BOT_DESCRIPTION)
            .hipchatBotToken(DEFAULT_HIPCHAT_BOT_TOKEN)
            .hipchatBotEnabled(DEFAULT_HIPCHAT_BOT_ENABLED);
        return hipchatBot;
    }

    @Before
    public void initTest() {
        hipchatBotSearchRepository.deleteAll();
        hipchatBot = createEntity(em);
    }

    @Test
    @Transactional
    public void createHipchatBot() throws Exception {
        int databaseSizeBeforeCreate = hipchatBotRepository.findAll().size();

        // Create the HipchatBot
        restHipchatBotMockMvc.perform(post("/api/hipchat-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hipchatBot)))
            .andExpect(status().isCreated());

        // Validate the HipchatBot in the database
        List<HipchatBot> hipchatBotList = hipchatBotRepository.findAll();
        assertThat(hipchatBotList).hasSize(databaseSizeBeforeCreate + 1);
        HipchatBot testHipchatBot = hipchatBotList.get(hipchatBotList.size() - 1);
        assertThat(testHipchatBot.getHipchatBotName()).isEqualTo(DEFAULT_HIPCHAT_BOT_NAME);
        assertThat(testHipchatBot.getHipchatBotDescription()).isEqualTo(DEFAULT_HIPCHAT_BOT_DESCRIPTION);
        assertThat(testHipchatBot.getHipchatBotToken()).isEqualTo(DEFAULT_HIPCHAT_BOT_TOKEN);
        assertThat(testHipchatBot.isHipchatBotEnabled()).isEqualTo(DEFAULT_HIPCHAT_BOT_ENABLED);

        // Validate the HipchatBot in Elasticsearch
        HipchatBot hipchatBotEs = hipchatBotSearchRepository.findOne(testHipchatBot.getId());
        assertThat(hipchatBotEs).isEqualToComparingFieldByField(testHipchatBot);
    }

    @Test
    @Transactional
    public void createHipchatBotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hipchatBotRepository.findAll().size();

        // Create the HipchatBot with an existing ID
        hipchatBot.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHipchatBotMockMvc.perform(post("/api/hipchat-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hipchatBot)))
            .andExpect(status().isBadRequest());

        // Validate the HipchatBot in the database
        List<HipchatBot> hipchatBotList = hipchatBotRepository.findAll();
        assertThat(hipchatBotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHipchatBotNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hipchatBotRepository.findAll().size();
        // set the field null
        hipchatBot.setHipchatBotName(null);

        // Create the HipchatBot, which fails.

        restHipchatBotMockMvc.perform(post("/api/hipchat-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hipchatBot)))
            .andExpect(status().isBadRequest());

        List<HipchatBot> hipchatBotList = hipchatBotRepository.findAll();
        assertThat(hipchatBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHipchatBotTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = hipchatBotRepository.findAll().size();
        // set the field null
        hipchatBot.setHipchatBotToken(null);

        // Create the HipchatBot, which fails.

        restHipchatBotMockMvc.perform(post("/api/hipchat-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hipchatBot)))
            .andExpect(status().isBadRequest());

        List<HipchatBot> hipchatBotList = hipchatBotRepository.findAll();
        assertThat(hipchatBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHipchatBots() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList
        restHipchatBotMockMvc.perform(get("/api/hipchat-bots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hipchatBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].hipchatBotName").value(hasItem(DEFAULT_HIPCHAT_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].hipchatBotDescription").value(hasItem(DEFAULT_HIPCHAT_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].hipchatBotToken").value(hasItem(DEFAULT_HIPCHAT_BOT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].hipchatBotEnabled").value(hasItem(DEFAULT_HIPCHAT_BOT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getHipchatBot() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get the hipchatBot
        restHipchatBotMockMvc.perform(get("/api/hipchat-bots/{id}", hipchatBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hipchatBot.getId().intValue()))
            .andExpect(jsonPath("$.hipchatBotName").value(DEFAULT_HIPCHAT_BOT_NAME.toString()))
            .andExpect(jsonPath("$.hipchatBotDescription").value(DEFAULT_HIPCHAT_BOT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.hipchatBotToken").value(DEFAULT_HIPCHAT_BOT_TOKEN.toString()))
            .andExpect(jsonPath("$.hipchatBotEnabled").value(DEFAULT_HIPCHAT_BOT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotNameIsEqualToSomething() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotName equals to DEFAULT_HIPCHAT_BOT_NAME
        defaultHipchatBotShouldBeFound("hipchatBotName.equals=" + DEFAULT_HIPCHAT_BOT_NAME);

        // Get all the hipchatBotList where hipchatBotName equals to UPDATED_HIPCHAT_BOT_NAME
        defaultHipchatBotShouldNotBeFound("hipchatBotName.equals=" + UPDATED_HIPCHAT_BOT_NAME);
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotNameIsInShouldWork() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotName in DEFAULT_HIPCHAT_BOT_NAME or UPDATED_HIPCHAT_BOT_NAME
        defaultHipchatBotShouldBeFound("hipchatBotName.in=" + DEFAULT_HIPCHAT_BOT_NAME + "," + UPDATED_HIPCHAT_BOT_NAME);

        // Get all the hipchatBotList where hipchatBotName equals to UPDATED_HIPCHAT_BOT_NAME
        defaultHipchatBotShouldNotBeFound("hipchatBotName.in=" + UPDATED_HIPCHAT_BOT_NAME);
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotName is not null
        defaultHipchatBotShouldBeFound("hipchatBotName.specified=true");

        // Get all the hipchatBotList where hipchatBotName is null
        defaultHipchatBotShouldNotBeFound("hipchatBotName.specified=false");
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotDescription equals to DEFAULT_HIPCHAT_BOT_DESCRIPTION
        defaultHipchatBotShouldBeFound("hipchatBotDescription.equals=" + DEFAULT_HIPCHAT_BOT_DESCRIPTION);

        // Get all the hipchatBotList where hipchatBotDescription equals to UPDATED_HIPCHAT_BOT_DESCRIPTION
        defaultHipchatBotShouldNotBeFound("hipchatBotDescription.equals=" + UPDATED_HIPCHAT_BOT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotDescription in DEFAULT_HIPCHAT_BOT_DESCRIPTION or UPDATED_HIPCHAT_BOT_DESCRIPTION
        defaultHipchatBotShouldBeFound("hipchatBotDescription.in=" + DEFAULT_HIPCHAT_BOT_DESCRIPTION + "," + UPDATED_HIPCHAT_BOT_DESCRIPTION);

        // Get all the hipchatBotList where hipchatBotDescription equals to UPDATED_HIPCHAT_BOT_DESCRIPTION
        defaultHipchatBotShouldNotBeFound("hipchatBotDescription.in=" + UPDATED_HIPCHAT_BOT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotDescription is not null
        defaultHipchatBotShouldBeFound("hipchatBotDescription.specified=true");

        // Get all the hipchatBotList where hipchatBotDescription is null
        defaultHipchatBotShouldNotBeFound("hipchatBotDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotToken equals to DEFAULT_HIPCHAT_BOT_TOKEN
        defaultHipchatBotShouldBeFound("hipchatBotToken.equals=" + DEFAULT_HIPCHAT_BOT_TOKEN);

        // Get all the hipchatBotList where hipchatBotToken equals to UPDATED_HIPCHAT_BOT_TOKEN
        defaultHipchatBotShouldNotBeFound("hipchatBotToken.equals=" + UPDATED_HIPCHAT_BOT_TOKEN);
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotTokenIsInShouldWork() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotToken in DEFAULT_HIPCHAT_BOT_TOKEN or UPDATED_HIPCHAT_BOT_TOKEN
        defaultHipchatBotShouldBeFound("hipchatBotToken.in=" + DEFAULT_HIPCHAT_BOT_TOKEN + "," + UPDATED_HIPCHAT_BOT_TOKEN);

        // Get all the hipchatBotList where hipchatBotToken equals to UPDATED_HIPCHAT_BOT_TOKEN
        defaultHipchatBotShouldNotBeFound("hipchatBotToken.in=" + UPDATED_HIPCHAT_BOT_TOKEN);
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotToken is not null
        defaultHipchatBotShouldBeFound("hipchatBotToken.specified=true");

        // Get all the hipchatBotList where hipchatBotToken is null
        defaultHipchatBotShouldNotBeFound("hipchatBotToken.specified=false");
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotEnabled equals to DEFAULT_HIPCHAT_BOT_ENABLED
        defaultHipchatBotShouldBeFound("hipchatBotEnabled.equals=" + DEFAULT_HIPCHAT_BOT_ENABLED);

        // Get all the hipchatBotList where hipchatBotEnabled equals to UPDATED_HIPCHAT_BOT_ENABLED
        defaultHipchatBotShouldNotBeFound("hipchatBotEnabled.equals=" + UPDATED_HIPCHAT_BOT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotEnabled in DEFAULT_HIPCHAT_BOT_ENABLED or UPDATED_HIPCHAT_BOT_ENABLED
        defaultHipchatBotShouldBeFound("hipchatBotEnabled.in=" + DEFAULT_HIPCHAT_BOT_ENABLED + "," + UPDATED_HIPCHAT_BOT_ENABLED);

        // Get all the hipchatBotList where hipchatBotEnabled equals to UPDATED_HIPCHAT_BOT_ENABLED
        defaultHipchatBotShouldNotBeFound("hipchatBotEnabled.in=" + UPDATED_HIPCHAT_BOT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllHipchatBotsByHipchatBotEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        hipchatBotRepository.saveAndFlush(hipchatBot);

        // Get all the hipchatBotList where hipchatBotEnabled is not null
        defaultHipchatBotShouldBeFound("hipchatBotEnabled.specified=true");

        // Get all the hipchatBotList where hipchatBotEnabled is null
        defaultHipchatBotShouldNotBeFound("hipchatBotEnabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHipchatBotShouldBeFound(String filter) throws Exception {
        restHipchatBotMockMvc.perform(get("/api/hipchat-bots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hipchatBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].hipchatBotName").value(hasItem(DEFAULT_HIPCHAT_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].hipchatBotDescription").value(hasItem(DEFAULT_HIPCHAT_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].hipchatBotToken").value(hasItem(DEFAULT_HIPCHAT_BOT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].hipchatBotEnabled").value(hasItem(DEFAULT_HIPCHAT_BOT_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHipchatBotShouldNotBeFound(String filter) throws Exception {
        restHipchatBotMockMvc.perform(get("/api/hipchat-bots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingHipchatBot() throws Exception {
        // Get the hipchatBot
        restHipchatBotMockMvc.perform(get("/api/hipchat-bots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHipchatBot() throws Exception {
        // Initialize the database
        hipchatBotService.save(hipchatBot);

        int databaseSizeBeforeUpdate = hipchatBotRepository.findAll().size();

        // Update the hipchatBot
        HipchatBot updatedHipchatBot = hipchatBotRepository.findOne(hipchatBot.getId());
        updatedHipchatBot
            .hipchatBotName(UPDATED_HIPCHAT_BOT_NAME)
            .hipchatBotDescription(UPDATED_HIPCHAT_BOT_DESCRIPTION)
            .hipchatBotToken(UPDATED_HIPCHAT_BOT_TOKEN)
            .hipchatBotEnabled(UPDATED_HIPCHAT_BOT_ENABLED);

        restHipchatBotMockMvc.perform(put("/api/hipchat-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHipchatBot)))
            .andExpect(status().isOk());

        // Validate the HipchatBot in the database
        List<HipchatBot> hipchatBotList = hipchatBotRepository.findAll();
        assertThat(hipchatBotList).hasSize(databaseSizeBeforeUpdate);
        HipchatBot testHipchatBot = hipchatBotList.get(hipchatBotList.size() - 1);
        assertThat(testHipchatBot.getHipchatBotName()).isEqualTo(UPDATED_HIPCHAT_BOT_NAME);
        assertThat(testHipchatBot.getHipchatBotDescription()).isEqualTo(UPDATED_HIPCHAT_BOT_DESCRIPTION);
        assertThat(testHipchatBot.getHipchatBotToken()).isEqualTo(UPDATED_HIPCHAT_BOT_TOKEN);
        assertThat(testHipchatBot.isHipchatBotEnabled()).isEqualTo(UPDATED_HIPCHAT_BOT_ENABLED);

        // Validate the HipchatBot in Elasticsearch
        HipchatBot hipchatBotEs = hipchatBotSearchRepository.findOne(testHipchatBot.getId());
        assertThat(hipchatBotEs).isEqualToComparingFieldByField(testHipchatBot);
    }

    @Test
    @Transactional
    public void updateNonExistingHipchatBot() throws Exception {
        int databaseSizeBeforeUpdate = hipchatBotRepository.findAll().size();

        // Create the HipchatBot

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHipchatBotMockMvc.perform(put("/api/hipchat-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hipchatBot)))
            .andExpect(status().isCreated());

        // Validate the HipchatBot in the database
        List<HipchatBot> hipchatBotList = hipchatBotRepository.findAll();
        assertThat(hipchatBotList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHipchatBot() throws Exception {
        // Initialize the database
        hipchatBotService.save(hipchatBot);

        int databaseSizeBeforeDelete = hipchatBotRepository.findAll().size();

        // Get the hipchatBot
        restHipchatBotMockMvc.perform(delete("/api/hipchat-bots/{id}", hipchatBot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean hipchatBotExistsInEs = hipchatBotSearchRepository.exists(hipchatBot.getId());
        assertThat(hipchatBotExistsInEs).isFalse();

        // Validate the database is empty
        List<HipchatBot> hipchatBotList = hipchatBotRepository.findAll();
        assertThat(hipchatBotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHipchatBot() throws Exception {
        // Initialize the database
        hipchatBotService.save(hipchatBot);

        // Search the hipchatBot
        restHipchatBotMockMvc.perform(get("/api/_search/hipchat-bots?query=id:" + hipchatBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hipchatBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].hipchatBotName").value(hasItem(DEFAULT_HIPCHAT_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].hipchatBotDescription").value(hasItem(DEFAULT_HIPCHAT_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].hipchatBotToken").value(hasItem(DEFAULT_HIPCHAT_BOT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].hipchatBotEnabled").value(hasItem(DEFAULT_HIPCHAT_BOT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HipchatBot.class);
        HipchatBot hipchatBot1 = new HipchatBot();
        hipchatBot1.setId(1L);
        HipchatBot hipchatBot2 = new HipchatBot();
        hipchatBot2.setId(hipchatBot1.getId());
        assertThat(hipchatBot1).isEqualTo(hipchatBot2);
        hipchatBot2.setId(2L);
        assertThat(hipchatBot1).isNotEqualTo(hipchatBot2);
        hipchatBot1.setId(null);
        assertThat(hipchatBot1).isNotEqualTo(hipchatBot2);
    }
}
