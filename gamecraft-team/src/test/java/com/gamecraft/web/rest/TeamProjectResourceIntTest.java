package com.gamecraft.web.rest;

import com.gamecraft.GamecraftteamApp;

import com.gamecraft.domain.TeamProject;
import com.gamecraft.repository.TeamProjectRepository;
import com.gamecraft.service.TeamProjectService;
import com.gamecraft.repository.search.TeamProjectSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.TeamProjectCriteria;
import com.gamecraft.service.TeamProjectQueryService;

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
 * Test class for the TeamProjectResource REST controller.
 *
 * @see TeamProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftteamApp.class)
public class TeamProjectResourceIntTest {

    private static final Long DEFAULT_TEAM_ID = 1L;
    private static final Long UPDATED_TEAM_ID = 2L;

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long UPDATED_PROJECT_ID = 2L;

    @Autowired
    private TeamProjectRepository teamProjectRepository;

    @Autowired
    private TeamProjectService teamProjectService;

    @Autowired
    private TeamProjectSearchRepository teamProjectSearchRepository;

    @Autowired
    private TeamProjectQueryService teamProjectQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeamProjectMockMvc;

    private TeamProject teamProject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamProjectResource teamProjectResource = new TeamProjectResource(teamProjectService, teamProjectQueryService);
        this.restTeamProjectMockMvc = MockMvcBuilders.standaloneSetup(teamProjectResource)
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
    public static TeamProject createEntity(EntityManager em) {
        TeamProject teamProject = new TeamProject()
            .teamId(DEFAULT_TEAM_ID)
            .projectId(DEFAULT_PROJECT_ID);
        return teamProject;
    }

    @Before
    public void initTest() {
        teamProjectSearchRepository.deleteAll();
        teamProject = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamProject() throws Exception {
        int databaseSizeBeforeCreate = teamProjectRepository.findAll().size();

        // Create the TeamProject
        restTeamProjectMockMvc.perform(post("/api/team-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamProject)))
            .andExpect(status().isCreated());

        // Validate the TeamProject in the database
        List<TeamProject> teamProjectList = teamProjectRepository.findAll();
        assertThat(teamProjectList).hasSize(databaseSizeBeforeCreate + 1);
        TeamProject testTeamProject = teamProjectList.get(teamProjectList.size() - 1);
        assertThat(testTeamProject.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testTeamProject.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);

        // Validate the TeamProject in Elasticsearch
        TeamProject teamProjectEs = teamProjectSearchRepository.findOne(testTeamProject.getId());
        assertThat(teamProjectEs).isEqualToComparingFieldByField(testTeamProject);
    }

    @Test
    @Transactional
    public void createTeamProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamProjectRepository.findAll().size();

        // Create the TeamProject with an existing ID
        teamProject.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamProjectMockMvc.perform(post("/api/team-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamProject)))
            .andExpect(status().isBadRequest());

        // Validate the TeamProject in the database
        List<TeamProject> teamProjectList = teamProjectRepository.findAll();
        assertThat(teamProjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTeamIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamProjectRepository.findAll().size();
        // set the field null
        teamProject.setTeamId(null);

        // Create the TeamProject, which fails.

        restTeamProjectMockMvc.perform(post("/api/team-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamProject)))
            .andExpect(status().isBadRequest());

        List<TeamProject> teamProjectList = teamProjectRepository.findAll();
        assertThat(teamProjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamProjectRepository.findAll().size();
        // set the field null
        teamProject.setProjectId(null);

        // Create the TeamProject, which fails.

        restTeamProjectMockMvc.perform(post("/api/team-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamProject)))
            .andExpect(status().isBadRequest());

        List<TeamProject> teamProjectList = teamProjectRepository.findAll();
        assertThat(teamProjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeamProjects() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList
        restTeamProjectMockMvc.perform(get("/api/team-projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getTeamProject() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get the teamProject
        restTeamProjectMockMvc.perform(get("/api/team-projects/{id}", teamProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamProject.getId().intValue()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID.intValue()))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllTeamProjectsByTeamIdIsEqualToSomething() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where teamId equals to DEFAULT_TEAM_ID
        defaultTeamProjectShouldBeFound("teamId.equals=" + DEFAULT_TEAM_ID);

        // Get all the teamProjectList where teamId equals to UPDATED_TEAM_ID
        defaultTeamProjectShouldNotBeFound("teamId.equals=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllTeamProjectsByTeamIdIsInShouldWork() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where teamId in DEFAULT_TEAM_ID or UPDATED_TEAM_ID
        defaultTeamProjectShouldBeFound("teamId.in=" + DEFAULT_TEAM_ID + "," + UPDATED_TEAM_ID);

        // Get all the teamProjectList where teamId equals to UPDATED_TEAM_ID
        defaultTeamProjectShouldNotBeFound("teamId.in=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllTeamProjectsByTeamIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where teamId is not null
        defaultTeamProjectShouldBeFound("teamId.specified=true");

        // Get all the teamProjectList where teamId is null
        defaultTeamProjectShouldNotBeFound("teamId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamProjectsByTeamIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where teamId greater than or equals to DEFAULT_TEAM_ID
        defaultTeamProjectShouldBeFound("teamId.greaterOrEqualThan=" + DEFAULT_TEAM_ID);

        // Get all the teamProjectList where teamId greater than or equals to UPDATED_TEAM_ID
        defaultTeamProjectShouldNotBeFound("teamId.greaterOrEqualThan=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllTeamProjectsByTeamIdIsLessThanSomething() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where teamId less than or equals to DEFAULT_TEAM_ID
        defaultTeamProjectShouldNotBeFound("teamId.lessThan=" + DEFAULT_TEAM_ID);

        // Get all the teamProjectList where teamId less than or equals to UPDATED_TEAM_ID
        defaultTeamProjectShouldBeFound("teamId.lessThan=" + UPDATED_TEAM_ID);
    }


    @Test
    @Transactional
    public void getAllTeamProjectsByProjectIdIsEqualToSomething() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where projectId equals to DEFAULT_PROJECT_ID
        defaultTeamProjectShouldBeFound("projectId.equals=" + DEFAULT_PROJECT_ID);

        // Get all the teamProjectList where projectId equals to UPDATED_PROJECT_ID
        defaultTeamProjectShouldNotBeFound("projectId.equals=" + UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllTeamProjectsByProjectIdIsInShouldWork() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where projectId in DEFAULT_PROJECT_ID or UPDATED_PROJECT_ID
        defaultTeamProjectShouldBeFound("projectId.in=" + DEFAULT_PROJECT_ID + "," + UPDATED_PROJECT_ID);

        // Get all the teamProjectList where projectId equals to UPDATED_PROJECT_ID
        defaultTeamProjectShouldNotBeFound("projectId.in=" + UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllTeamProjectsByProjectIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where projectId is not null
        defaultTeamProjectShouldBeFound("projectId.specified=true");

        // Get all the teamProjectList where projectId is null
        defaultTeamProjectShouldNotBeFound("projectId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamProjectsByProjectIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where projectId greater than or equals to DEFAULT_PROJECT_ID
        defaultTeamProjectShouldBeFound("projectId.greaterOrEqualThan=" + DEFAULT_PROJECT_ID);

        // Get all the teamProjectList where projectId greater than or equals to UPDATED_PROJECT_ID
        defaultTeamProjectShouldNotBeFound("projectId.greaterOrEqualThan=" + UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllTeamProjectsByProjectIdIsLessThanSomething() throws Exception {
        // Initialize the database
        teamProjectRepository.saveAndFlush(teamProject);

        // Get all the teamProjectList where projectId less than or equals to DEFAULT_PROJECT_ID
        defaultTeamProjectShouldNotBeFound("projectId.lessThan=" + DEFAULT_PROJECT_ID);

        // Get all the teamProjectList where projectId less than or equals to UPDATED_PROJECT_ID
        defaultTeamProjectShouldBeFound("projectId.lessThan=" + UPDATED_PROJECT_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTeamProjectShouldBeFound(String filter) throws Exception {
        restTeamProjectMockMvc.perform(get("/api/team-projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTeamProjectShouldNotBeFound(String filter) throws Exception {
        restTeamProjectMockMvc.perform(get("/api/team-projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTeamProject() throws Exception {
        // Get the teamProject
        restTeamProjectMockMvc.perform(get("/api/team-projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamProject() throws Exception {
        // Initialize the database
        teamProjectService.save(teamProject);

        int databaseSizeBeforeUpdate = teamProjectRepository.findAll().size();

        // Update the teamProject
        TeamProject updatedTeamProject = teamProjectRepository.findOne(teamProject.getId());
        updatedTeamProject
            .teamId(UPDATED_TEAM_ID)
            .projectId(UPDATED_PROJECT_ID);

        restTeamProjectMockMvc.perform(put("/api/team-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamProject)))
            .andExpect(status().isOk());

        // Validate the TeamProject in the database
        List<TeamProject> teamProjectList = teamProjectRepository.findAll();
        assertThat(teamProjectList).hasSize(databaseSizeBeforeUpdate);
        TeamProject testTeamProject = teamProjectList.get(teamProjectList.size() - 1);
        assertThat(testTeamProject.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testTeamProject.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);

        // Validate the TeamProject in Elasticsearch
        TeamProject teamProjectEs = teamProjectSearchRepository.findOne(testTeamProject.getId());
        assertThat(teamProjectEs).isEqualToComparingFieldByField(testTeamProject);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamProject() throws Exception {
        int databaseSizeBeforeUpdate = teamProjectRepository.findAll().size();

        // Create the TeamProject

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamProjectMockMvc.perform(put("/api/team-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamProject)))
            .andExpect(status().isCreated());

        // Validate the TeamProject in the database
        List<TeamProject> teamProjectList = teamProjectRepository.findAll();
        assertThat(teamProjectList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTeamProject() throws Exception {
        // Initialize the database
        teamProjectService.save(teamProject);

        int databaseSizeBeforeDelete = teamProjectRepository.findAll().size();

        // Get the teamProject
        restTeamProjectMockMvc.perform(delete("/api/team-projects/{id}", teamProject.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean teamProjectExistsInEs = teamProjectSearchRepository.exists(teamProject.getId());
        assertThat(teamProjectExistsInEs).isFalse();

        // Validate the database is empty
        List<TeamProject> teamProjectList = teamProjectRepository.findAll();
        assertThat(teamProjectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTeamProject() throws Exception {
        // Initialize the database
        teamProjectService.save(teamProject);

        // Search the teamProject
        restTeamProjectMockMvc.perform(get("/api/_search/team-projects?query=id:" + teamProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamProject.class);
        TeamProject teamProject1 = new TeamProject();
        teamProject1.setId(1L);
        TeamProject teamProject2 = new TeamProject();
        teamProject2.setId(teamProject1.getId());
        assertThat(teamProject1).isEqualTo(teamProject2);
        teamProject2.setId(2L);
        assertThat(teamProject1).isNotEqualTo(teamProject2);
        teamProject1.setId(null);
        assertThat(teamProject1).isNotEqualTo(teamProject2);
    }
}
