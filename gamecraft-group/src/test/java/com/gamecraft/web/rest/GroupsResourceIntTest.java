package com.gamecraft.web.rest;

import com.gamecraft.GamecraftgroupApp;

import com.gamecraft.domain.Groups;
import com.gamecraft.repository.GroupsRepository;
import com.gamecraft.service.GroupsService;
import com.gamecraft.repository.search.GroupsSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.GroupsCriteria;
import com.gamecraft.service.GroupsQueryService;

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

import com.gamecraft.domain.enumeration.Role;
/**
 * Test class for the GroupsResource REST controller.
 *
 * @see GroupsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftgroupApp.class)
public class GroupsResourceIntTest {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_DESCRIPTION = "BBBBBBBBBB";

    private static final Role DEFAULT_GROUP_ROLE = Role.GUEST;
    private static final Role UPDATED_GROUP_ROLE = Role.USER;

    private static final Boolean DEFAULT_GROUP_ENABLED = false;
    private static final Boolean UPDATED_GROUP_ENABLED = true;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private GroupsSearchRepository groupsSearchRepository;

    @Autowired
    private GroupsQueryService groupsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGroupsMockMvc;

    private Groups groups;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GroupsResource groupsResource = new GroupsResource(groupsService, groupsQueryService);
        this.restGroupsMockMvc = MockMvcBuilders.standaloneSetup(groupsResource)
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
    public static Groups createEntity(EntityManager em) {
        Groups groups = new Groups()
            .groupName(DEFAULT_GROUP_NAME)
            .groupDescription(DEFAULT_GROUP_DESCRIPTION)
            .groupRole(DEFAULT_GROUP_ROLE)
            .groupEnabled(DEFAULT_GROUP_ENABLED);
        return groups;
    }

    @Before
    public void initTest() {
        groupsSearchRepository.deleteAll();
        groups = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroups() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // Create the Groups
        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate + 1);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testGroups.getGroupDescription()).isEqualTo(DEFAULT_GROUP_DESCRIPTION);
        assertThat(testGroups.getGroupRole()).isEqualTo(DEFAULT_GROUP_ROLE);
        assertThat(testGroups.isGroupEnabled()).isEqualTo(DEFAULT_GROUP_ENABLED);

        // Validate the Groups in Elasticsearch
        Groups groupsEs = groupsSearchRepository.findOne(testGroups.getId());
        assertThat(groupsEs).isEqualToIgnoringGivenFields(testGroups);
    }

    @Test
    @Transactional
    public void createGroupsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // Create the Groups with an existing ID
        groups.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isBadRequest());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupsRepository.findAll().size();
        // set the field null
        groups.setGroupName(null);

        // Create the Groups, which fails.

        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isBadRequest());

        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGroupRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupsRepository.findAll().size();
        // set the field null
        groups.setGroupRole(null);

        // Create the Groups, which fails.

        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isBadRequest());

        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGroupEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupsRepository.findAll().size();
        // set the field null
        groups.setGroupEnabled(null);

        // Create the Groups, which fails.

        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isBadRequest());

        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList
        restGroupsMockMvc.perform(get("/api/groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].groupDescription").value(hasItem(DEFAULT_GROUP_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].groupRole").value(hasItem(DEFAULT_GROUP_ROLE.toString())))
            .andExpect(jsonPath("$.[*].groupEnabled").value(hasItem(DEFAULT_GROUP_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", groups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groups.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME.toString()))
            .andExpect(jsonPath("$.groupDescription").value(DEFAULT_GROUP_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.groupRole").value(DEFAULT_GROUP_ROLE.toString()))
            .andExpect(jsonPath("$.groupEnabled").value(DEFAULT_GROUP_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupNameIsEqualToSomething() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupName equals to DEFAULT_GROUP_NAME
        defaultGroupsShouldBeFound("groupName.equals=" + DEFAULT_GROUP_NAME);

        // Get all the groupsList where groupName equals to UPDATED_GROUP_NAME
        defaultGroupsShouldNotBeFound("groupName.equals=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupNameIsInShouldWork() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupName in DEFAULT_GROUP_NAME or UPDATED_GROUP_NAME
        defaultGroupsShouldBeFound("groupName.in=" + DEFAULT_GROUP_NAME + "," + UPDATED_GROUP_NAME);

        // Get all the groupsList where groupName equals to UPDATED_GROUP_NAME
        defaultGroupsShouldNotBeFound("groupName.in=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupName is not null
        defaultGroupsShouldBeFound("groupName.specified=true");

        // Get all the groupsList where groupName is null
        defaultGroupsShouldNotBeFound("groupName.specified=false");
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupDescription equals to DEFAULT_GROUP_DESCRIPTION
        defaultGroupsShouldBeFound("groupDescription.equals=" + DEFAULT_GROUP_DESCRIPTION);

        // Get all the groupsList where groupDescription equals to UPDATED_GROUP_DESCRIPTION
        defaultGroupsShouldNotBeFound("groupDescription.equals=" + UPDATED_GROUP_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupDescription in DEFAULT_GROUP_DESCRIPTION or UPDATED_GROUP_DESCRIPTION
        defaultGroupsShouldBeFound("groupDescription.in=" + DEFAULT_GROUP_DESCRIPTION + "," + UPDATED_GROUP_DESCRIPTION);

        // Get all the groupsList where groupDescription equals to UPDATED_GROUP_DESCRIPTION
        defaultGroupsShouldNotBeFound("groupDescription.in=" + UPDATED_GROUP_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupDescription is not null
        defaultGroupsShouldBeFound("groupDescription.specified=true");

        // Get all the groupsList where groupDescription is null
        defaultGroupsShouldNotBeFound("groupDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupRole equals to DEFAULT_GROUP_ROLE
        defaultGroupsShouldBeFound("groupRole.equals=" + DEFAULT_GROUP_ROLE);

        // Get all the groupsList where groupRole equals to UPDATED_GROUP_ROLE
        defaultGroupsShouldNotBeFound("groupRole.equals=" + UPDATED_GROUP_ROLE);
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupRoleIsInShouldWork() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupRole in DEFAULT_GROUP_ROLE or UPDATED_GROUP_ROLE
        defaultGroupsShouldBeFound("groupRole.in=" + DEFAULT_GROUP_ROLE + "," + UPDATED_GROUP_ROLE);

        // Get all the groupsList where groupRole equals to UPDATED_GROUP_ROLE
        defaultGroupsShouldNotBeFound("groupRole.in=" + UPDATED_GROUP_ROLE);
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupRole is not null
        defaultGroupsShouldBeFound("groupRole.specified=true");

        // Get all the groupsList where groupRole is null
        defaultGroupsShouldNotBeFound("groupRole.specified=false");
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupEnabled equals to DEFAULT_GROUP_ENABLED
        defaultGroupsShouldBeFound("groupEnabled.equals=" + DEFAULT_GROUP_ENABLED);

        // Get all the groupsList where groupEnabled equals to UPDATED_GROUP_ENABLED
        defaultGroupsShouldNotBeFound("groupEnabled.equals=" + UPDATED_GROUP_ENABLED);
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupEnabled in DEFAULT_GROUP_ENABLED or UPDATED_GROUP_ENABLED
        defaultGroupsShouldBeFound("groupEnabled.in=" + DEFAULT_GROUP_ENABLED + "," + UPDATED_GROUP_ENABLED);

        // Get all the groupsList where groupEnabled equals to UPDATED_GROUP_ENABLED
        defaultGroupsShouldNotBeFound("groupEnabled.in=" + UPDATED_GROUP_ENABLED);
    }

    @Test
    @Transactional
    public void getAllGroupsByGroupEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList where groupEnabled is not null
        defaultGroupsShouldBeFound("groupEnabled.specified=true");

        // Get all the groupsList where groupEnabled is null
        defaultGroupsShouldNotBeFound("groupEnabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGroupsShouldBeFound(String filter) throws Exception {
        restGroupsMockMvc.perform(get("/api/groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].groupDescription").value(hasItem(DEFAULT_GROUP_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].groupRole").value(hasItem(DEFAULT_GROUP_ROLE.toString())))
            .andExpect(jsonPath("$.[*].groupEnabled").value(hasItem(DEFAULT_GROUP_ENABLED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGroupsShouldNotBeFound(String filter) throws Exception {
        restGroupsMockMvc.perform(get("/api/groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingGroups() throws Exception {
        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroups() throws Exception {
        // Initialize the database
        groupsService.save(groups);

        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Update the groups
        Groups updatedGroups = groupsRepository.findOne(groups.getId());
        // Disconnect from session so that the updates on updatedGroups are not directly saved in db
        em.detach(updatedGroups);
        updatedGroups
            .groupName(UPDATED_GROUP_NAME)
            .groupDescription(UPDATED_GROUP_DESCRIPTION)
            .groupRole(UPDATED_GROUP_ROLE)
            .groupEnabled(UPDATED_GROUP_ENABLED);

        restGroupsMockMvc.perform(put("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGroups)))
            .andExpect(status().isOk());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testGroups.getGroupDescription()).isEqualTo(UPDATED_GROUP_DESCRIPTION);
        assertThat(testGroups.getGroupRole()).isEqualTo(UPDATED_GROUP_ROLE);
        assertThat(testGroups.isGroupEnabled()).isEqualTo(UPDATED_GROUP_ENABLED);

        // Validate the Groups in Elasticsearch
        Groups groupsEs = groupsSearchRepository.findOne(testGroups.getId());
        assertThat(groupsEs).isEqualToIgnoringGivenFields(testGroups);
    }

    @Test
    @Transactional
    public void updateNonExistingGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Create the Groups

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGroupsMockMvc.perform(put("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGroups() throws Exception {
        // Initialize the database
        groupsService.save(groups);

        int databaseSizeBeforeDelete = groupsRepository.findAll().size();

        // Get the groups
        restGroupsMockMvc.perform(delete("/api/groups/{id}", groups.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean groupsExistsInEs = groupsSearchRepository.exists(groups.getId());
        assertThat(groupsExistsInEs).isFalse();

        // Validate the database is empty
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGroups() throws Exception {
        // Initialize the database
        groupsService.save(groups);

        // Search the groups
        restGroupsMockMvc.perform(get("/api/_search/groups?query=id:" + groups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].groupDescription").value(hasItem(DEFAULT_GROUP_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].groupRole").value(hasItem(DEFAULT_GROUP_ROLE.toString())))
            .andExpect(jsonPath("$.[*].groupEnabled").value(hasItem(DEFAULT_GROUP_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Groups.class);
        Groups groups1 = new Groups();
        groups1.setId(1L);
        Groups groups2 = new Groups();
        groups2.setId(groups1.getId());
        assertThat(groups1).isEqualTo(groups2);
        groups2.setId(2L);
        assertThat(groups1).isNotEqualTo(groups2);
        groups1.setId(null);
        assertThat(groups1).isNotEqualTo(groups2);
    }
}
