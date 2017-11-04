package com.gamecraft.web.rest;

import com.gamecraft.GamecraftemailnotificationmanagerApp;

import com.gamecraft.domain.EmailAccount;
import com.gamecraft.repository.EmailAccountRepository;
import com.gamecraft.service.EmailAccountService;
import com.gamecraft.repository.search.EmailAccountSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.EmailAccountCriteria;
import com.gamecraft.service.EmailAccountQueryService;

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
 * Test class for the EmailAccountResource REST controller.
 *
 * @see EmailAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftemailnotificationmanagerApp.class)
public class EmailAccountResourceIntTest {

    private static final String DEFAULT_EMAIL_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_SMTP_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_SMTP_SERVER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_SMTP_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_SMTP_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_SMTP_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_SMTP_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EMAIL_SMTP_USE_SSL = false;
    private static final Boolean UPDATED_EMAIL_SMTP_USE_SSL = true;

    private static final Integer DEFAULT_EMAIL_SMTP_PORT = 1;
    private static final Integer UPDATED_EMAIL_SMTP_PORT = 2;

    private static final String DEFAULT_EMAIL_ACCOUNT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ACCOUNT_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EMAIL_ACCOUNT_ENABLED = false;
    private static final Boolean UPDATED_EMAIL_ACCOUNT_ENABLED = true;

    @Autowired
    private EmailAccountRepository emailAccountRepository;

    @Autowired
    private EmailAccountService emailAccountService;

    @Autowired
    private EmailAccountSearchRepository emailAccountSearchRepository;

    @Autowired
    private EmailAccountQueryService emailAccountQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmailAccountMockMvc;

    private EmailAccount emailAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmailAccountResource emailAccountResource = new EmailAccountResource(emailAccountService, emailAccountQueryService);
        this.restEmailAccountMockMvc = MockMvcBuilders.standaloneSetup(emailAccountResource)
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
    public static EmailAccount createEntity(EntityManager em) {
        EmailAccount emailAccount = new EmailAccount()
            .emailAccountName(DEFAULT_EMAIL_ACCOUNT_NAME)
            .emailSmtpServer(DEFAULT_EMAIL_SMTP_SERVER)
            .emailSmtpUsername(DEFAULT_EMAIL_SMTP_USERNAME)
            .emailSmtpPassword(DEFAULT_EMAIL_SMTP_PASSWORD)
            .emailSmtpUseSSL(DEFAULT_EMAIL_SMTP_USE_SSL)
            .emailSmtpPort(DEFAULT_EMAIL_SMTP_PORT)
            .emailAccountDescription(DEFAULT_EMAIL_ACCOUNT_DESCRIPTION)
            .emailAccountEnabled(DEFAULT_EMAIL_ACCOUNT_ENABLED);
        return emailAccount;
    }

    @Before
    public void initTest() {
        emailAccountSearchRepository.deleteAll();
        emailAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmailAccount() throws Exception {
        int databaseSizeBeforeCreate = emailAccountRepository.findAll().size();

        // Create the EmailAccount
        restEmailAccountMockMvc.perform(post("/api/email-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailAccount)))
            .andExpect(status().isCreated());

        // Validate the EmailAccount in the database
        List<EmailAccount> emailAccountList = emailAccountRepository.findAll();
        assertThat(emailAccountList).hasSize(databaseSizeBeforeCreate + 1);
        EmailAccount testEmailAccount = emailAccountList.get(emailAccountList.size() - 1);
        assertThat(testEmailAccount.getEmailAccountName()).isEqualTo(DEFAULT_EMAIL_ACCOUNT_NAME);
        assertThat(testEmailAccount.getEmailSmtpServer()).isEqualTo(DEFAULT_EMAIL_SMTP_SERVER);
        assertThat(testEmailAccount.getEmailSmtpUsername()).isEqualTo(DEFAULT_EMAIL_SMTP_USERNAME);
        assertThat(testEmailAccount.getEmailSmtpPassword()).isEqualTo(DEFAULT_EMAIL_SMTP_PASSWORD);
        assertThat(testEmailAccount.isEmailSmtpUseSSL()).isEqualTo(DEFAULT_EMAIL_SMTP_USE_SSL);
        assertThat(testEmailAccount.getEmailSmtpPort()).isEqualTo(DEFAULT_EMAIL_SMTP_PORT);
        assertThat(testEmailAccount.getEmailAccountDescription()).isEqualTo(DEFAULT_EMAIL_ACCOUNT_DESCRIPTION);
        assertThat(testEmailAccount.isEmailAccountEnabled()).isEqualTo(DEFAULT_EMAIL_ACCOUNT_ENABLED);

        // Validate the EmailAccount in Elasticsearch
        EmailAccount emailAccountEs = emailAccountSearchRepository.findOne(testEmailAccount.getId());
        assertThat(emailAccountEs).isEqualToComparingFieldByField(testEmailAccount);
    }

    @Test
    @Transactional
    public void createEmailAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailAccountRepository.findAll().size();

        // Create the EmailAccount with an existing ID
        emailAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailAccountMockMvc.perform(post("/api/email-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailAccount)))
            .andExpect(status().isBadRequest());

        // Validate the EmailAccount in the database
        List<EmailAccount> emailAccountList = emailAccountRepository.findAll();
        assertThat(emailAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailAccountRepository.findAll().size();
        // set the field null
        emailAccount.setEmailAccountName(null);

        // Create the EmailAccount, which fails.

        restEmailAccountMockMvc.perform(post("/api/email-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailAccount)))
            .andExpect(status().isBadRequest());

        List<EmailAccount> emailAccountList = emailAccountRepository.findAll();
        assertThat(emailAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailSmtpServerIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailAccountRepository.findAll().size();
        // set the field null
        emailAccount.setEmailSmtpServer(null);

        // Create the EmailAccount, which fails.

        restEmailAccountMockMvc.perform(post("/api/email-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailAccount)))
            .andExpect(status().isBadRequest());

        List<EmailAccount> emailAccountList = emailAccountRepository.findAll();
        assertThat(emailAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmailAccounts() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList
        restEmailAccountMockMvc.perform(get("/api/email-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailAccountName").value(hasItem(DEFAULT_EMAIL_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpServer").value(hasItem(DEFAULT_EMAIL_SMTP_SERVER.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpUsername").value(hasItem(DEFAULT_EMAIL_SMTP_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpPassword").value(hasItem(DEFAULT_EMAIL_SMTP_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpUseSSL").value(hasItem(DEFAULT_EMAIL_SMTP_USE_SSL.booleanValue())))
            .andExpect(jsonPath("$.[*].emailSmtpPort").value(hasItem(DEFAULT_EMAIL_SMTP_PORT)))
            .andExpect(jsonPath("$.[*].emailAccountDescription").value(hasItem(DEFAULT_EMAIL_ACCOUNT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].emailAccountEnabled").value(hasItem(DEFAULT_EMAIL_ACCOUNT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getEmailAccount() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get the emailAccount
        restEmailAccountMockMvc.perform(get("/api/email-accounts/{id}", emailAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(emailAccount.getId().intValue()))
            .andExpect(jsonPath("$.emailAccountName").value(DEFAULT_EMAIL_ACCOUNT_NAME.toString()))
            .andExpect(jsonPath("$.emailSmtpServer").value(DEFAULT_EMAIL_SMTP_SERVER.toString()))
            .andExpect(jsonPath("$.emailSmtpUsername").value(DEFAULT_EMAIL_SMTP_USERNAME.toString()))
            .andExpect(jsonPath("$.emailSmtpPassword").value(DEFAULT_EMAIL_SMTP_PASSWORD.toString()))
            .andExpect(jsonPath("$.emailSmtpUseSSL").value(DEFAULT_EMAIL_SMTP_USE_SSL.booleanValue()))
            .andExpect(jsonPath("$.emailSmtpPort").value(DEFAULT_EMAIL_SMTP_PORT))
            .andExpect(jsonPath("$.emailAccountDescription").value(DEFAULT_EMAIL_ACCOUNT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.emailAccountEnabled").value(DEFAULT_EMAIL_ACCOUNT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailAccountName equals to DEFAULT_EMAIL_ACCOUNT_NAME
        defaultEmailAccountShouldBeFound("emailAccountName.equals=" + DEFAULT_EMAIL_ACCOUNT_NAME);

        // Get all the emailAccountList where emailAccountName equals to UPDATED_EMAIL_ACCOUNT_NAME
        defaultEmailAccountShouldNotBeFound("emailAccountName.equals=" + UPDATED_EMAIL_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailAccountName in DEFAULT_EMAIL_ACCOUNT_NAME or UPDATED_EMAIL_ACCOUNT_NAME
        defaultEmailAccountShouldBeFound("emailAccountName.in=" + DEFAULT_EMAIL_ACCOUNT_NAME + "," + UPDATED_EMAIL_ACCOUNT_NAME);

        // Get all the emailAccountList where emailAccountName equals to UPDATED_EMAIL_ACCOUNT_NAME
        defaultEmailAccountShouldNotBeFound("emailAccountName.in=" + UPDATED_EMAIL_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailAccountName is not null
        defaultEmailAccountShouldBeFound("emailAccountName.specified=true");

        // Get all the emailAccountList where emailAccountName is null
        defaultEmailAccountShouldNotBeFound("emailAccountName.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpServerIsEqualToSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpServer equals to DEFAULT_EMAIL_SMTP_SERVER
        defaultEmailAccountShouldBeFound("emailSmtpServer.equals=" + DEFAULT_EMAIL_SMTP_SERVER);

        // Get all the emailAccountList where emailSmtpServer equals to UPDATED_EMAIL_SMTP_SERVER
        defaultEmailAccountShouldNotBeFound("emailSmtpServer.equals=" + UPDATED_EMAIL_SMTP_SERVER);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpServerIsInShouldWork() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpServer in DEFAULT_EMAIL_SMTP_SERVER or UPDATED_EMAIL_SMTP_SERVER
        defaultEmailAccountShouldBeFound("emailSmtpServer.in=" + DEFAULT_EMAIL_SMTP_SERVER + "," + UPDATED_EMAIL_SMTP_SERVER);

        // Get all the emailAccountList where emailSmtpServer equals to UPDATED_EMAIL_SMTP_SERVER
        defaultEmailAccountShouldNotBeFound("emailSmtpServer.in=" + UPDATED_EMAIL_SMTP_SERVER);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpServerIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpServer is not null
        defaultEmailAccountShouldBeFound("emailSmtpServer.specified=true");

        // Get all the emailAccountList where emailSmtpServer is null
        defaultEmailAccountShouldNotBeFound("emailSmtpServer.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpUsername equals to DEFAULT_EMAIL_SMTP_USERNAME
        defaultEmailAccountShouldBeFound("emailSmtpUsername.equals=" + DEFAULT_EMAIL_SMTP_USERNAME);

        // Get all the emailAccountList where emailSmtpUsername equals to UPDATED_EMAIL_SMTP_USERNAME
        defaultEmailAccountShouldNotBeFound("emailSmtpUsername.equals=" + UPDATED_EMAIL_SMTP_USERNAME);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpUsername in DEFAULT_EMAIL_SMTP_USERNAME or UPDATED_EMAIL_SMTP_USERNAME
        defaultEmailAccountShouldBeFound("emailSmtpUsername.in=" + DEFAULT_EMAIL_SMTP_USERNAME + "," + UPDATED_EMAIL_SMTP_USERNAME);

        // Get all the emailAccountList where emailSmtpUsername equals to UPDATED_EMAIL_SMTP_USERNAME
        defaultEmailAccountShouldNotBeFound("emailSmtpUsername.in=" + UPDATED_EMAIL_SMTP_USERNAME);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpUsername is not null
        defaultEmailAccountShouldBeFound("emailSmtpUsername.specified=true");

        // Get all the emailAccountList where emailSmtpUsername is null
        defaultEmailAccountShouldNotBeFound("emailSmtpUsername.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpPassword equals to DEFAULT_EMAIL_SMTP_PASSWORD
        defaultEmailAccountShouldBeFound("emailSmtpPassword.equals=" + DEFAULT_EMAIL_SMTP_PASSWORD);

        // Get all the emailAccountList where emailSmtpPassword equals to UPDATED_EMAIL_SMTP_PASSWORD
        defaultEmailAccountShouldNotBeFound("emailSmtpPassword.equals=" + UPDATED_EMAIL_SMTP_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpPassword in DEFAULT_EMAIL_SMTP_PASSWORD or UPDATED_EMAIL_SMTP_PASSWORD
        defaultEmailAccountShouldBeFound("emailSmtpPassword.in=" + DEFAULT_EMAIL_SMTP_PASSWORD + "," + UPDATED_EMAIL_SMTP_PASSWORD);

        // Get all the emailAccountList where emailSmtpPassword equals to UPDATED_EMAIL_SMTP_PASSWORD
        defaultEmailAccountShouldNotBeFound("emailSmtpPassword.in=" + UPDATED_EMAIL_SMTP_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpPassword is not null
        defaultEmailAccountShouldBeFound("emailSmtpPassword.specified=true");

        // Get all the emailAccountList where emailSmtpPassword is null
        defaultEmailAccountShouldNotBeFound("emailSmtpPassword.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpUseSSLIsEqualToSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpUseSSL equals to DEFAULT_EMAIL_SMTP_USE_SSL
        defaultEmailAccountShouldBeFound("emailSmtpUseSSL.equals=" + DEFAULT_EMAIL_SMTP_USE_SSL);

        // Get all the emailAccountList where emailSmtpUseSSL equals to UPDATED_EMAIL_SMTP_USE_SSL
        defaultEmailAccountShouldNotBeFound("emailSmtpUseSSL.equals=" + UPDATED_EMAIL_SMTP_USE_SSL);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpUseSSLIsInShouldWork() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpUseSSL in DEFAULT_EMAIL_SMTP_USE_SSL or UPDATED_EMAIL_SMTP_USE_SSL
        defaultEmailAccountShouldBeFound("emailSmtpUseSSL.in=" + DEFAULT_EMAIL_SMTP_USE_SSL + "," + UPDATED_EMAIL_SMTP_USE_SSL);

        // Get all the emailAccountList where emailSmtpUseSSL equals to UPDATED_EMAIL_SMTP_USE_SSL
        defaultEmailAccountShouldNotBeFound("emailSmtpUseSSL.in=" + UPDATED_EMAIL_SMTP_USE_SSL);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpUseSSLIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpUseSSL is not null
        defaultEmailAccountShouldBeFound("emailSmtpUseSSL.specified=true");

        // Get all the emailAccountList where emailSmtpUseSSL is null
        defaultEmailAccountShouldNotBeFound("emailSmtpUseSSL.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpPortIsEqualToSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpPort equals to DEFAULT_EMAIL_SMTP_PORT
        defaultEmailAccountShouldBeFound("emailSmtpPort.equals=" + DEFAULT_EMAIL_SMTP_PORT);

        // Get all the emailAccountList where emailSmtpPort equals to UPDATED_EMAIL_SMTP_PORT
        defaultEmailAccountShouldNotBeFound("emailSmtpPort.equals=" + UPDATED_EMAIL_SMTP_PORT);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpPortIsInShouldWork() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpPort in DEFAULT_EMAIL_SMTP_PORT or UPDATED_EMAIL_SMTP_PORT
        defaultEmailAccountShouldBeFound("emailSmtpPort.in=" + DEFAULT_EMAIL_SMTP_PORT + "," + UPDATED_EMAIL_SMTP_PORT);

        // Get all the emailAccountList where emailSmtpPort equals to UPDATED_EMAIL_SMTP_PORT
        defaultEmailAccountShouldNotBeFound("emailSmtpPort.in=" + UPDATED_EMAIL_SMTP_PORT);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpPort is not null
        defaultEmailAccountShouldBeFound("emailSmtpPort.specified=true");

        // Get all the emailAccountList where emailSmtpPort is null
        defaultEmailAccountShouldNotBeFound("emailSmtpPort.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpPortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpPort greater than or equals to DEFAULT_EMAIL_SMTP_PORT
        defaultEmailAccountShouldBeFound("emailSmtpPort.greaterOrEqualThan=" + DEFAULT_EMAIL_SMTP_PORT);

        // Get all the emailAccountList where emailSmtpPort greater than or equals to UPDATED_EMAIL_SMTP_PORT
        defaultEmailAccountShouldNotBeFound("emailSmtpPort.greaterOrEqualThan=" + UPDATED_EMAIL_SMTP_PORT);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailSmtpPortIsLessThanSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailSmtpPort less than or equals to DEFAULT_EMAIL_SMTP_PORT
        defaultEmailAccountShouldNotBeFound("emailSmtpPort.lessThan=" + DEFAULT_EMAIL_SMTP_PORT);

        // Get all the emailAccountList where emailSmtpPort less than or equals to UPDATED_EMAIL_SMTP_PORT
        defaultEmailAccountShouldBeFound("emailSmtpPort.lessThan=" + UPDATED_EMAIL_SMTP_PORT);
    }


    @Test
    @Transactional
    public void getAllEmailAccountsByEmailAccountDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailAccountDescription equals to DEFAULT_EMAIL_ACCOUNT_DESCRIPTION
        defaultEmailAccountShouldBeFound("emailAccountDescription.equals=" + DEFAULT_EMAIL_ACCOUNT_DESCRIPTION);

        // Get all the emailAccountList where emailAccountDescription equals to UPDATED_EMAIL_ACCOUNT_DESCRIPTION
        defaultEmailAccountShouldNotBeFound("emailAccountDescription.equals=" + UPDATED_EMAIL_ACCOUNT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailAccountDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailAccountDescription in DEFAULT_EMAIL_ACCOUNT_DESCRIPTION or UPDATED_EMAIL_ACCOUNT_DESCRIPTION
        defaultEmailAccountShouldBeFound("emailAccountDescription.in=" + DEFAULT_EMAIL_ACCOUNT_DESCRIPTION + "," + UPDATED_EMAIL_ACCOUNT_DESCRIPTION);

        // Get all the emailAccountList where emailAccountDescription equals to UPDATED_EMAIL_ACCOUNT_DESCRIPTION
        defaultEmailAccountShouldNotBeFound("emailAccountDescription.in=" + UPDATED_EMAIL_ACCOUNT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailAccountDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailAccountDescription is not null
        defaultEmailAccountShouldBeFound("emailAccountDescription.specified=true");

        // Get all the emailAccountList where emailAccountDescription is null
        defaultEmailAccountShouldNotBeFound("emailAccountDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailAccountEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailAccountEnabled equals to DEFAULT_EMAIL_ACCOUNT_ENABLED
        defaultEmailAccountShouldBeFound("emailAccountEnabled.equals=" + DEFAULT_EMAIL_ACCOUNT_ENABLED);

        // Get all the emailAccountList where emailAccountEnabled equals to UPDATED_EMAIL_ACCOUNT_ENABLED
        defaultEmailAccountShouldNotBeFound("emailAccountEnabled.equals=" + UPDATED_EMAIL_ACCOUNT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailAccountEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailAccountEnabled in DEFAULT_EMAIL_ACCOUNT_ENABLED or UPDATED_EMAIL_ACCOUNT_ENABLED
        defaultEmailAccountShouldBeFound("emailAccountEnabled.in=" + DEFAULT_EMAIL_ACCOUNT_ENABLED + "," + UPDATED_EMAIL_ACCOUNT_ENABLED);

        // Get all the emailAccountList where emailAccountEnabled equals to UPDATED_EMAIL_ACCOUNT_ENABLED
        defaultEmailAccountShouldNotBeFound("emailAccountEnabled.in=" + UPDATED_EMAIL_ACCOUNT_ENABLED);
    }

    @Test
    @Transactional
    public void getAllEmailAccountsByEmailAccountEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailAccountRepository.saveAndFlush(emailAccount);

        // Get all the emailAccountList where emailAccountEnabled is not null
        defaultEmailAccountShouldBeFound("emailAccountEnabled.specified=true");

        // Get all the emailAccountList where emailAccountEnabled is null
        defaultEmailAccountShouldNotBeFound("emailAccountEnabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEmailAccountShouldBeFound(String filter) throws Exception {
        restEmailAccountMockMvc.perform(get("/api/email-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailAccountName").value(hasItem(DEFAULT_EMAIL_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpServer").value(hasItem(DEFAULT_EMAIL_SMTP_SERVER.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpUsername").value(hasItem(DEFAULT_EMAIL_SMTP_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpPassword").value(hasItem(DEFAULT_EMAIL_SMTP_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpUseSSL").value(hasItem(DEFAULT_EMAIL_SMTP_USE_SSL.booleanValue())))
            .andExpect(jsonPath("$.[*].emailSmtpPort").value(hasItem(DEFAULT_EMAIL_SMTP_PORT)))
            .andExpect(jsonPath("$.[*].emailAccountDescription").value(hasItem(DEFAULT_EMAIL_ACCOUNT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].emailAccountEnabled").value(hasItem(DEFAULT_EMAIL_ACCOUNT_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEmailAccountShouldNotBeFound(String filter) throws Exception {
        restEmailAccountMockMvc.perform(get("/api/email-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingEmailAccount() throws Exception {
        // Get the emailAccount
        restEmailAccountMockMvc.perform(get("/api/email-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmailAccount() throws Exception {
        // Initialize the database
        emailAccountService.save(emailAccount);

        int databaseSizeBeforeUpdate = emailAccountRepository.findAll().size();

        // Update the emailAccount
        EmailAccount updatedEmailAccount = emailAccountRepository.findOne(emailAccount.getId());
        updatedEmailAccount
            .emailAccountName(UPDATED_EMAIL_ACCOUNT_NAME)
            .emailSmtpServer(UPDATED_EMAIL_SMTP_SERVER)
            .emailSmtpUsername(UPDATED_EMAIL_SMTP_USERNAME)
            .emailSmtpPassword(UPDATED_EMAIL_SMTP_PASSWORD)
            .emailSmtpUseSSL(UPDATED_EMAIL_SMTP_USE_SSL)
            .emailSmtpPort(UPDATED_EMAIL_SMTP_PORT)
            .emailAccountDescription(UPDATED_EMAIL_ACCOUNT_DESCRIPTION)
            .emailAccountEnabled(UPDATED_EMAIL_ACCOUNT_ENABLED);

        restEmailAccountMockMvc.perform(put("/api/email-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmailAccount)))
            .andExpect(status().isOk());

        // Validate the EmailAccount in the database
        List<EmailAccount> emailAccountList = emailAccountRepository.findAll();
        assertThat(emailAccountList).hasSize(databaseSizeBeforeUpdate);
        EmailAccount testEmailAccount = emailAccountList.get(emailAccountList.size() - 1);
        assertThat(testEmailAccount.getEmailAccountName()).isEqualTo(UPDATED_EMAIL_ACCOUNT_NAME);
        assertThat(testEmailAccount.getEmailSmtpServer()).isEqualTo(UPDATED_EMAIL_SMTP_SERVER);
        assertThat(testEmailAccount.getEmailSmtpUsername()).isEqualTo(UPDATED_EMAIL_SMTP_USERNAME);
        assertThat(testEmailAccount.getEmailSmtpPassword()).isEqualTo(UPDATED_EMAIL_SMTP_PASSWORD);
        assertThat(testEmailAccount.isEmailSmtpUseSSL()).isEqualTo(UPDATED_EMAIL_SMTP_USE_SSL);
        assertThat(testEmailAccount.getEmailSmtpPort()).isEqualTo(UPDATED_EMAIL_SMTP_PORT);
        assertThat(testEmailAccount.getEmailAccountDescription()).isEqualTo(UPDATED_EMAIL_ACCOUNT_DESCRIPTION);
        assertThat(testEmailAccount.isEmailAccountEnabled()).isEqualTo(UPDATED_EMAIL_ACCOUNT_ENABLED);

        // Validate the EmailAccount in Elasticsearch
        EmailAccount emailAccountEs = emailAccountSearchRepository.findOne(testEmailAccount.getId());
        assertThat(emailAccountEs).isEqualToComparingFieldByField(testEmailAccount);
    }

    @Test
    @Transactional
    public void updateNonExistingEmailAccount() throws Exception {
        int databaseSizeBeforeUpdate = emailAccountRepository.findAll().size();

        // Create the EmailAccount

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmailAccountMockMvc.perform(put("/api/email-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailAccount)))
            .andExpect(status().isCreated());

        // Validate the EmailAccount in the database
        List<EmailAccount> emailAccountList = emailAccountRepository.findAll();
        assertThat(emailAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmailAccount() throws Exception {
        // Initialize the database
        emailAccountService.save(emailAccount);

        int databaseSizeBeforeDelete = emailAccountRepository.findAll().size();

        // Get the emailAccount
        restEmailAccountMockMvc.perform(delete("/api/email-accounts/{id}", emailAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean emailAccountExistsInEs = emailAccountSearchRepository.exists(emailAccount.getId());
        assertThat(emailAccountExistsInEs).isFalse();

        // Validate the database is empty
        List<EmailAccount> emailAccountList = emailAccountRepository.findAll();
        assertThat(emailAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmailAccount() throws Exception {
        // Initialize the database
        emailAccountService.save(emailAccount);

        // Search the emailAccount
        restEmailAccountMockMvc.perform(get("/api/_search/email-accounts?query=id:" + emailAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailAccountName").value(hasItem(DEFAULT_EMAIL_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpServer").value(hasItem(DEFAULT_EMAIL_SMTP_SERVER.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpUsername").value(hasItem(DEFAULT_EMAIL_SMTP_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpPassword").value(hasItem(DEFAULT_EMAIL_SMTP_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].emailSmtpUseSSL").value(hasItem(DEFAULT_EMAIL_SMTP_USE_SSL.booleanValue())))
            .andExpect(jsonPath("$.[*].emailSmtpPort").value(hasItem(DEFAULT_EMAIL_SMTP_PORT)))
            .andExpect(jsonPath("$.[*].emailAccountDescription").value(hasItem(DEFAULT_EMAIL_ACCOUNT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].emailAccountEnabled").value(hasItem(DEFAULT_EMAIL_ACCOUNT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailAccount.class);
        EmailAccount emailAccount1 = new EmailAccount();
        emailAccount1.setId(1L);
        EmailAccount emailAccount2 = new EmailAccount();
        emailAccount2.setId(emailAccount1.getId());
        assertThat(emailAccount1).isEqualTo(emailAccount2);
        emailAccount2.setId(2L);
        assertThat(emailAccount1).isNotEqualTo(emailAccount2);
        emailAccount1.setId(null);
        assertThat(emailAccount1).isNotEqualTo(emailAccount2);
    }
}
