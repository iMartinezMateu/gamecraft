package com.gamecraft.web.rest;

import com.gamecraft.GamecraftteamApp;

import com.gamecraft.domain.Team;
import com.gamecraft.repository.TeamRepository;
import com.gamecraft.service.TeamService;
import com.gamecraft.repository.search.TeamSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.TeamCriteria;
import com.gamecraft.service.TeamQueryService;

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
 * Test class for the TeamResource REST controller.
 *
 * @see TeamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftteamApp.class)
public class TeamResourceIntTest {

    private static final String DEFAULT_TEAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamSearchRepository teamSearchRepository;

    @Autowired
    private TeamQueryService teamQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeamMockMvc;

    private Team team;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamResource teamResource = new TeamResource(teamService, teamQueryService);
        this.restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
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
    public static Team createEntity(EntityManager em) {
        Team team = new Team()
            .teamName(DEFAULT_TEAM_NAME)
            .teamDescription(DEFAULT_TEAM_DESCRIPTION);
        return team;
    }

    @Before
    public void initTest() {
        teamSearchRepository.deleteAll();
        team = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeam() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();

        // Create the Team
        restTeamMockMvc.perform(post("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(team)))
            .andExpect(status().isCreated());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeCreate + 1);
        Team testTeam = teamList.get(teamList.size() - 1);
        assertThat(testTeam.getTeamName()).isEqualTo(DEFAULT_TEAM_NAME);
        assertThat(testTeam.getTeamDescription()).isEqualTo(DEFAULT_TEAM_DESCRIPTION);

        // Validate the Team in Elasticsearch
        Team teamEs = teamSearchRepository.findOne(testTeam.getId());
        assertThat(teamEs).isEqualToComparingFieldByField(testTeam);
    }

    @Test
    @Transactional
    public void createTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();

        // Create the Team with an existing ID
        team.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamMockMvc.perform(post("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(team)))
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTeamNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamRepository.findAll().size();
        // set the field null
        team.setTeamName(null);

        // Create the Team, which fails.

        restTeamMockMvc.perform(post("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(team)))
            .andExpect(status().isBadRequest());

        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeams() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME.toString())))
            .andExpect(jsonPath("$.[*].teamDescription").value(hasItem(DEFAULT_TEAM_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", team.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(team.getId().intValue()))
            .andExpect(jsonPath("$.teamName").value(DEFAULT_TEAM_NAME.toString()))
            .andExpect(jsonPath("$.teamDescription").value(DEFAULT_TEAM_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllTeamsByTeamNameIsEqualToSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where teamName equals to DEFAULT_TEAM_NAME
        defaultTeamShouldBeFound("teamName.equals=" + DEFAULT_TEAM_NAME);

        // Get all the teamList where teamName equals to UPDATED_TEAM_NAME
        defaultTeamShouldNotBeFound("teamName.equals=" + UPDATED_TEAM_NAME);
    }

    @Test
    @Transactional
    public void getAllTeamsByTeamNameIsInShouldWork() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where teamName in DEFAULT_TEAM_NAME or UPDATED_TEAM_NAME
        defaultTeamShouldBeFound("teamName.in=" + DEFAULT_TEAM_NAME + "," + UPDATED_TEAM_NAME);

        // Get all the teamList where teamName equals to UPDATED_TEAM_NAME
        defaultTeamShouldNotBeFound("teamName.in=" + UPDATED_TEAM_NAME);
    }

    @Test
    @Transactional
    public void getAllTeamsByTeamNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where teamName is not null
        defaultTeamShouldBeFound("teamName.specified=true");

        // Get all the teamList where teamName is null
        defaultTeamShouldNotBeFound("teamName.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamsByTeamDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where teamDescription equals to DEFAULT_TEAM_DESCRIPTION
        defaultTeamShouldBeFound("teamDescription.equals=" + DEFAULT_TEAM_DESCRIPTION);

        // Get all the teamList where teamDescription equals to UPDATED_TEAM_DESCRIPTION
        defaultTeamShouldNotBeFound("teamDescription.equals=" + UPDATED_TEAM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTeamsByTeamDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where teamDescription in DEFAULT_TEAM_DESCRIPTION or UPDATED_TEAM_DESCRIPTION
        defaultTeamShouldBeFound("teamDescription.in=" + DEFAULT_TEAM_DESCRIPTION + "," + UPDATED_TEAM_DESCRIPTION);

        // Get all the teamList where teamDescription equals to UPDATED_TEAM_DESCRIPTION
        defaultTeamShouldNotBeFound("teamDescription.in=" + UPDATED_TEAM_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTeamsByTeamDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where teamDescription is not null
        defaultTeamShouldBeFound("teamDescription.specified=true");

        // Get all the teamList where teamDescription is null
        defaultTeamShouldNotBeFound("teamDescription.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTeamShouldBeFound(String filter) throws Exception {
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME.toString())))
            .andExpect(jsonPath("$.[*].teamDescription").value(hasItem(DEFAULT_TEAM_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTeamShouldNotBeFound(String filter) throws Exception {
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTeam() throws Exception {
        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeam() throws Exception {
        // Initialize the database
        teamService.save(team);

        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Update the team
        Team updatedTeam = teamRepository.findOne(team.getId());
        updatedTeam
            .teamName(UPDATED_TEAM_NAME)
            .teamDescription(UPDATED_TEAM_DESCRIPTION);

        restTeamMockMvc.perform(put("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeam)))
            .andExpect(status().isOk());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate);
        Team testTeam = teamList.get(teamList.size() - 1);
        assertThat(testTeam.getTeamName()).isEqualTo(UPDATED_TEAM_NAME);
        assertThat(testTeam.getTeamDescription()).isEqualTo(UPDATED_TEAM_DESCRIPTION);

        // Validate the Team in Elasticsearch
        Team teamEs = teamSearchRepository.findOne(testTeam.getId());
        assertThat(teamEs).isEqualToComparingFieldByField(testTeam);
    }

    @Test
    @Transactional
    public void updateNonExistingTeam() throws Exception {
        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Create the Team

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamMockMvc.perform(put("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(team)))
            .andExpect(status().isCreated());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTeam() throws Exception {
        // Initialize the database
        teamService.save(team);

        int databaseSizeBeforeDelete = teamRepository.findAll().size();

        // Get the team
        restTeamMockMvc.perform(delete("/api/teams/{id}", team.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean teamExistsInEs = teamSearchRepository.exists(team.getId());
        assertThat(teamExistsInEs).isFalse();

        // Validate the database is empty
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTeam() throws Exception {
        // Initialize the database
        teamService.save(team);

        // Search the team
        restTeamMockMvc.perform(get("/api/_search/teams?query=id:" + team.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME.toString())))
            .andExpect(jsonPath("$.[*].teamDescription").value(hasItem(DEFAULT_TEAM_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Team.class);
        Team team1 = new Team();
        team1.setId(1L);
        Team team2 = new Team();
        team2.setId(team1.getId());
        assertThat(team1).isEqualTo(team2);
        team2.setId(2L);
        assertThat(team1).isNotEqualTo(team2);
        team1.setId(null);
        assertThat(team1).isNotEqualTo(team2);
    }
}
