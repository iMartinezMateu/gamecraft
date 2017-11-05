package com.gamecraft.web.rest;

import com.gamecraft.GamecraftslacknotificationmanagerApp;

import com.gamecraft.domain.SlackAccount;
import com.gamecraft.repository.SlackAccountRepository;
import com.gamecraft.service.SlackAccountService;
import com.gamecraft.repository.search.SlackAccountSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.SlackAccountCriteria;
import com.gamecraft.service.SlackAccountQueryService;

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
 * Test class for the SlackAccountResource REST controller.
 *
 * @see SlackAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftslacknotificationmanagerApp.class)
public class SlackAccountResourceIntTest {

    private static final String DEFAULT_SLACK_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SLACK_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLACK_ACCOUNT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SLACK_ACCOUNT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SLACK_ACCOUNT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_SLACK_ACCOUNT_TOKEN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SLACK_ACCOUNT_ENABLED = false;
    private static final Boolean UPDATED_SLACK_ACCOUNT_ENABLED = true;

    @Autowired
    private SlackAccountRepository slackAccountRepository;

    @Autowired
    private SlackAccountService slackAccountService;

    @Autowired
    private SlackAccountSearchRepository slackAccountSearchRepository;

    @Autowired
    private SlackAccountQueryService slackAccountQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSlackAccountMockMvc;

    private SlackAccount slackAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SlackAccountResource slackAccountResource = new SlackAccountResource(slackAccountService, slackAccountQueryService);
        this.restSlackAccountMockMvc = MockMvcBuilders.standaloneSetup(slackAccountResource)
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
    public static SlackAccount createEntity(EntityManager em) {
        SlackAccount slackAccount = new SlackAccount()
            .slackAccountName(DEFAULT_SLACK_ACCOUNT_NAME)
            .slackAccountDescription(DEFAULT_SLACK_ACCOUNT_DESCRIPTION)
            .slackAccountToken(DEFAULT_SLACK_ACCOUNT_TOKEN)
            .slackAccountEnabled(DEFAULT_SLACK_ACCOUNT_ENABLED);
        return slackAccount;
    }

    @Before
    public void initTest() {
        slackAccountSearchRepository.deleteAll();
        slackAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createSlackAccount() throws Exception {
        int databaseSizeBeforeCreate = slackAccountRepository.findAll().size();

        // Create the SlackAccount
        restSlackAccountMockMvc.perform(post("/api/slack-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackAccount)))
            .andExpect(status().isCreated());

        // Validate the SlackAccount in the database
        List<SlackAccount> slackAccountList = slackAccountRepository.findAll();
        assertThat(slackAccountList).hasSize(databaseSizeBeforeCreate + 1);
        SlackAccount testSlackAccount = slackAccountList.get(slackAccountList.size() - 1);
        assertThat(testSlackAccount.getSlackAccountName()).isEqualTo(DEFAULT_SLACK_ACCOUNT_NAME);
        assertThat(testSlackAccount.getSlackAccountDescription()).isEqualTo(DEFAULT_SLACK_ACCOUNT_DESCRIPTION);
        assertThat(testSlackAccount.getSlackAccountToken()).isEqualTo(DEFAULT_SLACK_ACCOUNT_TOKEN);
        assertThat(testSlackAccount.isSlackAccountEnabled()).isEqualTo(DEFAULT_SLACK_ACCOUNT_ENABLED);

        // Validate the SlackAccount in Elasticsearch
        SlackAccount slackAccountEs = slackAccountSearchRepository.findOne(testSlackAccount.getId());
        assertThat(slackAccountEs).isEqualToComparingFieldByField(testSlackAccount);
    }

    @Test
    @Transactional
    public void createSlackAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slackAccountRepository.findAll().size();

        // Create the SlackAccount with an existing ID
        slackAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlackAccountMockMvc.perform(post("/api/slack-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackAccount)))
            .andExpect(status().isBadRequest());

        // Validate the SlackAccount in the database
        List<SlackAccount> slackAccountList = slackAccountRepository.findAll();
        assertThat(slackAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSlackAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = slackAccountRepository.findAll().size();
        // set the field null
        slackAccount.setSlackAccountName(null);

        // Create the SlackAccount, which fails.

        restSlackAccountMockMvc.perform(post("/api/slack-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackAccount)))
            .andExpect(status().isBadRequest());

        List<SlackAccount> slackAccountList = slackAccountRepository.findAll();
        assertThat(slackAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSlackAccountTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = slackAccountRepository.findAll().size();
        // set the field null
        slackAccount.setSlackAccountToken(null);

        // Create the SlackAccount, which fails.

        restSlackAccountMockMvc.perform(post("/api/slack-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackAccount)))
            .andExpect(status().isBadRequest());

        List<SlackAccount> slackAccountList = slackAccountRepository.findAll();
        assertThat(slackAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSlackAccounts() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList
        restSlackAccountMockMvc.perform(get("/api/slack-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slackAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].slackAccountName").value(hasItem(DEFAULT_SLACK_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].slackAccountDescription").value(hasItem(DEFAULT_SLACK_ACCOUNT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].slackAccountToken").value(hasItem(DEFAULT_SLACK_ACCOUNT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].slackAccountEnabled").value(hasItem(DEFAULT_SLACK_ACCOUNT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getSlackAccount() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get the slackAccount
        restSlackAccountMockMvc.perform(get("/api/slack-accounts/{id}", slackAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(slackAccount.getId().intValue()))
            .andExpect(jsonPath("$.slackAccountName").value(DEFAULT_SLACK_ACCOUNT_NAME.toString()))
            .andExpect(jsonPath("$.slackAccountDescription").value(DEFAULT_SLACK_ACCOUNT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.slackAccountToken").value(DEFAULT_SLACK_ACCOUNT_TOKEN.toString()))
            .andExpect(jsonPath("$.slackAccountEnabled").value(DEFAULT_SLACK_ACCOUNT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountName equals to DEFAULT_SLACK_ACCOUNT_NAME
        defaultSlackAccountShouldBeFound("slackAccountName.equals=" + DEFAULT_SLACK_ACCOUNT_NAME);

        // Get all the slackAccountList where slackAccountName equals to UPDATED_SLACK_ACCOUNT_NAME
        defaultSlackAccountShouldNotBeFound("slackAccountName.equals=" + UPDATED_SLACK_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountName in DEFAULT_SLACK_ACCOUNT_NAME or UPDATED_SLACK_ACCOUNT_NAME
        defaultSlackAccountShouldBeFound("slackAccountName.in=" + DEFAULT_SLACK_ACCOUNT_NAME + "," + UPDATED_SLACK_ACCOUNT_NAME);

        // Get all the slackAccountList where slackAccountName equals to UPDATED_SLACK_ACCOUNT_NAME
        defaultSlackAccountShouldNotBeFound("slackAccountName.in=" + UPDATED_SLACK_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountName is not null
        defaultSlackAccountShouldBeFound("slackAccountName.specified=true");

        // Get all the slackAccountList where slackAccountName is null
        defaultSlackAccountShouldNotBeFound("slackAccountName.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountDescription equals to DEFAULT_SLACK_ACCOUNT_DESCRIPTION
        defaultSlackAccountShouldBeFound("slackAccountDescription.equals=" + DEFAULT_SLACK_ACCOUNT_DESCRIPTION);

        // Get all the slackAccountList where slackAccountDescription equals to UPDATED_SLACK_ACCOUNT_DESCRIPTION
        defaultSlackAccountShouldNotBeFound("slackAccountDescription.equals=" + UPDATED_SLACK_ACCOUNT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountDescription in DEFAULT_SLACK_ACCOUNT_DESCRIPTION or UPDATED_SLACK_ACCOUNT_DESCRIPTION
        defaultSlackAccountShouldBeFound("slackAccountDescription.in=" + DEFAULT_SLACK_ACCOUNT_DESCRIPTION + "," + UPDATED_SLACK_ACCOUNT_DESCRIPTION);

        // Get all the slackAccountList where slackAccountDescription equals to UPDATED_SLACK_ACCOUNT_DESCRIPTION
        defaultSlackAccountShouldNotBeFound("slackAccountDescription.in=" + UPDATED_SLACK_ACCOUNT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountDescription is not null
        defaultSlackAccountShouldBeFound("slackAccountDescription.specified=true");

        // Get all the slackAccountList where slackAccountDescription is null
        defaultSlackAccountShouldNotBeFound("slackAccountDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountToken equals to DEFAULT_SLACK_ACCOUNT_TOKEN
        defaultSlackAccountShouldBeFound("slackAccountToken.equals=" + DEFAULT_SLACK_ACCOUNT_TOKEN);

        // Get all the slackAccountList where slackAccountToken equals to UPDATED_SLACK_ACCOUNT_TOKEN
        defaultSlackAccountShouldNotBeFound("slackAccountToken.equals=" + UPDATED_SLACK_ACCOUNT_TOKEN);
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountTokenIsInShouldWork() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountToken in DEFAULT_SLACK_ACCOUNT_TOKEN or UPDATED_SLACK_ACCOUNT_TOKEN
        defaultSlackAccountShouldBeFound("slackAccountToken.in=" + DEFAULT_SLACK_ACCOUNT_TOKEN + "," + UPDATED_SLACK_ACCOUNT_TOKEN);

        // Get all the slackAccountList where slackAccountToken equals to UPDATED_SLACK_ACCOUNT_TOKEN
        defaultSlackAccountShouldNotBeFound("slackAccountToken.in=" + UPDATED_SLACK_ACCOUNT_TOKEN);
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountToken is not null
        defaultSlackAccountShouldBeFound("slackAccountToken.specified=true");

        // Get all the slackAccountList where slackAccountToken is null
        defaultSlackAccountShouldNotBeFound("slackAccountToken.specified=false");
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountEnabled equals to DEFAULT_SLACK_ACCOUNT_ENABLED
        defaultSlackAccountShouldBeFound("slackAccountEnabled.equals=" + DEFAULT_SLACK_ACCOUNT_ENABLED);

        // Get all the slackAccountList where slackAccountEnabled equals to UPDATED_SLACK_ACCOUNT_ENABLED
        defaultSlackAccountShouldNotBeFound("slackAccountEnabled.equals=" + UPDATED_SLACK_ACCOUNT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountEnabled in DEFAULT_SLACK_ACCOUNT_ENABLED or UPDATED_SLACK_ACCOUNT_ENABLED
        defaultSlackAccountShouldBeFound("slackAccountEnabled.in=" + DEFAULT_SLACK_ACCOUNT_ENABLED + "," + UPDATED_SLACK_ACCOUNT_ENABLED);

        // Get all the slackAccountList where slackAccountEnabled equals to UPDATED_SLACK_ACCOUNT_ENABLED
        defaultSlackAccountShouldNotBeFound("slackAccountEnabled.in=" + UPDATED_SLACK_ACCOUNT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllSlackAccountsBySlackAccountEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        slackAccountRepository.saveAndFlush(slackAccount);

        // Get all the slackAccountList where slackAccountEnabled is not null
        defaultSlackAccountShouldBeFound("slackAccountEnabled.specified=true");

        // Get all the slackAccountList where slackAccountEnabled is null
        defaultSlackAccountShouldNotBeFound("slackAccountEnabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSlackAccountShouldBeFound(String filter) throws Exception {
        restSlackAccountMockMvc.perform(get("/api/slack-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slackAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].slackAccountName").value(hasItem(DEFAULT_SLACK_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].slackAccountDescription").value(hasItem(DEFAULT_SLACK_ACCOUNT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].slackAccountToken").value(hasItem(DEFAULT_SLACK_ACCOUNT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].slackAccountEnabled").value(hasItem(DEFAULT_SLACK_ACCOUNT_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSlackAccountShouldNotBeFound(String filter) throws Exception {
        restSlackAccountMockMvc.perform(get("/api/slack-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSlackAccount() throws Exception {
        // Get the slackAccount
        restSlackAccountMockMvc.perform(get("/api/slack-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSlackAccount() throws Exception {
        // Initialize the database
        slackAccountService.save(slackAccount);

        int databaseSizeBeforeUpdate = slackAccountRepository.findAll().size();

        // Update the slackAccount
        SlackAccount updatedSlackAccount = slackAccountRepository.findOne(slackAccount.getId());
        updatedSlackAccount
            .slackAccountName(UPDATED_SLACK_ACCOUNT_NAME)
            .slackAccountDescription(UPDATED_SLACK_ACCOUNT_DESCRIPTION)
            .slackAccountToken(UPDATED_SLACK_ACCOUNT_TOKEN)
            .slackAccountEnabled(UPDATED_SLACK_ACCOUNT_ENABLED);

        restSlackAccountMockMvc.perform(put("/api/slack-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSlackAccount)))
            .andExpect(status().isOk());

        // Validate the SlackAccount in the database
        List<SlackAccount> slackAccountList = slackAccountRepository.findAll();
        assertThat(slackAccountList).hasSize(databaseSizeBeforeUpdate);
        SlackAccount testSlackAccount = slackAccountList.get(slackAccountList.size() - 1);
        assertThat(testSlackAccount.getSlackAccountName()).isEqualTo(UPDATED_SLACK_ACCOUNT_NAME);
        assertThat(testSlackAccount.getSlackAccountDescription()).isEqualTo(UPDATED_SLACK_ACCOUNT_DESCRIPTION);
        assertThat(testSlackAccount.getSlackAccountToken()).isEqualTo(UPDATED_SLACK_ACCOUNT_TOKEN);
        assertThat(testSlackAccount.isSlackAccountEnabled()).isEqualTo(UPDATED_SLACK_ACCOUNT_ENABLED);

        // Validate the SlackAccount in Elasticsearch
        SlackAccount slackAccountEs = slackAccountSearchRepository.findOne(testSlackAccount.getId());
        assertThat(slackAccountEs).isEqualToComparingFieldByField(testSlackAccount);
    }

    @Test
    @Transactional
    public void updateNonExistingSlackAccount() throws Exception {
        int databaseSizeBeforeUpdate = slackAccountRepository.findAll().size();

        // Create the SlackAccount

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSlackAccountMockMvc.perform(put("/api/slack-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slackAccount)))
            .andExpect(status().isCreated());

        // Validate the SlackAccount in the database
        List<SlackAccount> slackAccountList = slackAccountRepository.findAll();
        assertThat(slackAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSlackAccount() throws Exception {
        // Initialize the database
        slackAccountService.save(slackAccount);

        int databaseSizeBeforeDelete = slackAccountRepository.findAll().size();

        // Get the slackAccount
        restSlackAccountMockMvc.perform(delete("/api/slack-accounts/{id}", slackAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean slackAccountExistsInEs = slackAccountSearchRepository.exists(slackAccount.getId());
        assertThat(slackAccountExistsInEs).isFalse();

        // Validate the database is empty
        List<SlackAccount> slackAccountList = slackAccountRepository.findAll();
        assertThat(slackAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSlackAccount() throws Exception {
        // Initialize the database
        slackAccountService.save(slackAccount);

        // Search the slackAccount
        restSlackAccountMockMvc.perform(get("/api/_search/slack-accounts?query=id:" + slackAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slackAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].slackAccountName").value(hasItem(DEFAULT_SLACK_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].slackAccountDescription").value(hasItem(DEFAULT_SLACK_ACCOUNT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].slackAccountToken").value(hasItem(DEFAULT_SLACK_ACCOUNT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].slackAccountEnabled").value(hasItem(DEFAULT_SLACK_ACCOUNT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlackAccount.class);
        SlackAccount slackAccount1 = new SlackAccount();
        slackAccount1.setId(1L);
        SlackAccount slackAccount2 = new SlackAccount();
        slackAccount2.setId(slackAccount1.getId());
        assertThat(slackAccount1).isEqualTo(slackAccount2);
        slackAccount2.setId(2L);
        assertThat(slackAccount1).isNotEqualTo(slackAccount2);
        slackAccount1.setId(null);
        assertThat(slackAccount1).isNotEqualTo(slackAccount2);
    }
}
