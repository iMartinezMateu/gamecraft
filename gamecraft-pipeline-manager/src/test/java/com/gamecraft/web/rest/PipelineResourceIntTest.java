package com.gamecraft.web.rest;

import com.gamecraft.GamecraftpipelinemanagerApp;

import com.gamecraft.domain.Pipeline;
import com.gamecraft.repository.PipelineRepository;
import com.gamecraft.service.PipelineService;
import com.gamecraft.repository.search.PipelineSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.PipelineCriteria;
import com.gamecraft.service.PipelineQueryService;

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

import com.gamecraft.domain.enumeration.PipelineRepositoryType;
import com.gamecraft.domain.enumeration.PipelineNotificatorType;
/**
 * Test class for the PipelineResource REST controller.
 *
 * @see PipelineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftpipelinemanagerApp.class)
public class PipelineResourceIntTest {

    private static final String DEFAULT_PIPELINE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PIPELINE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PIPELINE_PROJECT_ID = 1L;
    private static final Long UPDATED_PIPELINE_PROJECT_ID = 2L;

    private static final String DEFAULT_PIPELINE_REPOSITORY_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_REPOSITORY_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PIPELINE_REPOSITORY_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_REPOSITORY_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PIPELINE_REPOSITORY_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_REPOSITORY_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PIPELINE_ENGINE_COMPILER_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_ENGINE_COMPILER_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_ENGINE_COMPILER_ARGUMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_PIPELINE_FTP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_FTP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PIPELINE_FTP_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_FTP_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PIPELINE_FTP_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_FTP_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PIPELINE_NOTIFICATOR_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_PIPELINE_NOTIFICATOR_DETAILS = "BBBBBBBBBB";

    private static final PipelineRepositoryType DEFAULT_PIPELINE_REPOSITORY_TYPE = PipelineRepositoryType.GITHUB;
    private static final PipelineRepositoryType UPDATED_PIPELINE_REPOSITORY_TYPE = PipelineRepositoryType.BITBUCKET;

    private static final PipelineNotificatorType DEFAULT_PIPELINE_NOTIFICATOR_TYPE = PipelineNotificatorType.EMAIL;
    private static final PipelineNotificatorType UPDATED_PIPELINE_NOTIFICATOR_TYPE = PipelineNotificatorType.TWITTER;

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private PipelineService pipelineService;

    @Autowired
    private PipelineSearchRepository pipelineSearchRepository;

    @Autowired
    private PipelineQueryService pipelineQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPipelineMockMvc;

    private Pipeline pipeline;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PipelineResource pipelineResource = new PipelineResource(pipelineService, pipelineQueryService);
        this.restPipelineMockMvc = MockMvcBuilders.standaloneSetup(pipelineResource)
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
    public static Pipeline createEntity(EntityManager em) {
        Pipeline pipeline = new Pipeline()
            .pipelineName(DEFAULT_PIPELINE_NAME)
            .pipelineDescription(DEFAULT_PIPELINE_DESCRIPTION)
            .pipelineProjectId(DEFAULT_PIPELINE_PROJECT_ID)
            .pipelineRepositoryAddress(DEFAULT_PIPELINE_REPOSITORY_ADDRESS)
            .pipelineRepositoryUsername(DEFAULT_PIPELINE_REPOSITORY_USERNAME)
            .pipelineRepositoryPassword(DEFAULT_PIPELINE_REPOSITORY_PASSWORD)
            .pipelineEngineCompilerPath(DEFAULT_PIPELINE_ENGINE_COMPILER_PATH)
            .pipelineEngineCompilerArguments(DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS)
            .pipelineFtpAddress(DEFAULT_PIPELINE_FTP_ADDRESS)
            .pipelineFtpUsername(DEFAULT_PIPELINE_FTP_USERNAME)
            .pipelineFtpPassword(DEFAULT_PIPELINE_FTP_PASSWORD)
            .pipelineNotificatorDetails(DEFAULT_PIPELINE_NOTIFICATOR_DETAILS)
            .pipelineRepositoryType(DEFAULT_PIPELINE_REPOSITORY_TYPE)
            .pipelineNotificatorType(DEFAULT_PIPELINE_NOTIFICATOR_TYPE);
        return pipeline;
    }

    @Before
    public void initTest() {
        pipelineSearchRepository.deleteAll();
        pipeline = createEntity(em);
    }

    @Test
    @Transactional
    public void createPipeline() throws Exception {
        int databaseSizeBeforeCreate = pipelineRepository.findAll().size();

        // Create the Pipeline
        restPipelineMockMvc.perform(post("/api/pipelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pipeline)))
            .andExpect(status().isCreated());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeCreate + 1);
        Pipeline testPipeline = pipelineList.get(pipelineList.size() - 1);
        assertThat(testPipeline.getPipelineName()).isEqualTo(DEFAULT_PIPELINE_NAME);
        assertThat(testPipeline.getPipelineDescription()).isEqualTo(DEFAULT_PIPELINE_DESCRIPTION);
        assertThat(testPipeline.getPipelineProjectId()).isEqualTo(DEFAULT_PIPELINE_PROJECT_ID);
        assertThat(testPipeline.getPipelineRepositoryAddress()).isEqualTo(DEFAULT_PIPELINE_REPOSITORY_ADDRESS);
        assertThat(testPipeline.getPipelineRepositoryUsername()).isEqualTo(DEFAULT_PIPELINE_REPOSITORY_USERNAME);
        assertThat(testPipeline.getPipelineRepositoryPassword()).isEqualTo(DEFAULT_PIPELINE_REPOSITORY_PASSWORD);
        assertThat(testPipeline.getPipelineEngineCompilerPath()).isEqualTo(DEFAULT_PIPELINE_ENGINE_COMPILER_PATH);
        assertThat(testPipeline.getPipelineEngineCompilerArguments()).isEqualTo(DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS);
        assertThat(testPipeline.getPipelineFtpAddress()).isEqualTo(DEFAULT_PIPELINE_FTP_ADDRESS);
        assertThat(testPipeline.getPipelineFtpUsername()).isEqualTo(DEFAULT_PIPELINE_FTP_USERNAME);
        assertThat(testPipeline.getPipelineFtpPassword()).isEqualTo(DEFAULT_PIPELINE_FTP_PASSWORD);
        assertThat(testPipeline.getPipelineNotificatorDetails()).isEqualTo(DEFAULT_PIPELINE_NOTIFICATOR_DETAILS);
        assertThat(testPipeline.getPipelineRepositoryType()).isEqualTo(DEFAULT_PIPELINE_REPOSITORY_TYPE);
        assertThat(testPipeline.getPipelineNotificatorType()).isEqualTo(DEFAULT_PIPELINE_NOTIFICATOR_TYPE);

        // Validate the Pipeline in Elasticsearch
        Pipeline pipelineEs = pipelineSearchRepository.findOne(testPipeline.getId());
        assertThat(pipelineEs).isEqualToIgnoringGivenFields(testPipeline);
    }

    @Test
    @Transactional
    public void createPipelineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pipelineRepository.findAll().size();

        // Create the Pipeline with an existing ID
        pipeline.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPipelineMockMvc.perform(post("/api/pipelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pipeline)))
            .andExpect(status().isBadRequest());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPipelineNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pipelineRepository.findAll().size();
        // set the field null
        pipeline.setPipelineName(null);

        // Create the Pipeline, which fails.

        restPipelineMockMvc.perform(post("/api/pipelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pipeline)))
            .andExpect(status().isBadRequest());

        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPipelineProjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pipelineRepository.findAll().size();
        // set the field null
        pipeline.setPipelineProjectId(null);

        // Create the Pipeline, which fails.

        restPipelineMockMvc.perform(post("/api/pipelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pipeline)))
            .andExpect(status().isBadRequest());

        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPipelines() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList
        restPipelineMockMvc.perform(get("/api/pipelines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipeline.getId().intValue())))
            .andExpect(jsonPath("$.[*].pipelineName").value(hasItem(DEFAULT_PIPELINE_NAME.toString())))
            .andExpect(jsonPath("$.[*].pipelineDescription").value(hasItem(DEFAULT_PIPELINE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pipelineProjectId").value(hasItem(DEFAULT_PIPELINE_PROJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryAddress").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryUsername").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryPassword").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].pipelineEngineCompilerPath").value(hasItem(DEFAULT_PIPELINE_ENGINE_COMPILER_PATH.toString())))
            .andExpect(jsonPath("$.[*].pipelineEngineCompilerArguments").value(hasItem(DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS.toString())))
            .andExpect(jsonPath("$.[*].pipelineFtpAddress").value(hasItem(DEFAULT_PIPELINE_FTP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].pipelineFtpUsername").value(hasItem(DEFAULT_PIPELINE_FTP_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].pipelineFtpPassword").value(hasItem(DEFAULT_PIPELINE_FTP_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].pipelineNotificatorDetails").value(hasItem(DEFAULT_PIPELINE_NOTIFICATOR_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryType").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pipelineNotificatorType").value(hasItem(DEFAULT_PIPELINE_NOTIFICATOR_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPipeline() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get the pipeline
        restPipelineMockMvc.perform(get("/api/pipelines/{id}", pipeline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pipeline.getId().intValue()))
            .andExpect(jsonPath("$.pipelineName").value(DEFAULT_PIPELINE_NAME.toString()))
            .andExpect(jsonPath("$.pipelineDescription").value(DEFAULT_PIPELINE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.pipelineProjectId").value(DEFAULT_PIPELINE_PROJECT_ID.intValue()))
            .andExpect(jsonPath("$.pipelineRepositoryAddress").value(DEFAULT_PIPELINE_REPOSITORY_ADDRESS.toString()))
            .andExpect(jsonPath("$.pipelineRepositoryUsername").value(DEFAULT_PIPELINE_REPOSITORY_USERNAME.toString()))
            .andExpect(jsonPath("$.pipelineRepositoryPassword").value(DEFAULT_PIPELINE_REPOSITORY_PASSWORD.toString()))
            .andExpect(jsonPath("$.pipelineEngineCompilerPath").value(DEFAULT_PIPELINE_ENGINE_COMPILER_PATH.toString()))
            .andExpect(jsonPath("$.pipelineEngineCompilerArguments").value(DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS.toString()))
            .andExpect(jsonPath("$.pipelineFtpAddress").value(DEFAULT_PIPELINE_FTP_ADDRESS.toString()))
            .andExpect(jsonPath("$.pipelineFtpUsername").value(DEFAULT_PIPELINE_FTP_USERNAME.toString()))
            .andExpect(jsonPath("$.pipelineFtpPassword").value(DEFAULT_PIPELINE_FTP_PASSWORD.toString()))
            .andExpect(jsonPath("$.pipelineNotificatorDetails").value(DEFAULT_PIPELINE_NOTIFICATOR_DETAILS.toString()))
            .andExpect(jsonPath("$.pipelineRepositoryType").value(DEFAULT_PIPELINE_REPOSITORY_TYPE.toString()))
            .andExpect(jsonPath("$.pipelineNotificatorType").value(DEFAULT_PIPELINE_NOTIFICATOR_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineName equals to DEFAULT_PIPELINE_NAME
        defaultPipelineShouldBeFound("pipelineName.equals=" + DEFAULT_PIPELINE_NAME);

        // Get all the pipelineList where pipelineName equals to UPDATED_PIPELINE_NAME
        defaultPipelineShouldNotBeFound("pipelineName.equals=" + UPDATED_PIPELINE_NAME);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineNameIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineName in DEFAULT_PIPELINE_NAME or UPDATED_PIPELINE_NAME
        defaultPipelineShouldBeFound("pipelineName.in=" + DEFAULT_PIPELINE_NAME + "," + UPDATED_PIPELINE_NAME);

        // Get all the pipelineList where pipelineName equals to UPDATED_PIPELINE_NAME
        defaultPipelineShouldNotBeFound("pipelineName.in=" + UPDATED_PIPELINE_NAME);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineName is not null
        defaultPipelineShouldBeFound("pipelineName.specified=true");

        // Get all the pipelineList where pipelineName is null
        defaultPipelineShouldNotBeFound("pipelineName.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineDescription equals to DEFAULT_PIPELINE_DESCRIPTION
        defaultPipelineShouldBeFound("pipelineDescription.equals=" + DEFAULT_PIPELINE_DESCRIPTION);

        // Get all the pipelineList where pipelineDescription equals to UPDATED_PIPELINE_DESCRIPTION
        defaultPipelineShouldNotBeFound("pipelineDescription.equals=" + UPDATED_PIPELINE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineDescription in DEFAULT_PIPELINE_DESCRIPTION or UPDATED_PIPELINE_DESCRIPTION
        defaultPipelineShouldBeFound("pipelineDescription.in=" + DEFAULT_PIPELINE_DESCRIPTION + "," + UPDATED_PIPELINE_DESCRIPTION);

        // Get all the pipelineList where pipelineDescription equals to UPDATED_PIPELINE_DESCRIPTION
        defaultPipelineShouldNotBeFound("pipelineDescription.in=" + UPDATED_PIPELINE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineDescription is not null
        defaultPipelineShouldBeFound("pipelineDescription.specified=true");

        // Get all the pipelineList where pipelineDescription is null
        defaultPipelineShouldNotBeFound("pipelineDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineProjectIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineProjectId equals to DEFAULT_PIPELINE_PROJECT_ID
        defaultPipelineShouldBeFound("pipelineProjectId.equals=" + DEFAULT_PIPELINE_PROJECT_ID);

        // Get all the pipelineList where pipelineProjectId equals to UPDATED_PIPELINE_PROJECT_ID
        defaultPipelineShouldNotBeFound("pipelineProjectId.equals=" + UPDATED_PIPELINE_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineProjectIdIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineProjectId in DEFAULT_PIPELINE_PROJECT_ID or UPDATED_PIPELINE_PROJECT_ID
        defaultPipelineShouldBeFound("pipelineProjectId.in=" + DEFAULT_PIPELINE_PROJECT_ID + "," + UPDATED_PIPELINE_PROJECT_ID);

        // Get all the pipelineList where pipelineProjectId equals to UPDATED_PIPELINE_PROJECT_ID
        defaultPipelineShouldNotBeFound("pipelineProjectId.in=" + UPDATED_PIPELINE_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineProjectIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineProjectId is not null
        defaultPipelineShouldBeFound("pipelineProjectId.specified=true");

        // Get all the pipelineList where pipelineProjectId is null
        defaultPipelineShouldNotBeFound("pipelineProjectId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineProjectIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineProjectId greater than or equals to DEFAULT_PIPELINE_PROJECT_ID
        defaultPipelineShouldBeFound("pipelineProjectId.greaterOrEqualThan=" + DEFAULT_PIPELINE_PROJECT_ID);

        // Get all the pipelineList where pipelineProjectId greater than or equals to UPDATED_PIPELINE_PROJECT_ID
        defaultPipelineShouldNotBeFound("pipelineProjectId.greaterOrEqualThan=" + UPDATED_PIPELINE_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineProjectIdIsLessThanSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineProjectId less than or equals to DEFAULT_PIPELINE_PROJECT_ID
        defaultPipelineShouldNotBeFound("pipelineProjectId.lessThan=" + DEFAULT_PIPELINE_PROJECT_ID);

        // Get all the pipelineList where pipelineProjectId less than or equals to UPDATED_PIPELINE_PROJECT_ID
        defaultPipelineShouldBeFound("pipelineProjectId.lessThan=" + UPDATED_PIPELINE_PROJECT_ID);
    }


    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryAddress equals to DEFAULT_PIPELINE_REPOSITORY_ADDRESS
        defaultPipelineShouldBeFound("pipelineRepositoryAddress.equals=" + DEFAULT_PIPELINE_REPOSITORY_ADDRESS);

        // Get all the pipelineList where pipelineRepositoryAddress equals to UPDATED_PIPELINE_REPOSITORY_ADDRESS
        defaultPipelineShouldNotBeFound("pipelineRepositoryAddress.equals=" + UPDATED_PIPELINE_REPOSITORY_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryAddressIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryAddress in DEFAULT_PIPELINE_REPOSITORY_ADDRESS or UPDATED_PIPELINE_REPOSITORY_ADDRESS
        defaultPipelineShouldBeFound("pipelineRepositoryAddress.in=" + DEFAULT_PIPELINE_REPOSITORY_ADDRESS + "," + UPDATED_PIPELINE_REPOSITORY_ADDRESS);

        // Get all the pipelineList where pipelineRepositoryAddress equals to UPDATED_PIPELINE_REPOSITORY_ADDRESS
        defaultPipelineShouldNotBeFound("pipelineRepositoryAddress.in=" + UPDATED_PIPELINE_REPOSITORY_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryAddress is not null
        defaultPipelineShouldBeFound("pipelineRepositoryAddress.specified=true");

        // Get all the pipelineList where pipelineRepositoryAddress is null
        defaultPipelineShouldNotBeFound("pipelineRepositoryAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryUsername equals to DEFAULT_PIPELINE_REPOSITORY_USERNAME
        defaultPipelineShouldBeFound("pipelineRepositoryUsername.equals=" + DEFAULT_PIPELINE_REPOSITORY_USERNAME);

        // Get all the pipelineList where pipelineRepositoryUsername equals to UPDATED_PIPELINE_REPOSITORY_USERNAME
        defaultPipelineShouldNotBeFound("pipelineRepositoryUsername.equals=" + UPDATED_PIPELINE_REPOSITORY_USERNAME);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryUsername in DEFAULT_PIPELINE_REPOSITORY_USERNAME or UPDATED_PIPELINE_REPOSITORY_USERNAME
        defaultPipelineShouldBeFound("pipelineRepositoryUsername.in=" + DEFAULT_PIPELINE_REPOSITORY_USERNAME + "," + UPDATED_PIPELINE_REPOSITORY_USERNAME);

        // Get all the pipelineList where pipelineRepositoryUsername equals to UPDATED_PIPELINE_REPOSITORY_USERNAME
        defaultPipelineShouldNotBeFound("pipelineRepositoryUsername.in=" + UPDATED_PIPELINE_REPOSITORY_USERNAME);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryUsername is not null
        defaultPipelineShouldBeFound("pipelineRepositoryUsername.specified=true");

        // Get all the pipelineList where pipelineRepositoryUsername is null
        defaultPipelineShouldNotBeFound("pipelineRepositoryUsername.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryPassword equals to DEFAULT_PIPELINE_REPOSITORY_PASSWORD
        defaultPipelineShouldBeFound("pipelineRepositoryPassword.equals=" + DEFAULT_PIPELINE_REPOSITORY_PASSWORD);

        // Get all the pipelineList where pipelineRepositoryPassword equals to UPDATED_PIPELINE_REPOSITORY_PASSWORD
        defaultPipelineShouldNotBeFound("pipelineRepositoryPassword.equals=" + UPDATED_PIPELINE_REPOSITORY_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryPassword in DEFAULT_PIPELINE_REPOSITORY_PASSWORD or UPDATED_PIPELINE_REPOSITORY_PASSWORD
        defaultPipelineShouldBeFound("pipelineRepositoryPassword.in=" + DEFAULT_PIPELINE_REPOSITORY_PASSWORD + "," + UPDATED_PIPELINE_REPOSITORY_PASSWORD);

        // Get all the pipelineList where pipelineRepositoryPassword equals to UPDATED_PIPELINE_REPOSITORY_PASSWORD
        defaultPipelineShouldNotBeFound("pipelineRepositoryPassword.in=" + UPDATED_PIPELINE_REPOSITORY_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryPassword is not null
        defaultPipelineShouldBeFound("pipelineRepositoryPassword.specified=true");

        // Get all the pipelineList where pipelineRepositoryPassword is null
        defaultPipelineShouldNotBeFound("pipelineRepositoryPassword.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineEngineCompilerPathIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineEngineCompilerPath equals to DEFAULT_PIPELINE_ENGINE_COMPILER_PATH
        defaultPipelineShouldBeFound("pipelineEngineCompilerPath.equals=" + DEFAULT_PIPELINE_ENGINE_COMPILER_PATH);

        // Get all the pipelineList where pipelineEngineCompilerPath equals to UPDATED_PIPELINE_ENGINE_COMPILER_PATH
        defaultPipelineShouldNotBeFound("pipelineEngineCompilerPath.equals=" + UPDATED_PIPELINE_ENGINE_COMPILER_PATH);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineEngineCompilerPathIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineEngineCompilerPath in DEFAULT_PIPELINE_ENGINE_COMPILER_PATH or UPDATED_PIPELINE_ENGINE_COMPILER_PATH
        defaultPipelineShouldBeFound("pipelineEngineCompilerPath.in=" + DEFAULT_PIPELINE_ENGINE_COMPILER_PATH + "," + UPDATED_PIPELINE_ENGINE_COMPILER_PATH);

        // Get all the pipelineList where pipelineEngineCompilerPath equals to UPDATED_PIPELINE_ENGINE_COMPILER_PATH
        defaultPipelineShouldNotBeFound("pipelineEngineCompilerPath.in=" + UPDATED_PIPELINE_ENGINE_COMPILER_PATH);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineEngineCompilerPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineEngineCompilerPath is not null
        defaultPipelineShouldBeFound("pipelineEngineCompilerPath.specified=true");

        // Get all the pipelineList where pipelineEngineCompilerPath is null
        defaultPipelineShouldNotBeFound("pipelineEngineCompilerPath.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineEngineCompilerArgumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineEngineCompilerArguments equals to DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS
        defaultPipelineShouldBeFound("pipelineEngineCompilerArguments.equals=" + DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS);

        // Get all the pipelineList where pipelineEngineCompilerArguments equals to UPDATED_PIPELINE_ENGINE_COMPILER_ARGUMENTS
        defaultPipelineShouldNotBeFound("pipelineEngineCompilerArguments.equals=" + UPDATED_PIPELINE_ENGINE_COMPILER_ARGUMENTS);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineEngineCompilerArgumentsIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineEngineCompilerArguments in DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS or UPDATED_PIPELINE_ENGINE_COMPILER_ARGUMENTS
        defaultPipelineShouldBeFound("pipelineEngineCompilerArguments.in=" + DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS + "," + UPDATED_PIPELINE_ENGINE_COMPILER_ARGUMENTS);

        // Get all the pipelineList where pipelineEngineCompilerArguments equals to UPDATED_PIPELINE_ENGINE_COMPILER_ARGUMENTS
        defaultPipelineShouldNotBeFound("pipelineEngineCompilerArguments.in=" + UPDATED_PIPELINE_ENGINE_COMPILER_ARGUMENTS);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineEngineCompilerArgumentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineEngineCompilerArguments is not null
        defaultPipelineShouldBeFound("pipelineEngineCompilerArguments.specified=true");

        // Get all the pipelineList where pipelineEngineCompilerArguments is null
        defaultPipelineShouldNotBeFound("pipelineEngineCompilerArguments.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineFtpAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineFtpAddress equals to DEFAULT_PIPELINE_FTP_ADDRESS
        defaultPipelineShouldBeFound("pipelineFtpAddress.equals=" + DEFAULT_PIPELINE_FTP_ADDRESS);

        // Get all the pipelineList where pipelineFtpAddress equals to UPDATED_PIPELINE_FTP_ADDRESS
        defaultPipelineShouldNotBeFound("pipelineFtpAddress.equals=" + UPDATED_PIPELINE_FTP_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineFtpAddressIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineFtpAddress in DEFAULT_PIPELINE_FTP_ADDRESS or UPDATED_PIPELINE_FTP_ADDRESS
        defaultPipelineShouldBeFound("pipelineFtpAddress.in=" + DEFAULT_PIPELINE_FTP_ADDRESS + "," + UPDATED_PIPELINE_FTP_ADDRESS);

        // Get all the pipelineList where pipelineFtpAddress equals to UPDATED_PIPELINE_FTP_ADDRESS
        defaultPipelineShouldNotBeFound("pipelineFtpAddress.in=" + UPDATED_PIPELINE_FTP_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineFtpAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineFtpAddress is not null
        defaultPipelineShouldBeFound("pipelineFtpAddress.specified=true");

        // Get all the pipelineList where pipelineFtpAddress is null
        defaultPipelineShouldNotBeFound("pipelineFtpAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineFtpUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineFtpUsername equals to DEFAULT_PIPELINE_FTP_USERNAME
        defaultPipelineShouldBeFound("pipelineFtpUsername.equals=" + DEFAULT_PIPELINE_FTP_USERNAME);

        // Get all the pipelineList where pipelineFtpUsername equals to UPDATED_PIPELINE_FTP_USERNAME
        defaultPipelineShouldNotBeFound("pipelineFtpUsername.equals=" + UPDATED_PIPELINE_FTP_USERNAME);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineFtpUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineFtpUsername in DEFAULT_PIPELINE_FTP_USERNAME or UPDATED_PIPELINE_FTP_USERNAME
        defaultPipelineShouldBeFound("pipelineFtpUsername.in=" + DEFAULT_PIPELINE_FTP_USERNAME + "," + UPDATED_PIPELINE_FTP_USERNAME);

        // Get all the pipelineList where pipelineFtpUsername equals to UPDATED_PIPELINE_FTP_USERNAME
        defaultPipelineShouldNotBeFound("pipelineFtpUsername.in=" + UPDATED_PIPELINE_FTP_USERNAME);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineFtpUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineFtpUsername is not null
        defaultPipelineShouldBeFound("pipelineFtpUsername.specified=true");

        // Get all the pipelineList where pipelineFtpUsername is null
        defaultPipelineShouldNotBeFound("pipelineFtpUsername.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineFtpPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineFtpPassword equals to DEFAULT_PIPELINE_FTP_PASSWORD
        defaultPipelineShouldBeFound("pipelineFtpPassword.equals=" + DEFAULT_PIPELINE_FTP_PASSWORD);

        // Get all the pipelineList where pipelineFtpPassword equals to UPDATED_PIPELINE_FTP_PASSWORD
        defaultPipelineShouldNotBeFound("pipelineFtpPassword.equals=" + UPDATED_PIPELINE_FTP_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineFtpPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineFtpPassword in DEFAULT_PIPELINE_FTP_PASSWORD or UPDATED_PIPELINE_FTP_PASSWORD
        defaultPipelineShouldBeFound("pipelineFtpPassword.in=" + DEFAULT_PIPELINE_FTP_PASSWORD + "," + UPDATED_PIPELINE_FTP_PASSWORD);

        // Get all the pipelineList where pipelineFtpPassword equals to UPDATED_PIPELINE_FTP_PASSWORD
        defaultPipelineShouldNotBeFound("pipelineFtpPassword.in=" + UPDATED_PIPELINE_FTP_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineFtpPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineFtpPassword is not null
        defaultPipelineShouldBeFound("pipelineFtpPassword.specified=true");

        // Get all the pipelineList where pipelineFtpPassword is null
        defaultPipelineShouldNotBeFound("pipelineFtpPassword.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineNotificatorDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineNotificatorDetails equals to DEFAULT_PIPELINE_NOTIFICATOR_DETAILS
        defaultPipelineShouldBeFound("pipelineNotificatorDetails.equals=" + DEFAULT_PIPELINE_NOTIFICATOR_DETAILS);

        // Get all the pipelineList where pipelineNotificatorDetails equals to UPDATED_PIPELINE_NOTIFICATOR_DETAILS
        defaultPipelineShouldNotBeFound("pipelineNotificatorDetails.equals=" + UPDATED_PIPELINE_NOTIFICATOR_DETAILS);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineNotificatorDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineNotificatorDetails in DEFAULT_PIPELINE_NOTIFICATOR_DETAILS or UPDATED_PIPELINE_NOTIFICATOR_DETAILS
        defaultPipelineShouldBeFound("pipelineNotificatorDetails.in=" + DEFAULT_PIPELINE_NOTIFICATOR_DETAILS + "," + UPDATED_PIPELINE_NOTIFICATOR_DETAILS);

        // Get all the pipelineList where pipelineNotificatorDetails equals to UPDATED_PIPELINE_NOTIFICATOR_DETAILS
        defaultPipelineShouldNotBeFound("pipelineNotificatorDetails.in=" + UPDATED_PIPELINE_NOTIFICATOR_DETAILS);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineNotificatorDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineNotificatorDetails is not null
        defaultPipelineShouldBeFound("pipelineNotificatorDetails.specified=true");

        // Get all the pipelineList where pipelineNotificatorDetails is null
        defaultPipelineShouldNotBeFound("pipelineNotificatorDetails.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryType equals to DEFAULT_PIPELINE_REPOSITORY_TYPE
        defaultPipelineShouldBeFound("pipelineRepositoryType.equals=" + DEFAULT_PIPELINE_REPOSITORY_TYPE);

        // Get all the pipelineList where pipelineRepositoryType equals to UPDATED_PIPELINE_REPOSITORY_TYPE
        defaultPipelineShouldNotBeFound("pipelineRepositoryType.equals=" + UPDATED_PIPELINE_REPOSITORY_TYPE);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryType in DEFAULT_PIPELINE_REPOSITORY_TYPE or UPDATED_PIPELINE_REPOSITORY_TYPE
        defaultPipelineShouldBeFound("pipelineRepositoryType.in=" + DEFAULT_PIPELINE_REPOSITORY_TYPE + "," + UPDATED_PIPELINE_REPOSITORY_TYPE);

        // Get all the pipelineList where pipelineRepositoryType equals to UPDATED_PIPELINE_REPOSITORY_TYPE
        defaultPipelineShouldNotBeFound("pipelineRepositoryType.in=" + UPDATED_PIPELINE_REPOSITORY_TYPE);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineRepositoryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineRepositoryType is not null
        defaultPipelineShouldBeFound("pipelineRepositoryType.specified=true");

        // Get all the pipelineList where pipelineRepositoryType is null
        defaultPipelineShouldNotBeFound("pipelineRepositoryType.specified=false");
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineNotificatorTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineNotificatorType equals to DEFAULT_PIPELINE_NOTIFICATOR_TYPE
        defaultPipelineShouldBeFound("pipelineNotificatorType.equals=" + DEFAULT_PIPELINE_NOTIFICATOR_TYPE);

        // Get all the pipelineList where pipelineNotificatorType equals to UPDATED_PIPELINE_NOTIFICATOR_TYPE
        defaultPipelineShouldNotBeFound("pipelineNotificatorType.equals=" + UPDATED_PIPELINE_NOTIFICATOR_TYPE);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineNotificatorTypeIsInShouldWork() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineNotificatorType in DEFAULT_PIPELINE_NOTIFICATOR_TYPE or UPDATED_PIPELINE_NOTIFICATOR_TYPE
        defaultPipelineShouldBeFound("pipelineNotificatorType.in=" + DEFAULT_PIPELINE_NOTIFICATOR_TYPE + "," + UPDATED_PIPELINE_NOTIFICATOR_TYPE);

        // Get all the pipelineList where pipelineNotificatorType equals to UPDATED_PIPELINE_NOTIFICATOR_TYPE
        defaultPipelineShouldNotBeFound("pipelineNotificatorType.in=" + UPDATED_PIPELINE_NOTIFICATOR_TYPE);
    }

    @Test
    @Transactional
    public void getAllPipelinesByPipelineNotificatorTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pipelineRepository.saveAndFlush(pipeline);

        // Get all the pipelineList where pipelineNotificatorType is not null
        defaultPipelineShouldBeFound("pipelineNotificatorType.specified=true");

        // Get all the pipelineList where pipelineNotificatorType is null
        defaultPipelineShouldNotBeFound("pipelineNotificatorType.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPipelineShouldBeFound(String filter) throws Exception {
        restPipelineMockMvc.perform(get("/api/pipelines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipeline.getId().intValue())))
            .andExpect(jsonPath("$.[*].pipelineName").value(hasItem(DEFAULT_PIPELINE_NAME.toString())))
            .andExpect(jsonPath("$.[*].pipelineDescription").value(hasItem(DEFAULT_PIPELINE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pipelineProjectId").value(hasItem(DEFAULT_PIPELINE_PROJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryAddress").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryUsername").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryPassword").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].pipelineEngineCompilerPath").value(hasItem(DEFAULT_PIPELINE_ENGINE_COMPILER_PATH.toString())))
            .andExpect(jsonPath("$.[*].pipelineEngineCompilerArguments").value(hasItem(DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS.toString())))
            .andExpect(jsonPath("$.[*].pipelineFtpAddress").value(hasItem(DEFAULT_PIPELINE_FTP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].pipelineFtpUsername").value(hasItem(DEFAULT_PIPELINE_FTP_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].pipelineFtpPassword").value(hasItem(DEFAULT_PIPELINE_FTP_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].pipelineNotificatorDetails").value(hasItem(DEFAULT_PIPELINE_NOTIFICATOR_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryType").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pipelineNotificatorType").value(hasItem(DEFAULT_PIPELINE_NOTIFICATOR_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPipelineShouldNotBeFound(String filter) throws Exception {
        restPipelineMockMvc.perform(get("/api/pipelines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPipeline() throws Exception {
        // Get the pipeline
        restPipelineMockMvc.perform(get("/api/pipelines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePipeline() throws Exception {
        // Initialize the database
        pipelineService.save(pipeline);

        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();

        // Update the pipeline
        Pipeline updatedPipeline = pipelineRepository.findOne(pipeline.getId());
        // Disconnect from session so that the updates on updatedPipeline are not directly saved in db
        em.detach(updatedPipeline);
        updatedPipeline
            .pipelineName(UPDATED_PIPELINE_NAME)
            .pipelineDescription(UPDATED_PIPELINE_DESCRIPTION)
            .pipelineProjectId(UPDATED_PIPELINE_PROJECT_ID)
            .pipelineRepositoryAddress(UPDATED_PIPELINE_REPOSITORY_ADDRESS)
            .pipelineRepositoryUsername(UPDATED_PIPELINE_REPOSITORY_USERNAME)
            .pipelineRepositoryPassword(UPDATED_PIPELINE_REPOSITORY_PASSWORD)
            .pipelineEngineCompilerPath(UPDATED_PIPELINE_ENGINE_COMPILER_PATH)
            .pipelineEngineCompilerArguments(UPDATED_PIPELINE_ENGINE_COMPILER_ARGUMENTS)
            .pipelineFtpAddress(UPDATED_PIPELINE_FTP_ADDRESS)
            .pipelineFtpUsername(UPDATED_PIPELINE_FTP_USERNAME)
            .pipelineFtpPassword(UPDATED_PIPELINE_FTP_PASSWORD)
            .pipelineNotificatorDetails(UPDATED_PIPELINE_NOTIFICATOR_DETAILS)
            .pipelineRepositoryType(UPDATED_PIPELINE_REPOSITORY_TYPE)
            .pipelineNotificatorType(UPDATED_PIPELINE_NOTIFICATOR_TYPE);

        restPipelineMockMvc.perform(put("/api/pipelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPipeline)))
            .andExpect(status().isOk());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate);
        Pipeline testPipeline = pipelineList.get(pipelineList.size() - 1);
        assertThat(testPipeline.getPipelineName()).isEqualTo(UPDATED_PIPELINE_NAME);
        assertThat(testPipeline.getPipelineDescription()).isEqualTo(UPDATED_PIPELINE_DESCRIPTION);
        assertThat(testPipeline.getPipelineProjectId()).isEqualTo(UPDATED_PIPELINE_PROJECT_ID);
        assertThat(testPipeline.getPipelineRepositoryAddress()).isEqualTo(UPDATED_PIPELINE_REPOSITORY_ADDRESS);
        assertThat(testPipeline.getPipelineRepositoryUsername()).isEqualTo(UPDATED_PIPELINE_REPOSITORY_USERNAME);
        assertThat(testPipeline.getPipelineRepositoryPassword()).isEqualTo(UPDATED_PIPELINE_REPOSITORY_PASSWORD);
        assertThat(testPipeline.getPipelineEngineCompilerPath()).isEqualTo(UPDATED_PIPELINE_ENGINE_COMPILER_PATH);
        assertThat(testPipeline.getPipelineEngineCompilerArguments()).isEqualTo(UPDATED_PIPELINE_ENGINE_COMPILER_ARGUMENTS);
        assertThat(testPipeline.getPipelineFtpAddress()).isEqualTo(UPDATED_PIPELINE_FTP_ADDRESS);
        assertThat(testPipeline.getPipelineFtpUsername()).isEqualTo(UPDATED_PIPELINE_FTP_USERNAME);
        assertThat(testPipeline.getPipelineFtpPassword()).isEqualTo(UPDATED_PIPELINE_FTP_PASSWORD);
        assertThat(testPipeline.getPipelineNotificatorDetails()).isEqualTo(UPDATED_PIPELINE_NOTIFICATOR_DETAILS);
        assertThat(testPipeline.getPipelineRepositoryType()).isEqualTo(UPDATED_PIPELINE_REPOSITORY_TYPE);
        assertThat(testPipeline.getPipelineNotificatorType()).isEqualTo(UPDATED_PIPELINE_NOTIFICATOR_TYPE);

        // Validate the Pipeline in Elasticsearch
        Pipeline pipelineEs = pipelineSearchRepository.findOne(testPipeline.getId());
        assertThat(pipelineEs).isEqualToIgnoringGivenFields(testPipeline);
    }

    @Test
    @Transactional
    public void updateNonExistingPipeline() throws Exception {
        int databaseSizeBeforeUpdate = pipelineRepository.findAll().size();

        // Create the Pipeline

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPipelineMockMvc.perform(put("/api/pipelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pipeline)))
            .andExpect(status().isCreated());

        // Validate the Pipeline in the database
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePipeline() throws Exception {
        // Initialize the database
        pipelineService.save(pipeline);

        int databaseSizeBeforeDelete = pipelineRepository.findAll().size();

        // Get the pipeline
        restPipelineMockMvc.perform(delete("/api/pipelines/{id}", pipeline.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pipelineExistsInEs = pipelineSearchRepository.exists(pipeline.getId());
        assertThat(pipelineExistsInEs).isFalse();

        // Validate the database is empty
        List<Pipeline> pipelineList = pipelineRepository.findAll();
        assertThat(pipelineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPipeline() throws Exception {
        // Initialize the database
        pipelineService.save(pipeline);

        // Search the pipeline
        restPipelineMockMvc.perform(get("/api/_search/pipelines?query=id:" + pipeline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipeline.getId().intValue())))
            .andExpect(jsonPath("$.[*].pipelineName").value(hasItem(DEFAULT_PIPELINE_NAME.toString())))
            .andExpect(jsonPath("$.[*].pipelineDescription").value(hasItem(DEFAULT_PIPELINE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pipelineProjectId").value(hasItem(DEFAULT_PIPELINE_PROJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryAddress").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryUsername").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryPassword").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].pipelineEngineCompilerPath").value(hasItem(DEFAULT_PIPELINE_ENGINE_COMPILER_PATH.toString())))
            .andExpect(jsonPath("$.[*].pipelineEngineCompilerArguments").value(hasItem(DEFAULT_PIPELINE_ENGINE_COMPILER_ARGUMENTS.toString())))
            .andExpect(jsonPath("$.[*].pipelineFtpAddress").value(hasItem(DEFAULT_PIPELINE_FTP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].pipelineFtpUsername").value(hasItem(DEFAULT_PIPELINE_FTP_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].pipelineFtpPassword").value(hasItem(DEFAULT_PIPELINE_FTP_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].pipelineNotificatorDetails").value(hasItem(DEFAULT_PIPELINE_NOTIFICATOR_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].pipelineRepositoryType").value(hasItem(DEFAULT_PIPELINE_REPOSITORY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pipelineNotificatorType").value(hasItem(DEFAULT_PIPELINE_NOTIFICATOR_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pipeline.class);
        Pipeline pipeline1 = new Pipeline();
        pipeline1.setId(1L);
        Pipeline pipeline2 = new Pipeline();
        pipeline2.setId(pipeline1.getId());
        assertThat(pipeline1).isEqualTo(pipeline2);
        pipeline2.setId(2L);
        assertThat(pipeline1).isNotEqualTo(pipeline2);
        pipeline1.setId(null);
        assertThat(pipeline1).isNotEqualTo(pipeline2);
    }
}
