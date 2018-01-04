package com.gamecraft.web.rest;

import com.gamecraft.GamecraftircnotificationmanagerApp;

import com.gamecraft.domain.IrcBot;
import com.gamecraft.repository.IrcBotRepository;
import com.gamecraft.service.IrcBotService;
import com.gamecraft.repository.search.IrcBotSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.IrcBotCriteria;
import com.gamecraft.service.IrcBotQueryService;

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
 * Test class for the IrcBotResource REST controller.
 *
 * @see IrcBotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftircnotificationmanagerApp.class)
public class IrcBotResourceIntTest {

    private static final String DEFAULT_IRC_BOT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_IRC_BOT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IRC_BOT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_IRC_BOT_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IRC_BOT_ENABLED = false;
    private static final Boolean UPDATED_IRC_BOT_ENABLED = true;

    private static final String DEFAULT_IRC_SERVER_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IRC_SERVER_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_IRC_SERVER_PORT = 0;
    private static final Integer UPDATED_IRC_SERVER_PORT = 1;

    private static final String DEFAULT_IRC_BOT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_IRC_BOT_NICKNAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED = false;
    private static final Boolean UPDATED_IRC_SERVER_SECURED_PROTOCOL_ENABLED = true;

    @Autowired
    private IrcBotRepository ircBotRepository;

    @Autowired
    private IrcBotService ircBotService;

    @Autowired
    private IrcBotSearchRepository ircBotSearchRepository;

    @Autowired
    private IrcBotQueryService ircBotQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIrcBotMockMvc;

    private IrcBot ircBot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IrcBotResource ircBotResource = new IrcBotResource(ircBotService, ircBotQueryService);
        this.restIrcBotMockMvc = MockMvcBuilders.standaloneSetup(ircBotResource)
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
    public static IrcBot createEntity(EntityManager em) {
        IrcBot ircBot = new IrcBot()
            .ircBotName(DEFAULT_IRC_BOT_NAME)
            .ircBotDescription(DEFAULT_IRC_BOT_DESCRIPTION)
            .ircBotEnabled(DEFAULT_IRC_BOT_ENABLED)
            .ircServerAddress(DEFAULT_IRC_SERVER_ADDRESS)
            .ircServerPort(DEFAULT_IRC_SERVER_PORT)
            .ircBotNickname(DEFAULT_IRC_BOT_NICKNAME)
            .ircServerSecuredProtocolEnabled(DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED);
        return ircBot;
    }

    @Before
    public void initTest() {
        ircBotSearchRepository.deleteAll();
        ircBot = createEntity(em);
    }

    @Test
    @Transactional
    public void createIrcBot() throws Exception {
        int databaseSizeBeforeCreate = ircBotRepository.findAll().size();

        // Create the IrcBot
        restIrcBotMockMvc.perform(post("/api/irc-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ircBot)))
            .andExpect(status().isCreated());

        // Validate the IrcBot in the database
        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeCreate + 1);
        IrcBot testIrcBot = ircBotList.get(ircBotList.size() - 1);
        assertThat(testIrcBot.getIrcBotName()).isEqualTo(DEFAULT_IRC_BOT_NAME);
        assertThat(testIrcBot.getIrcBotDescription()).isEqualTo(DEFAULT_IRC_BOT_DESCRIPTION);
        assertThat(testIrcBot.isIrcBotEnabled()).isEqualTo(DEFAULT_IRC_BOT_ENABLED);
        assertThat(testIrcBot.getIrcServerAddress()).isEqualTo(DEFAULT_IRC_SERVER_ADDRESS);
        assertThat(testIrcBot.getIrcServerPort()).isEqualTo(DEFAULT_IRC_SERVER_PORT);
        assertThat(testIrcBot.getIrcBotNickname()).isEqualTo(DEFAULT_IRC_BOT_NICKNAME);
        assertThat(testIrcBot.isIrcServerSecuredProtocolEnabled()).isEqualTo(DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED);

        // Validate the IrcBot in Elasticsearch
        IrcBot ircBotEs = ircBotSearchRepository.findOne(testIrcBot.getId());
        assertThat(ircBotEs).isEqualToComparingFieldByField(testIrcBot);
    }

    @Test
    @Transactional
    public void createIrcBotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ircBotRepository.findAll().size();

        // Create the IrcBot with an existing ID
        ircBot.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIrcBotMockMvc.perform(post("/api/irc-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ircBot)))
            .andExpect(status().isBadRequest());

        // Validate the IrcBot in the database
        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIrcBotNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ircBotRepository.findAll().size();
        // set the field null
        ircBot.setIrcBotName(null);

        // Create the IrcBot, which fails.

        restIrcBotMockMvc.perform(post("/api/irc-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ircBot)))
            .andExpect(status().isBadRequest());

        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIrcServerAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = ircBotRepository.findAll().size();
        // set the field null
        ircBot.setIrcServerAddress(null);

        // Create the IrcBot, which fails.

        restIrcBotMockMvc.perform(post("/api/irc-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ircBot)))
            .andExpect(status().isBadRequest());

        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIrcServerPortIsRequired() throws Exception {
        int databaseSizeBeforeTest = ircBotRepository.findAll().size();
        // set the field null
        ircBot.setIrcServerPort(null);

        // Create the IrcBot, which fails.

        restIrcBotMockMvc.perform(post("/api/irc-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ircBot)))
            .andExpect(status().isBadRequest());

        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIrcBotNicknameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ircBotRepository.findAll().size();
        // set the field null
        ircBot.setIrcBotNickname(null);

        // Create the IrcBot, which fails.

        restIrcBotMockMvc.perform(post("/api/irc-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ircBot)))
            .andExpect(status().isBadRequest());

        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIrcServerSecuredProtocolEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = ircBotRepository.findAll().size();
        // set the field null
        ircBot.setIrcServerSecuredProtocolEnabled(null);

        // Create the IrcBot, which fails.

        restIrcBotMockMvc.perform(post("/api/irc-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ircBot)))
            .andExpect(status().isBadRequest());

        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIrcBots() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList
        restIrcBotMockMvc.perform(get("/api/irc-bots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ircBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].ircBotName").value(hasItem(DEFAULT_IRC_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].ircBotDescription").value(hasItem(DEFAULT_IRC_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].ircBotEnabled").value(hasItem(DEFAULT_IRC_BOT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].ircServerAddress").value(hasItem(DEFAULT_IRC_SERVER_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].ircServerPort").value(hasItem(DEFAULT_IRC_SERVER_PORT)))
            .andExpect(jsonPath("$.[*].ircBotNickname").value(hasItem(DEFAULT_IRC_BOT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].ircServerSecuredProtocolEnabled").value(hasItem(DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getIrcBot() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get the ircBot
        restIrcBotMockMvc.perform(get("/api/irc-bots/{id}", ircBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ircBot.getId().intValue()))
            .andExpect(jsonPath("$.ircBotName").value(DEFAULT_IRC_BOT_NAME.toString()))
            .andExpect(jsonPath("$.ircBotDescription").value(DEFAULT_IRC_BOT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.ircBotEnabled").value(DEFAULT_IRC_BOT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.ircServerAddress").value(DEFAULT_IRC_SERVER_ADDRESS.toString()))
            .andExpect(jsonPath("$.ircServerPort").value(DEFAULT_IRC_SERVER_PORT))
            .andExpect(jsonPath("$.ircBotNickname").value(DEFAULT_IRC_BOT_NICKNAME.toString()))
            .andExpect(jsonPath("$.ircServerSecuredProtocolEnabled").value(DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotName equals to DEFAULT_IRC_BOT_NAME
        defaultIrcBotShouldBeFound("ircBotName.equals=" + DEFAULT_IRC_BOT_NAME);

        // Get all the ircBotList where ircBotName equals to UPDATED_IRC_BOT_NAME
        defaultIrcBotShouldNotBeFound("ircBotName.equals=" + UPDATED_IRC_BOT_NAME);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotNameIsInShouldWork() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotName in DEFAULT_IRC_BOT_NAME or UPDATED_IRC_BOT_NAME
        defaultIrcBotShouldBeFound("ircBotName.in=" + DEFAULT_IRC_BOT_NAME + "," + UPDATED_IRC_BOT_NAME);

        // Get all the ircBotList where ircBotName equals to UPDATED_IRC_BOT_NAME
        defaultIrcBotShouldNotBeFound("ircBotName.in=" + UPDATED_IRC_BOT_NAME);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotName is not null
        defaultIrcBotShouldBeFound("ircBotName.specified=true");

        // Get all the ircBotList where ircBotName is null
        defaultIrcBotShouldNotBeFound("ircBotName.specified=false");
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotDescription equals to DEFAULT_IRC_BOT_DESCRIPTION
        defaultIrcBotShouldBeFound("ircBotDescription.equals=" + DEFAULT_IRC_BOT_DESCRIPTION);

        // Get all the ircBotList where ircBotDescription equals to UPDATED_IRC_BOT_DESCRIPTION
        defaultIrcBotShouldNotBeFound("ircBotDescription.equals=" + UPDATED_IRC_BOT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotDescription in DEFAULT_IRC_BOT_DESCRIPTION or UPDATED_IRC_BOT_DESCRIPTION
        defaultIrcBotShouldBeFound("ircBotDescription.in=" + DEFAULT_IRC_BOT_DESCRIPTION + "," + UPDATED_IRC_BOT_DESCRIPTION);

        // Get all the ircBotList where ircBotDescription equals to UPDATED_IRC_BOT_DESCRIPTION
        defaultIrcBotShouldNotBeFound("ircBotDescription.in=" + UPDATED_IRC_BOT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotDescription is not null
        defaultIrcBotShouldBeFound("ircBotDescription.specified=true");

        // Get all the ircBotList where ircBotDescription is null
        defaultIrcBotShouldNotBeFound("ircBotDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotEnabled equals to DEFAULT_IRC_BOT_ENABLED
        defaultIrcBotShouldBeFound("ircBotEnabled.equals=" + DEFAULT_IRC_BOT_ENABLED);

        // Get all the ircBotList where ircBotEnabled equals to UPDATED_IRC_BOT_ENABLED
        defaultIrcBotShouldNotBeFound("ircBotEnabled.equals=" + UPDATED_IRC_BOT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotEnabled in DEFAULT_IRC_BOT_ENABLED or UPDATED_IRC_BOT_ENABLED
        defaultIrcBotShouldBeFound("ircBotEnabled.in=" + DEFAULT_IRC_BOT_ENABLED + "," + UPDATED_IRC_BOT_ENABLED);

        // Get all the ircBotList where ircBotEnabled equals to UPDATED_IRC_BOT_ENABLED
        defaultIrcBotShouldNotBeFound("ircBotEnabled.in=" + UPDATED_IRC_BOT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotEnabled is not null
        defaultIrcBotShouldBeFound("ircBotEnabled.specified=true");

        // Get all the ircBotList where ircBotEnabled is null
        defaultIrcBotShouldNotBeFound("ircBotEnabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerAddress equals to DEFAULT_IRC_SERVER_ADDRESS
        defaultIrcBotShouldBeFound("ircServerAddress.equals=" + DEFAULT_IRC_SERVER_ADDRESS);

        // Get all the ircBotList where ircServerAddress equals to UPDATED_IRC_SERVER_ADDRESS
        defaultIrcBotShouldNotBeFound("ircServerAddress.equals=" + UPDATED_IRC_SERVER_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerAddressIsInShouldWork() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerAddress in DEFAULT_IRC_SERVER_ADDRESS or UPDATED_IRC_SERVER_ADDRESS
        defaultIrcBotShouldBeFound("ircServerAddress.in=" + DEFAULT_IRC_SERVER_ADDRESS + "," + UPDATED_IRC_SERVER_ADDRESS);

        // Get all the ircBotList where ircServerAddress equals to UPDATED_IRC_SERVER_ADDRESS
        defaultIrcBotShouldNotBeFound("ircServerAddress.in=" + UPDATED_IRC_SERVER_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerAddress is not null
        defaultIrcBotShouldBeFound("ircServerAddress.specified=true");

        // Get all the ircBotList where ircServerAddress is null
        defaultIrcBotShouldNotBeFound("ircServerAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerPortIsEqualToSomething() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerPort equals to DEFAULT_IRC_SERVER_PORT
        defaultIrcBotShouldBeFound("ircServerPort.equals=" + DEFAULT_IRC_SERVER_PORT);

        // Get all the ircBotList where ircServerPort equals to UPDATED_IRC_SERVER_PORT
        defaultIrcBotShouldNotBeFound("ircServerPort.equals=" + UPDATED_IRC_SERVER_PORT);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerPortIsInShouldWork() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerPort in DEFAULT_IRC_SERVER_PORT or UPDATED_IRC_SERVER_PORT
        defaultIrcBotShouldBeFound("ircServerPort.in=" + DEFAULT_IRC_SERVER_PORT + "," + UPDATED_IRC_SERVER_PORT);

        // Get all the ircBotList where ircServerPort equals to UPDATED_IRC_SERVER_PORT
        defaultIrcBotShouldNotBeFound("ircServerPort.in=" + UPDATED_IRC_SERVER_PORT);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerPort is not null
        defaultIrcBotShouldBeFound("ircServerPort.specified=true");

        // Get all the ircBotList where ircServerPort is null
        defaultIrcBotShouldNotBeFound("ircServerPort.specified=false");
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerPortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerPort greater than or equals to DEFAULT_IRC_SERVER_PORT
        defaultIrcBotShouldBeFound("ircServerPort.greaterOrEqualThan=" + DEFAULT_IRC_SERVER_PORT);

        // Get all the ircBotList where ircServerPort greater than or equals to UPDATED_IRC_SERVER_PORT
        defaultIrcBotShouldNotBeFound("ircServerPort.greaterOrEqualThan=" + UPDATED_IRC_SERVER_PORT);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerPortIsLessThanSomething() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerPort less than or equals to DEFAULT_IRC_SERVER_PORT
        defaultIrcBotShouldNotBeFound("ircServerPort.lessThan=" + DEFAULT_IRC_SERVER_PORT);

        // Get all the ircBotList where ircServerPort less than or equals to UPDATED_IRC_SERVER_PORT
        defaultIrcBotShouldBeFound("ircServerPort.lessThan=" + UPDATED_IRC_SERVER_PORT);
    }


    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotNicknameIsEqualToSomething() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotNickname equals to DEFAULT_IRC_BOT_NICKNAME
        defaultIrcBotShouldBeFound("ircBotNickname.equals=" + DEFAULT_IRC_BOT_NICKNAME);

        // Get all the ircBotList where ircBotNickname equals to UPDATED_IRC_BOT_NICKNAME
        defaultIrcBotShouldNotBeFound("ircBotNickname.equals=" + UPDATED_IRC_BOT_NICKNAME);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotNicknameIsInShouldWork() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotNickname in DEFAULT_IRC_BOT_NICKNAME or UPDATED_IRC_BOT_NICKNAME
        defaultIrcBotShouldBeFound("ircBotNickname.in=" + DEFAULT_IRC_BOT_NICKNAME + "," + UPDATED_IRC_BOT_NICKNAME);

        // Get all the ircBotList where ircBotNickname equals to UPDATED_IRC_BOT_NICKNAME
        defaultIrcBotShouldNotBeFound("ircBotNickname.in=" + UPDATED_IRC_BOT_NICKNAME);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcBotNicknameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircBotNickname is not null
        defaultIrcBotShouldBeFound("ircBotNickname.specified=true");

        // Get all the ircBotList where ircBotNickname is null
        defaultIrcBotShouldNotBeFound("ircBotNickname.specified=false");
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerSecuredProtocolEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerSecuredProtocolEnabled equals to DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED
        defaultIrcBotShouldBeFound("ircServerSecuredProtocolEnabled.equals=" + DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED);

        // Get all the ircBotList where ircServerSecuredProtocolEnabled equals to UPDATED_IRC_SERVER_SECURED_PROTOCOL_ENABLED
        defaultIrcBotShouldNotBeFound("ircServerSecuredProtocolEnabled.equals=" + UPDATED_IRC_SERVER_SECURED_PROTOCOL_ENABLED);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerSecuredProtocolEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerSecuredProtocolEnabled in DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED or UPDATED_IRC_SERVER_SECURED_PROTOCOL_ENABLED
        defaultIrcBotShouldBeFound("ircServerSecuredProtocolEnabled.in=" + DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED + "," + UPDATED_IRC_SERVER_SECURED_PROTOCOL_ENABLED);

        // Get all the ircBotList where ircServerSecuredProtocolEnabled equals to UPDATED_IRC_SERVER_SECURED_PROTOCOL_ENABLED
        defaultIrcBotShouldNotBeFound("ircServerSecuredProtocolEnabled.in=" + UPDATED_IRC_SERVER_SECURED_PROTOCOL_ENABLED);
    }

    @Test
    @Transactional
    public void getAllIrcBotsByIrcServerSecuredProtocolEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        ircBotRepository.saveAndFlush(ircBot);

        // Get all the ircBotList where ircServerSecuredProtocolEnabled is not null
        defaultIrcBotShouldBeFound("ircServerSecuredProtocolEnabled.specified=true");

        // Get all the ircBotList where ircServerSecuredProtocolEnabled is null
        defaultIrcBotShouldNotBeFound("ircServerSecuredProtocolEnabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultIrcBotShouldBeFound(String filter) throws Exception {
        restIrcBotMockMvc.perform(get("/api/irc-bots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ircBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].ircBotName").value(hasItem(DEFAULT_IRC_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].ircBotDescription").value(hasItem(DEFAULT_IRC_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].ircBotEnabled").value(hasItem(DEFAULT_IRC_BOT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].ircServerAddress").value(hasItem(DEFAULT_IRC_SERVER_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].ircServerPort").value(hasItem(DEFAULT_IRC_SERVER_PORT)))
            .andExpect(jsonPath("$.[*].ircBotNickname").value(hasItem(DEFAULT_IRC_BOT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].ircServerSecuredProtocolEnabled").value(hasItem(DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultIrcBotShouldNotBeFound(String filter) throws Exception {
        restIrcBotMockMvc.perform(get("/api/irc-bots?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingIrcBot() throws Exception {
        // Get the ircBot
        restIrcBotMockMvc.perform(get("/api/irc-bots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIrcBot() throws Exception {
        // Initialize the database
        ircBotService.save(ircBot);

        int databaseSizeBeforeUpdate = ircBotRepository.findAll().size();

        // Update the ircBot
        IrcBot updatedIrcBot = ircBotRepository.findOne(ircBot.getId());
        updatedIrcBot
            .ircBotName(UPDATED_IRC_BOT_NAME)
            .ircBotDescription(UPDATED_IRC_BOT_DESCRIPTION)
            .ircBotEnabled(UPDATED_IRC_BOT_ENABLED)
            .ircServerAddress(UPDATED_IRC_SERVER_ADDRESS)
            .ircServerPort(UPDATED_IRC_SERVER_PORT)
            .ircBotNickname(UPDATED_IRC_BOT_NICKNAME)
            .ircServerSecuredProtocolEnabled(UPDATED_IRC_SERVER_SECURED_PROTOCOL_ENABLED);

        restIrcBotMockMvc.perform(put("/api/irc-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIrcBot)))
            .andExpect(status().isOk());

        // Validate the IrcBot in the database
        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeUpdate);
        IrcBot testIrcBot = ircBotList.get(ircBotList.size() - 1);
        assertThat(testIrcBot.getIrcBotName()).isEqualTo(UPDATED_IRC_BOT_NAME);
        assertThat(testIrcBot.getIrcBotDescription()).isEqualTo(UPDATED_IRC_BOT_DESCRIPTION);
        assertThat(testIrcBot.isIrcBotEnabled()).isEqualTo(UPDATED_IRC_BOT_ENABLED);
        assertThat(testIrcBot.getIrcServerAddress()).isEqualTo(UPDATED_IRC_SERVER_ADDRESS);
        assertThat(testIrcBot.getIrcServerPort()).isEqualTo(UPDATED_IRC_SERVER_PORT);
        assertThat(testIrcBot.getIrcBotNickname()).isEqualTo(UPDATED_IRC_BOT_NICKNAME);
        assertThat(testIrcBot.isIrcServerSecuredProtocolEnabled()).isEqualTo(UPDATED_IRC_SERVER_SECURED_PROTOCOL_ENABLED);

        // Validate the IrcBot in Elasticsearch
        IrcBot ircBotEs = ircBotSearchRepository.findOne(testIrcBot.getId());
        assertThat(ircBotEs).isEqualToComparingFieldByField(testIrcBot);
    }

    @Test
    @Transactional
    public void updateNonExistingIrcBot() throws Exception {
        int databaseSizeBeforeUpdate = ircBotRepository.findAll().size();

        // Create the IrcBot

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIrcBotMockMvc.perform(put("/api/irc-bots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ircBot)))
            .andExpect(status().isCreated());

        // Validate the IrcBot in the database
        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIrcBot() throws Exception {
        // Initialize the database
        ircBotService.save(ircBot);

        int databaseSizeBeforeDelete = ircBotRepository.findAll().size();

        // Get the ircBot
        restIrcBotMockMvc.perform(delete("/api/irc-bots/{id}", ircBot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ircBotExistsInEs = ircBotSearchRepository.exists(ircBot.getId());
        assertThat(ircBotExistsInEs).isFalse();

        // Validate the database is empty
        List<IrcBot> ircBotList = ircBotRepository.findAll();
        assertThat(ircBotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIrcBot() throws Exception {
        // Initialize the database
        ircBotService.save(ircBot);

        // Search the ircBot
        restIrcBotMockMvc.perform(get("/api/_search/irc-bots?query=id:" + ircBot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ircBot.getId().intValue())))
            .andExpect(jsonPath("$.[*].ircBotName").value(hasItem(DEFAULT_IRC_BOT_NAME.toString())))
            .andExpect(jsonPath("$.[*].ircBotDescription").value(hasItem(DEFAULT_IRC_BOT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].ircBotEnabled").value(hasItem(DEFAULT_IRC_BOT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].ircServerAddress").value(hasItem(DEFAULT_IRC_SERVER_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].ircServerPort").value(hasItem(DEFAULT_IRC_SERVER_PORT)))
            .andExpect(jsonPath("$.[*].ircBotNickname").value(hasItem(DEFAULT_IRC_BOT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].ircServerSecuredProtocolEnabled").value(hasItem(DEFAULT_IRC_SERVER_SECURED_PROTOCOL_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IrcBot.class);
        IrcBot ircBot1 = new IrcBot();
        ircBot1.setId(1L);
        IrcBot ircBot2 = new IrcBot();
        ircBot2.setId(ircBot1.getId());
        assertThat(ircBot1).isEqualTo(ircBot2);
        ircBot2.setId(2L);
        assertThat(ircBot1).isNotEqualTo(ircBot2);
        ircBot1.setId(null);
        assertThat(ircBot1).isNotEqualTo(ircBot2);
    }
}
