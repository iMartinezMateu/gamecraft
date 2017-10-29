package com.gamecraft.web.rest;

import com.gamecraft.GamecraftteamApp;

import com.gamecraft.domain.TeamUser;
import com.gamecraft.repository.TeamUserRepository;
import com.gamecraft.service.TeamUserService;
import com.gamecraft.repository.search.TeamUserSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.TeamUserCriteria;
import com.gamecraft.service.TeamUserQueryService;

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
 * Test class for the TeamUserResource REST controller.
 *
 * @see TeamUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftteamApp.class)
public class TeamUserResourceIntTest {

    private static final Long DEFAULT_TEAM_ID = 1L;
    private static final Long UPDATED_TEAM_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private TeamUserService teamUserService;

    @Autowired
    private TeamUserSearchRepository teamUserSearchRepository;

    @Autowired
    private TeamUserQueryService teamUserQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeamUserMockMvc;

    private TeamUser teamUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamUserResource teamUserResource = new TeamUserResource(teamUserService, teamUserQueryService);
        this.restTeamUserMockMvc = MockMvcBuilders.standaloneSetup(teamUserResource)
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
    public static TeamUser createEntity(EntityManager em) {
        TeamUser teamUser = new TeamUser()
            .teamId(DEFAULT_TEAM_ID)
            .userId(DEFAULT_USER_ID);
        return teamUser;
    }

    @Before
    public void initTest() {
        teamUserSearchRepository.deleteAll();
        teamUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamUser() throws Exception {
        int databaseSizeBeforeCreate = teamUserRepository.findAll().size();

        // Create the TeamUser
        restTeamUserMockMvc.perform(post("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamUser)))
            .andExpect(status().isCreated());

        // Validate the TeamUser in the database
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeCreate + 1);
        TeamUser testTeamUser = teamUserList.get(teamUserList.size() - 1);
        assertThat(testTeamUser.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testTeamUser.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the TeamUser in Elasticsearch
        TeamUser teamUserEs = teamUserSearchRepository.findOne(testTeamUser.getId());
        assertThat(teamUserEs).isEqualToComparingFieldByField(testTeamUser);
    }

    @Test
    @Transactional
    public void createTeamUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamUserRepository.findAll().size();

        // Create the TeamUser with an existing ID
        teamUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamUserMockMvc.perform(post("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamUser)))
            .andExpect(status().isBadRequest());

        // Validate the TeamUser in the database
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTeamIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamUserRepository.findAll().size();
        // set the field null
        teamUser.setTeamId(null);

        // Create the TeamUser, which fails.

        restTeamUserMockMvc.perform(post("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamUser)))
            .andExpect(status().isBadRequest());

        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamUserRepository.findAll().size();
        // set the field null
        teamUser.setUserId(null);

        // Create the TeamUser, which fails.

        restTeamUserMockMvc.perform(post("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamUser)))
            .andExpect(status().isBadRequest());

        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeamUsers() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList
        restTeamUserMockMvc.perform(get("/api/team-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getTeamUser() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get the teamUser
        restTeamUserMockMvc.perform(get("/api/team-users/{id}", teamUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamUser.getId().intValue()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllTeamUsersByTeamIdIsEqualToSomething() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where teamId equals to DEFAULT_TEAM_ID
        defaultTeamUserShouldBeFound("teamId.equals=" + DEFAULT_TEAM_ID);

        // Get all the teamUserList where teamId equals to UPDATED_TEAM_ID
        defaultTeamUserShouldNotBeFound("teamId.equals=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllTeamUsersByTeamIdIsInShouldWork() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where teamId in DEFAULT_TEAM_ID or UPDATED_TEAM_ID
        defaultTeamUserShouldBeFound("teamId.in=" + DEFAULT_TEAM_ID + "," + UPDATED_TEAM_ID);

        // Get all the teamUserList where teamId equals to UPDATED_TEAM_ID
        defaultTeamUserShouldNotBeFound("teamId.in=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllTeamUsersByTeamIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where teamId is not null
        defaultTeamUserShouldBeFound("teamId.specified=true");

        // Get all the teamUserList where teamId is null
        defaultTeamUserShouldNotBeFound("teamId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamUsersByTeamIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where teamId greater than or equals to DEFAULT_TEAM_ID
        defaultTeamUserShouldBeFound("teamId.greaterOrEqualThan=" + DEFAULT_TEAM_ID);

        // Get all the teamUserList where teamId greater than or equals to UPDATED_TEAM_ID
        defaultTeamUserShouldNotBeFound("teamId.greaterOrEqualThan=" + UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void getAllTeamUsersByTeamIdIsLessThanSomething() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where teamId less than or equals to DEFAULT_TEAM_ID
        defaultTeamUserShouldNotBeFound("teamId.lessThan=" + DEFAULT_TEAM_ID);

        // Get all the teamUserList where teamId less than or equals to UPDATED_TEAM_ID
        defaultTeamUserShouldBeFound("teamId.lessThan=" + UPDATED_TEAM_ID);
    }


    @Test
    @Transactional
    public void getAllTeamUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where userId equals to DEFAULT_USER_ID
        defaultTeamUserShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the teamUserList where userId equals to UPDATED_USER_ID
        defaultTeamUserShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTeamUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultTeamUserShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the teamUserList where userId equals to UPDATED_USER_ID
        defaultTeamUserShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTeamUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where userId is not null
        defaultTeamUserShouldBeFound("userId.specified=true");

        // Get all the teamUserList where userId is null
        defaultTeamUserShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where userId greater than or equals to DEFAULT_USER_ID
        defaultTeamUserShouldBeFound("userId.greaterOrEqualThan=" + DEFAULT_USER_ID);

        // Get all the teamUserList where userId greater than or equals to UPDATED_USER_ID
        defaultTeamUserShouldNotBeFound("userId.greaterOrEqualThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTeamUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList where userId less than or equals to DEFAULT_USER_ID
        defaultTeamUserShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the teamUserList where userId less than or equals to UPDATED_USER_ID
        defaultTeamUserShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTeamUserShouldBeFound(String filter) throws Exception {
        restTeamUserMockMvc.perform(get("/api/team-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTeamUserShouldNotBeFound(String filter) throws Exception {
        restTeamUserMockMvc.perform(get("/api/team-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTeamUser() throws Exception {
        // Get the teamUser
        restTeamUserMockMvc.perform(get("/api/team-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamUser() throws Exception {
        // Initialize the database
        teamUserService.save(teamUser);

        int databaseSizeBeforeUpdate = teamUserRepository.findAll().size();

        // Update the teamUser
        TeamUser updatedTeamUser = teamUserRepository.findOne(teamUser.getId());
        updatedTeamUser
            .teamId(UPDATED_TEAM_ID)
            .userId(UPDATED_USER_ID);

        restTeamUserMockMvc.perform(put("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamUser)))
            .andExpect(status().isOk());

        // Validate the TeamUser in the database
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeUpdate);
        TeamUser testTeamUser = teamUserList.get(teamUserList.size() - 1);
        assertThat(testTeamUser.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testTeamUser.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the TeamUser in Elasticsearch
        TeamUser teamUserEs = teamUserSearchRepository.findOne(testTeamUser.getId());
        assertThat(teamUserEs).isEqualToComparingFieldByField(testTeamUser);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamUser() throws Exception {
        int databaseSizeBeforeUpdate = teamUserRepository.findAll().size();

        // Create the TeamUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamUserMockMvc.perform(put("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamUser)))
            .andExpect(status().isCreated());

        // Validate the TeamUser in the database
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTeamUser() throws Exception {
        // Initialize the database
        teamUserService.save(teamUser);

        int databaseSizeBeforeDelete = teamUserRepository.findAll().size();

        // Get the teamUser
        restTeamUserMockMvc.perform(delete("/api/team-users/{id}", teamUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean teamUserExistsInEs = teamUserSearchRepository.exists(teamUser.getId());
        assertThat(teamUserExistsInEs).isFalse();

        // Validate the database is empty
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTeamUser() throws Exception {
        // Initialize the database
        teamUserService.save(teamUser);

        // Search the teamUser
        restTeamUserMockMvc.perform(get("/api/_search/team-users?query=id:" + teamUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamUser.class);
        TeamUser teamUser1 = new TeamUser();
        teamUser1.setId(1L);
        TeamUser teamUser2 = new TeamUser();
        teamUser2.setId(teamUser1.getId());
        assertThat(teamUser1).isEqualTo(teamUser2);
        teamUser2.setId(2L);
        assertThat(teamUser1).isNotEqualTo(teamUser2);
        teamUser1.setId(null);
        assertThat(teamUser1).isNotEqualTo(teamUser2);
    }
}
