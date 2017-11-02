package com.gamecraft.web.rest;

import com.gamecraft.GamecraftenginemanagerApp;

import com.gamecraft.domain.Engine;
import com.gamecraft.repository.EngineRepository;
import com.gamecraft.service.EngineService;
import com.gamecraft.repository.search.EngineSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.EngineCriteria;
import com.gamecraft.service.EngineQueryService;

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
 * Test class for the EngineResource REST controller.
 *
 * @see EngineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftenginemanagerApp.class)
public class EngineResourceIntTest {

    private static final String DEFAULT_ENGINE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENGINE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENGINE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ENGINE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ENGINE_COMPILER_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ENGINE_COMPILER_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_ENGINE_COMPILER_ARGUMENTS = "AAAAAAAAAA";
    private static final String UPDATED_ENGINE_COMPILER_ARGUMENTS = "BBBBBBBBBB";

    @Autowired
    private EngineRepository engineRepository;

    @Autowired
    private EngineService engineService;

    @Autowired
    private EngineSearchRepository engineSearchRepository;

    @Autowired
    private EngineQueryService engineQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEngineMockMvc;

    private Engine engine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EngineResource engineResource = new EngineResource(engineService, engineQueryService);
        this.restEngineMockMvc = MockMvcBuilders.standaloneSetup(engineResource)
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
    public static Engine createEntity(EntityManager em) {
        Engine engine = new Engine()
            .engineName(DEFAULT_ENGINE_NAME)
            .engineDescription(DEFAULT_ENGINE_DESCRIPTION)
            .engineCompilerPath(DEFAULT_ENGINE_COMPILER_PATH)
            .engineCompilerArguments(DEFAULT_ENGINE_COMPILER_ARGUMENTS);
        return engine;
    }

    @Before
    public void initTest() {
        engineSearchRepository.deleteAll();
        engine = createEntity(em);
    }

    @Test
    @Transactional
    public void createEngine() throws Exception {
        int databaseSizeBeforeCreate = engineRepository.findAll().size();

        // Create the Engine
        restEngineMockMvc.perform(post("/api/engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(engine)))
            .andExpect(status().isCreated());

        // Validate the Engine in the database
        List<Engine> engineList = engineRepository.findAll();
        assertThat(engineList).hasSize(databaseSizeBeforeCreate + 1);
        Engine testEngine = engineList.get(engineList.size() - 1);
        assertThat(testEngine.getEngineName()).isEqualTo(DEFAULT_ENGINE_NAME);
        assertThat(testEngine.getEngineDescription()).isEqualTo(DEFAULT_ENGINE_DESCRIPTION);
        assertThat(testEngine.getEngineCompilerPath()).isEqualTo(DEFAULT_ENGINE_COMPILER_PATH);
        assertThat(testEngine.getEngineCompilerArguments()).isEqualTo(DEFAULT_ENGINE_COMPILER_ARGUMENTS);

        // Validate the Engine in Elasticsearch
        Engine engineEs = engineSearchRepository.findOne(testEngine.getId());
        assertThat(engineEs).isEqualToComparingFieldByField(testEngine);
    }

    @Test
    @Transactional
    public void createEngineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = engineRepository.findAll().size();

        // Create the Engine with an existing ID
        engine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEngineMockMvc.perform(post("/api/engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(engine)))
            .andExpect(status().isBadRequest());

        // Validate the Engine in the database
        List<Engine> engineList = engineRepository.findAll();
        assertThat(engineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEngineNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = engineRepository.findAll().size();
        // set the field null
        engine.setEngineName(null);

        // Create the Engine, which fails.

        restEngineMockMvc.perform(post("/api/engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(engine)))
            .andExpect(status().isBadRequest());

        List<Engine> engineList = engineRepository.findAll();
        assertThat(engineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEngineCompilerPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = engineRepository.findAll().size();
        // set the field null
        engine.setEngineCompilerPath(null);

        // Create the Engine, which fails.

        restEngineMockMvc.perform(post("/api/engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(engine)))
            .andExpect(status().isBadRequest());

        List<Engine> engineList = engineRepository.findAll();
        assertThat(engineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEngines() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList
        restEngineMockMvc.perform(get("/api/engines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(engine.getId().intValue())))
            .andExpect(jsonPath("$.[*].engineName").value(hasItem(DEFAULT_ENGINE_NAME.toString())))
            .andExpect(jsonPath("$.[*].engineDescription").value(hasItem(DEFAULT_ENGINE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].engineCompilerPath").value(hasItem(DEFAULT_ENGINE_COMPILER_PATH.toString())))
            .andExpect(jsonPath("$.[*].engineCompilerArguments").value(hasItem(DEFAULT_ENGINE_COMPILER_ARGUMENTS.toString())));
    }

    @Test
    @Transactional
    public void getEngine() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get the engine
        restEngineMockMvc.perform(get("/api/engines/{id}", engine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(engine.getId().intValue()))
            .andExpect(jsonPath("$.engineName").value(DEFAULT_ENGINE_NAME.toString()))
            .andExpect(jsonPath("$.engineDescription").value(DEFAULT_ENGINE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.engineCompilerPath").value(DEFAULT_ENGINE_COMPILER_PATH.toString()))
            .andExpect(jsonPath("$.engineCompilerArguments").value(DEFAULT_ENGINE_COMPILER_ARGUMENTS.toString()));
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineNameIsEqualToSomething() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineName equals to DEFAULT_ENGINE_NAME
        defaultEngineShouldBeFound("engineName.equals=" + DEFAULT_ENGINE_NAME);

        // Get all the engineList where engineName equals to UPDATED_ENGINE_NAME
        defaultEngineShouldNotBeFound("engineName.equals=" + UPDATED_ENGINE_NAME);
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineNameIsInShouldWork() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineName in DEFAULT_ENGINE_NAME or UPDATED_ENGINE_NAME
        defaultEngineShouldBeFound("engineName.in=" + DEFAULT_ENGINE_NAME + "," + UPDATED_ENGINE_NAME);

        // Get all the engineList where engineName equals to UPDATED_ENGINE_NAME
        defaultEngineShouldNotBeFound("engineName.in=" + UPDATED_ENGINE_NAME);
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineName is not null
        defaultEngineShouldBeFound("engineName.specified=true");

        // Get all the engineList where engineName is null
        defaultEngineShouldNotBeFound("engineName.specified=false");
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineDescription equals to DEFAULT_ENGINE_DESCRIPTION
        defaultEngineShouldBeFound("engineDescription.equals=" + DEFAULT_ENGINE_DESCRIPTION);

        // Get all the engineList where engineDescription equals to UPDATED_ENGINE_DESCRIPTION
        defaultEngineShouldNotBeFound("engineDescription.equals=" + UPDATED_ENGINE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineDescription in DEFAULT_ENGINE_DESCRIPTION or UPDATED_ENGINE_DESCRIPTION
        defaultEngineShouldBeFound("engineDescription.in=" + DEFAULT_ENGINE_DESCRIPTION + "," + UPDATED_ENGINE_DESCRIPTION);

        // Get all the engineList where engineDescription equals to UPDATED_ENGINE_DESCRIPTION
        defaultEngineShouldNotBeFound("engineDescription.in=" + UPDATED_ENGINE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineDescription is not null
        defaultEngineShouldBeFound("engineDescription.specified=true");

        // Get all the engineList where engineDescription is null
        defaultEngineShouldNotBeFound("engineDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineCompilerPathIsEqualToSomething() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineCompilerPath equals to DEFAULT_ENGINE_COMPILER_PATH
        defaultEngineShouldBeFound("engineCompilerPath.equals=" + DEFAULT_ENGINE_COMPILER_PATH);

        // Get all the engineList where engineCompilerPath equals to UPDATED_ENGINE_COMPILER_PATH
        defaultEngineShouldNotBeFound("engineCompilerPath.equals=" + UPDATED_ENGINE_COMPILER_PATH);
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineCompilerPathIsInShouldWork() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineCompilerPath in DEFAULT_ENGINE_COMPILER_PATH or UPDATED_ENGINE_COMPILER_PATH
        defaultEngineShouldBeFound("engineCompilerPath.in=" + DEFAULT_ENGINE_COMPILER_PATH + "," + UPDATED_ENGINE_COMPILER_PATH);

        // Get all the engineList where engineCompilerPath equals to UPDATED_ENGINE_COMPILER_PATH
        defaultEngineShouldNotBeFound("engineCompilerPath.in=" + UPDATED_ENGINE_COMPILER_PATH);
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineCompilerPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineCompilerPath is not null
        defaultEngineShouldBeFound("engineCompilerPath.specified=true");

        // Get all the engineList where engineCompilerPath is null
        defaultEngineShouldNotBeFound("engineCompilerPath.specified=false");
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineCompilerArgumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineCompilerArguments equals to DEFAULT_ENGINE_COMPILER_ARGUMENTS
        defaultEngineShouldBeFound("engineCompilerArguments.equals=" + DEFAULT_ENGINE_COMPILER_ARGUMENTS);

        // Get all the engineList where engineCompilerArguments equals to UPDATED_ENGINE_COMPILER_ARGUMENTS
        defaultEngineShouldNotBeFound("engineCompilerArguments.equals=" + UPDATED_ENGINE_COMPILER_ARGUMENTS);
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineCompilerArgumentsIsInShouldWork() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineCompilerArguments in DEFAULT_ENGINE_COMPILER_ARGUMENTS or UPDATED_ENGINE_COMPILER_ARGUMENTS
        defaultEngineShouldBeFound("engineCompilerArguments.in=" + DEFAULT_ENGINE_COMPILER_ARGUMENTS + "," + UPDATED_ENGINE_COMPILER_ARGUMENTS);

        // Get all the engineList where engineCompilerArguments equals to UPDATED_ENGINE_COMPILER_ARGUMENTS
        defaultEngineShouldNotBeFound("engineCompilerArguments.in=" + UPDATED_ENGINE_COMPILER_ARGUMENTS);
    }

    @Test
    @Transactional
    public void getAllEnginesByEngineCompilerArgumentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        engineRepository.saveAndFlush(engine);

        // Get all the engineList where engineCompilerArguments is not null
        defaultEngineShouldBeFound("engineCompilerArguments.specified=true");

        // Get all the engineList where engineCompilerArguments is null
        defaultEngineShouldNotBeFound("engineCompilerArguments.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEngineShouldBeFound(String filter) throws Exception {
        restEngineMockMvc.perform(get("/api/engines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(engine.getId().intValue())))
            .andExpect(jsonPath("$.[*].engineName").value(hasItem(DEFAULT_ENGINE_NAME.toString())))
            .andExpect(jsonPath("$.[*].engineDescription").value(hasItem(DEFAULT_ENGINE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].engineCompilerPath").value(hasItem(DEFAULT_ENGINE_COMPILER_PATH.toString())))
            .andExpect(jsonPath("$.[*].engineCompilerArguments").value(hasItem(DEFAULT_ENGINE_COMPILER_ARGUMENTS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEngineShouldNotBeFound(String filter) throws Exception {
        restEngineMockMvc.perform(get("/api/engines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingEngine() throws Exception {
        // Get the engine
        restEngineMockMvc.perform(get("/api/engines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEngine() throws Exception {
        // Initialize the database
        engineService.save(engine);

        int databaseSizeBeforeUpdate = engineRepository.findAll().size();

        // Update the engine
        Engine updatedEngine = engineRepository.findOne(engine.getId());
        updatedEngine
            .engineName(UPDATED_ENGINE_NAME)
            .engineDescription(UPDATED_ENGINE_DESCRIPTION)
            .engineCompilerPath(UPDATED_ENGINE_COMPILER_PATH)
            .engineCompilerArguments(UPDATED_ENGINE_COMPILER_ARGUMENTS);

        restEngineMockMvc.perform(put("/api/engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEngine)))
            .andExpect(status().isOk());

        // Validate the Engine in the database
        List<Engine> engineList = engineRepository.findAll();
        assertThat(engineList).hasSize(databaseSizeBeforeUpdate);
        Engine testEngine = engineList.get(engineList.size() - 1);
        assertThat(testEngine.getEngineName()).isEqualTo(UPDATED_ENGINE_NAME);
        assertThat(testEngine.getEngineDescription()).isEqualTo(UPDATED_ENGINE_DESCRIPTION);
        assertThat(testEngine.getEngineCompilerPath()).isEqualTo(UPDATED_ENGINE_COMPILER_PATH);
        assertThat(testEngine.getEngineCompilerArguments()).isEqualTo(UPDATED_ENGINE_COMPILER_ARGUMENTS);

        // Validate the Engine in Elasticsearch
        Engine engineEs = engineSearchRepository.findOne(testEngine.getId());
        assertThat(engineEs).isEqualToComparingFieldByField(testEngine);
    }

    @Test
    @Transactional
    public void updateNonExistingEngine() throws Exception {
        int databaseSizeBeforeUpdate = engineRepository.findAll().size();

        // Create the Engine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEngineMockMvc.perform(put("/api/engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(engine)))
            .andExpect(status().isCreated());

        // Validate the Engine in the database
        List<Engine> engineList = engineRepository.findAll();
        assertThat(engineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEngine() throws Exception {
        // Initialize the database
        engineService.save(engine);

        int databaseSizeBeforeDelete = engineRepository.findAll().size();

        // Get the engine
        restEngineMockMvc.perform(delete("/api/engines/{id}", engine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean engineExistsInEs = engineSearchRepository.exists(engine.getId());
        assertThat(engineExistsInEs).isFalse();

        // Validate the database is empty
        List<Engine> engineList = engineRepository.findAll();
        assertThat(engineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEngine() throws Exception {
        // Initialize the database
        engineService.save(engine);

        // Search the engine
        restEngineMockMvc.perform(get("/api/_search/engines?query=id:" + engine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(engine.getId().intValue())))
            .andExpect(jsonPath("$.[*].engineName").value(hasItem(DEFAULT_ENGINE_NAME.toString())))
            .andExpect(jsonPath("$.[*].engineDescription").value(hasItem(DEFAULT_ENGINE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].engineCompilerPath").value(hasItem(DEFAULT_ENGINE_COMPILER_PATH.toString())))
            .andExpect(jsonPath("$.[*].engineCompilerArguments").value(hasItem(DEFAULT_ENGINE_COMPILER_ARGUMENTS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Engine.class);
        Engine engine1 = new Engine();
        engine1.setId(1L);
        Engine engine2 = new Engine();
        engine2.setId(engine1.getId());
        assertThat(engine1).isEqualTo(engine2);
        engine2.setId(2L);
        assertThat(engine1).isNotEqualTo(engine2);
        engine1.setId(null);
        assertThat(engine1).isNotEqualTo(engine2);
    }
}
