package com.gamecraft.web.rest;

import com.gamecraft.GamecraftsonarmanagerApp;

import com.gamecraft.domain.SonarInstance;
import com.gamecraft.repository.SonarInstanceRepository;
import com.gamecraft.service.SonarInstanceService;
import com.gamecraft.repository.search.SonarInstanceSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.SonarInstanceCriteria;
import com.gamecraft.service.SonarInstanceQueryService;

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
 * Test class for the SonarInstanceResource REST controller.
 *
 * @see SonarInstanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftsonarmanagerApp.class)
public class SonarInstanceResourceIntTest {

    private static final String DEFAULT_SONAR_INSTANCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SONAR_INSTANCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SONAR_INSTANCE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SONAR_INSTANCE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SONAR_INSTANCE_RUNNER_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SONAR_INSTANCE_RUNNER_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SONAR_INSTANCE_ENABLED = false;
    private static final Boolean UPDATED_SONAR_INSTANCE_ENABLED = true;

    @Autowired
    private SonarInstanceRepository sonarInstanceRepository;

    @Autowired
    private SonarInstanceService sonarInstanceService;

    @Autowired
    private SonarInstanceSearchRepository sonarInstanceSearchRepository;

    @Autowired
    private SonarInstanceQueryService sonarInstanceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSonarInstanceMockMvc;

    private SonarInstance sonarInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SonarInstanceResource sonarInstanceResource = new SonarInstanceResource(sonarInstanceService, sonarInstanceQueryService);
        this.restSonarInstanceMockMvc = MockMvcBuilders.standaloneSetup(sonarInstanceResource)
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
    public static SonarInstance createEntity(EntityManager em) {
        SonarInstance sonarInstance = new SonarInstance()
            .sonarInstanceName(DEFAULT_SONAR_INSTANCE_NAME)
            .sonarInstanceDescription(DEFAULT_SONAR_INSTANCE_DESCRIPTION)
            .sonarInstanceRunnerPath(DEFAULT_SONAR_INSTANCE_RUNNER_PATH)
            .sonarInstanceEnabled(DEFAULT_SONAR_INSTANCE_ENABLED);
        return sonarInstance;
    }

    @Before
    public void initTest() {
        sonarInstanceSearchRepository.deleteAll();
        sonarInstance = createEntity(em);
    }

    @Test
    @Transactional
    public void createSonarInstance() throws Exception {
        int databaseSizeBeforeCreate = sonarInstanceRepository.findAll().size();

        // Create the SonarInstance
        restSonarInstanceMockMvc.perform(post("/api/sonar-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sonarInstance)))
            .andExpect(status().isCreated());

        // Validate the SonarInstance in the database
        List<SonarInstance> sonarInstanceList = sonarInstanceRepository.findAll();
        assertThat(sonarInstanceList).hasSize(databaseSizeBeforeCreate + 1);
        SonarInstance testSonarInstance = sonarInstanceList.get(sonarInstanceList.size() - 1);
        assertThat(testSonarInstance.getSonarInstanceName()).isEqualTo(DEFAULT_SONAR_INSTANCE_NAME);
        assertThat(testSonarInstance.getSonarInstanceDescription()).isEqualTo(DEFAULT_SONAR_INSTANCE_DESCRIPTION);
        assertThat(testSonarInstance.getSonarInstanceRunnerPath()).isEqualTo(DEFAULT_SONAR_INSTANCE_RUNNER_PATH);
        assertThat(testSonarInstance.isSonarInstanceEnabled()).isEqualTo(DEFAULT_SONAR_INSTANCE_ENABLED);

        // Validate the SonarInstance in Elasticsearch
        SonarInstance sonarInstanceEs = sonarInstanceSearchRepository.findOne(testSonarInstance.getId());
        assertThat(sonarInstanceEs).isEqualToComparingFieldByField(testSonarInstance);
    }

    @Test
    @Transactional
    public void createSonarInstanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sonarInstanceRepository.findAll().size();

        // Create the SonarInstance with an existing ID
        sonarInstance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSonarInstanceMockMvc.perform(post("/api/sonar-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sonarInstance)))
            .andExpect(status().isBadRequest());

        // Validate the SonarInstance in the database
        List<SonarInstance> sonarInstanceList = sonarInstanceRepository.findAll();
        assertThat(sonarInstanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSonarInstanceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sonarInstanceRepository.findAll().size();
        // set the field null
        sonarInstance.setSonarInstanceName(null);

        // Create the SonarInstance, which fails.

        restSonarInstanceMockMvc.perform(post("/api/sonar-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sonarInstance)))
            .andExpect(status().isBadRequest());

        List<SonarInstance> sonarInstanceList = sonarInstanceRepository.findAll();
        assertThat(sonarInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSonarInstanceEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = sonarInstanceRepository.findAll().size();
        // set the field null
        sonarInstance.setSonarInstanceEnabled(null);

        // Create the SonarInstance, which fails.

        restSonarInstanceMockMvc.perform(post("/api/sonar-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sonarInstance)))
            .andExpect(status().isBadRequest());

        List<SonarInstance> sonarInstanceList = sonarInstanceRepository.findAll();
        assertThat(sonarInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSonarInstances() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList
        restSonarInstanceMockMvc.perform(get("/api/sonar-instances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sonarInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].sonarInstanceName").value(hasItem(DEFAULT_SONAR_INSTANCE_NAME.toString())))
            .andExpect(jsonPath("$.[*].sonarInstanceDescription").value(hasItem(DEFAULT_SONAR_INSTANCE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].sonarInstanceRunnerPath").value(hasItem(DEFAULT_SONAR_INSTANCE_RUNNER_PATH.toString())))
            .andExpect(jsonPath("$.[*].sonarInstanceEnabled").value(hasItem(DEFAULT_SONAR_INSTANCE_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getSonarInstance() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get the sonarInstance
        restSonarInstanceMockMvc.perform(get("/api/sonar-instances/{id}", sonarInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sonarInstance.getId().intValue()))
            .andExpect(jsonPath("$.sonarInstanceName").value(DEFAULT_SONAR_INSTANCE_NAME.toString()))
            .andExpect(jsonPath("$.sonarInstanceDescription").value(DEFAULT_SONAR_INSTANCE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.sonarInstanceRunnerPath").value(DEFAULT_SONAR_INSTANCE_RUNNER_PATH.toString()))
            .andExpect(jsonPath("$.sonarInstanceEnabled").value(DEFAULT_SONAR_INSTANCE_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceName equals to DEFAULT_SONAR_INSTANCE_NAME
        defaultSonarInstanceShouldBeFound("sonarInstanceName.equals=" + DEFAULT_SONAR_INSTANCE_NAME);

        // Get all the sonarInstanceList where sonarInstanceName equals to UPDATED_SONAR_INSTANCE_NAME
        defaultSonarInstanceShouldNotBeFound("sonarInstanceName.equals=" + UPDATED_SONAR_INSTANCE_NAME);
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceNameIsInShouldWork() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceName in DEFAULT_SONAR_INSTANCE_NAME or UPDATED_SONAR_INSTANCE_NAME
        defaultSonarInstanceShouldBeFound("sonarInstanceName.in=" + DEFAULT_SONAR_INSTANCE_NAME + "," + UPDATED_SONAR_INSTANCE_NAME);

        // Get all the sonarInstanceList where sonarInstanceName equals to UPDATED_SONAR_INSTANCE_NAME
        defaultSonarInstanceShouldNotBeFound("sonarInstanceName.in=" + UPDATED_SONAR_INSTANCE_NAME);
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceName is not null
        defaultSonarInstanceShouldBeFound("sonarInstanceName.specified=true");

        // Get all the sonarInstanceList where sonarInstanceName is null
        defaultSonarInstanceShouldNotBeFound("sonarInstanceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceDescription equals to DEFAULT_SONAR_INSTANCE_DESCRIPTION
        defaultSonarInstanceShouldBeFound("sonarInstanceDescription.equals=" + DEFAULT_SONAR_INSTANCE_DESCRIPTION);

        // Get all the sonarInstanceList where sonarInstanceDescription equals to UPDATED_SONAR_INSTANCE_DESCRIPTION
        defaultSonarInstanceShouldNotBeFound("sonarInstanceDescription.equals=" + UPDATED_SONAR_INSTANCE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceDescription in DEFAULT_SONAR_INSTANCE_DESCRIPTION or UPDATED_SONAR_INSTANCE_DESCRIPTION
        defaultSonarInstanceShouldBeFound("sonarInstanceDescription.in=" + DEFAULT_SONAR_INSTANCE_DESCRIPTION + "," + UPDATED_SONAR_INSTANCE_DESCRIPTION);

        // Get all the sonarInstanceList where sonarInstanceDescription equals to UPDATED_SONAR_INSTANCE_DESCRIPTION
        defaultSonarInstanceShouldNotBeFound("sonarInstanceDescription.in=" + UPDATED_SONAR_INSTANCE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceDescription is not null
        defaultSonarInstanceShouldBeFound("sonarInstanceDescription.specified=true");

        // Get all the sonarInstanceList where sonarInstanceDescription is null
        defaultSonarInstanceShouldNotBeFound("sonarInstanceDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceRunnerPathIsEqualToSomething() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceRunnerPath equals to DEFAULT_SONAR_INSTANCE_RUNNER_PATH
        defaultSonarInstanceShouldBeFound("sonarInstanceRunnerPath.equals=" + DEFAULT_SONAR_INSTANCE_RUNNER_PATH);

        // Get all the sonarInstanceList where sonarInstanceRunnerPath equals to UPDATED_SONAR_INSTANCE_RUNNER_PATH
        defaultSonarInstanceShouldNotBeFound("sonarInstanceRunnerPath.equals=" + UPDATED_SONAR_INSTANCE_RUNNER_PATH);
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceRunnerPathIsInShouldWork() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceRunnerPath in DEFAULT_SONAR_INSTANCE_RUNNER_PATH or UPDATED_SONAR_INSTANCE_RUNNER_PATH
        defaultSonarInstanceShouldBeFound("sonarInstanceRunnerPath.in=" + DEFAULT_SONAR_INSTANCE_RUNNER_PATH + "," + UPDATED_SONAR_INSTANCE_RUNNER_PATH);

        // Get all the sonarInstanceList where sonarInstanceRunnerPath equals to UPDATED_SONAR_INSTANCE_RUNNER_PATH
        defaultSonarInstanceShouldNotBeFound("sonarInstanceRunnerPath.in=" + UPDATED_SONAR_INSTANCE_RUNNER_PATH);
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceRunnerPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceRunnerPath is not null
        defaultSonarInstanceShouldBeFound("sonarInstanceRunnerPath.specified=true");

        // Get all the sonarInstanceList where sonarInstanceRunnerPath is null
        defaultSonarInstanceShouldNotBeFound("sonarInstanceRunnerPath.specified=false");
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceEnabled equals to DEFAULT_SONAR_INSTANCE_ENABLED
        defaultSonarInstanceShouldBeFound("sonarInstanceEnabled.equals=" + DEFAULT_SONAR_INSTANCE_ENABLED);

        // Get all the sonarInstanceList where sonarInstanceEnabled equals to UPDATED_SONAR_INSTANCE_ENABLED
        defaultSonarInstanceShouldNotBeFound("sonarInstanceEnabled.equals=" + UPDATED_SONAR_INSTANCE_ENABLED);
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceEnabled in DEFAULT_SONAR_INSTANCE_ENABLED or UPDATED_SONAR_INSTANCE_ENABLED
        defaultSonarInstanceShouldBeFound("sonarInstanceEnabled.in=" + DEFAULT_SONAR_INSTANCE_ENABLED + "," + UPDATED_SONAR_INSTANCE_ENABLED);

        // Get all the sonarInstanceList where sonarInstanceEnabled equals to UPDATED_SONAR_INSTANCE_ENABLED
        defaultSonarInstanceShouldNotBeFound("sonarInstanceEnabled.in=" + UPDATED_SONAR_INSTANCE_ENABLED);
    }

    @Test
    @Transactional
    public void getAllSonarInstancesBySonarInstanceEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        sonarInstanceRepository.saveAndFlush(sonarInstance);

        // Get all the sonarInstanceList where sonarInstanceEnabled is not null
        defaultSonarInstanceShouldBeFound("sonarInstanceEnabled.specified=true");

        // Get all the sonarInstanceList where sonarInstanceEnabled is null
        defaultSonarInstanceShouldNotBeFound("sonarInstanceEnabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSonarInstanceShouldBeFound(String filter) throws Exception {
        restSonarInstanceMockMvc.perform(get("/api/sonar-instances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sonarInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].sonarInstanceName").value(hasItem(DEFAULT_SONAR_INSTANCE_NAME.toString())))
            .andExpect(jsonPath("$.[*].sonarInstanceDescription").value(hasItem(DEFAULT_SONAR_INSTANCE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].sonarInstanceRunnerPath").value(hasItem(DEFAULT_SONAR_INSTANCE_RUNNER_PATH.toString())))
            .andExpect(jsonPath("$.[*].sonarInstanceEnabled").value(hasItem(DEFAULT_SONAR_INSTANCE_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSonarInstanceShouldNotBeFound(String filter) throws Exception {
        restSonarInstanceMockMvc.perform(get("/api/sonar-instances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSonarInstance() throws Exception {
        // Get the sonarInstance
        restSonarInstanceMockMvc.perform(get("/api/sonar-instances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSonarInstance() throws Exception {
        // Initialize the database
        sonarInstanceService.save(sonarInstance);

        int databaseSizeBeforeUpdate = sonarInstanceRepository.findAll().size();

        // Update the sonarInstance
        SonarInstance updatedSonarInstance = sonarInstanceRepository.findOne(sonarInstance.getId());
        updatedSonarInstance
            .sonarInstanceName(UPDATED_SONAR_INSTANCE_NAME)
            .sonarInstanceDescription(UPDATED_SONAR_INSTANCE_DESCRIPTION)
            .sonarInstanceRunnerPath(UPDATED_SONAR_INSTANCE_RUNNER_PATH)
            .sonarInstanceEnabled(UPDATED_SONAR_INSTANCE_ENABLED);

        restSonarInstanceMockMvc.perform(put("/api/sonar-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSonarInstance)))
            .andExpect(status().isOk());

        // Validate the SonarInstance in the database
        List<SonarInstance> sonarInstanceList = sonarInstanceRepository.findAll();
        assertThat(sonarInstanceList).hasSize(databaseSizeBeforeUpdate);
        SonarInstance testSonarInstance = sonarInstanceList.get(sonarInstanceList.size() - 1);
        assertThat(testSonarInstance.getSonarInstanceName()).isEqualTo(UPDATED_SONAR_INSTANCE_NAME);
        assertThat(testSonarInstance.getSonarInstanceDescription()).isEqualTo(UPDATED_SONAR_INSTANCE_DESCRIPTION);
        assertThat(testSonarInstance.getSonarInstanceRunnerPath()).isEqualTo(UPDATED_SONAR_INSTANCE_RUNNER_PATH);
        assertThat(testSonarInstance.isSonarInstanceEnabled()).isEqualTo(UPDATED_SONAR_INSTANCE_ENABLED);

        // Validate the SonarInstance in Elasticsearch
        SonarInstance sonarInstanceEs = sonarInstanceSearchRepository.findOne(testSonarInstance.getId());
        assertThat(sonarInstanceEs).isEqualToComparingFieldByField(testSonarInstance);
    }

    @Test
    @Transactional
    public void updateNonExistingSonarInstance() throws Exception {
        int databaseSizeBeforeUpdate = sonarInstanceRepository.findAll().size();

        // Create the SonarInstance

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSonarInstanceMockMvc.perform(put("/api/sonar-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sonarInstance)))
            .andExpect(status().isCreated());

        // Validate the SonarInstance in the database
        List<SonarInstance> sonarInstanceList = sonarInstanceRepository.findAll();
        assertThat(sonarInstanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSonarInstance() throws Exception {
        // Initialize the database
        sonarInstanceService.save(sonarInstance);

        int databaseSizeBeforeDelete = sonarInstanceRepository.findAll().size();

        // Get the sonarInstance
        restSonarInstanceMockMvc.perform(delete("/api/sonar-instances/{id}", sonarInstance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sonarInstanceExistsInEs = sonarInstanceSearchRepository.exists(sonarInstance.getId());
        assertThat(sonarInstanceExistsInEs).isFalse();

        // Validate the database is empty
        List<SonarInstance> sonarInstanceList = sonarInstanceRepository.findAll();
        assertThat(sonarInstanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSonarInstance() throws Exception {
        // Initialize the database
        sonarInstanceService.save(sonarInstance);

        // Search the sonarInstance
        restSonarInstanceMockMvc.perform(get("/api/_search/sonar-instances?query=id:" + sonarInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sonarInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].sonarInstanceName").value(hasItem(DEFAULT_SONAR_INSTANCE_NAME.toString())))
            .andExpect(jsonPath("$.[*].sonarInstanceDescription").value(hasItem(DEFAULT_SONAR_INSTANCE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].sonarInstanceRunnerPath").value(hasItem(DEFAULT_SONAR_INSTANCE_RUNNER_PATH.toString())))
            .andExpect(jsonPath("$.[*].sonarInstanceEnabled").value(hasItem(DEFAULT_SONAR_INSTANCE_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SonarInstance.class);
        SonarInstance sonarInstance1 = new SonarInstance();
        sonarInstance1.setId(1L);
        SonarInstance sonarInstance2 = new SonarInstance();
        sonarInstance2.setId(sonarInstance1.getId());
        assertThat(sonarInstance1).isEqualTo(sonarInstance2);
        sonarInstance2.setId(2L);
        assertThat(sonarInstance1).isNotEqualTo(sonarInstance2);
        sonarInstance1.setId(null);
        assertThat(sonarInstance1).isNotEqualTo(sonarInstance2);
    }
}
