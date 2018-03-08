package com.gamecraft.web.rest;

import com.gamecraft.GamecraftgroupApp;

import com.gamecraft.domain.GroupsUser;
import com.gamecraft.repository.GroupsUserRepository;
import com.gamecraft.service.GroupsUserService;
import com.gamecraft.repository.search.GroupsUserSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.GroupsUserCriteria;
import com.gamecraft.service.GroupsUserQueryService;

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
 * Test class for the GroupsUserResource REST controller.
 *
 * @see GroupsUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftgroupApp.class)
public class GroupsUserResourceIntTest {

    private static final Long DEFAULT_GROUP_ID = 1L;
    private static final Long UPDATED_GROUP_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private GroupsUserRepository groupsUserRepository;

    @Autowired
    private GroupsUserService groupsUserService;

    @Autowired
    private GroupsUserSearchRepository groupsUserSearchRepository;

    @Autowired
    private GroupsUserQueryService groupsUserQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGroupsUserMockMvc;

    private GroupsUser groupsUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GroupsUserResource groupsUserResource = new GroupsUserResource(groupsUserService, groupsUserQueryService);
        this.restGroupsUserMockMvc = MockMvcBuilders.standaloneSetup(groupsUserResource)
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
    public static GroupsUser createEntity(EntityManager em) {
        GroupsUser groupsUser = new GroupsUser()
            .groupId(DEFAULT_GROUP_ID)
            .userId(DEFAULT_USER_ID);
        return groupsUser;
    }

    @Before
    public void initTest() {
        groupsUserSearchRepository.deleteAll();
        groupsUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroupsUser() throws Exception {
        int databaseSizeBeforeCreate = groupsUserRepository.findAll().size();

        // Create the GroupsUser
        restGroupsUserMockMvc.perform(post("/api/groups-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupsUser)))
            .andExpect(status().isCreated());

        // Validate the GroupsUser in the database
        List<GroupsUser> groupsUserList = groupsUserRepository.findAll();
        assertThat(groupsUserList).hasSize(databaseSizeBeforeCreate + 1);
        GroupsUser testGroupsUser = groupsUserList.get(groupsUserList.size() - 1);
        assertThat(testGroupsUser.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testGroupsUser.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the GroupsUser in Elasticsearch
        GroupsUser groupsUserEs = groupsUserSearchRepository.findOne(testGroupsUser.getId());
        assertThat(groupsUserEs).isEqualToIgnoringGivenFields(testGroupsUser);
    }

    @Test
    @Transactional
    public void createGroupsUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupsUserRepository.findAll().size();

        // Create the GroupsUser with an existing ID
        groupsUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupsUserMockMvc.perform(post("/api/groups-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupsUser)))
            .andExpect(status().isBadRequest());

        // Validate the GroupsUser in the database
        List<GroupsUser> groupsUserList = groupsUserRepository.findAll();
        assertThat(groupsUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGroupIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupsUserRepository.findAll().size();
        // set the field null
        groupsUser.setGroupId(null);

        // Create the GroupsUser, which fails.

        restGroupsUserMockMvc.perform(post("/api/groups-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupsUser)))
            .andExpect(status().isBadRequest());

        List<GroupsUser> groupsUserList = groupsUserRepository.findAll();
        assertThat(groupsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupsUserRepository.findAll().size();
        // set the field null
        groupsUser.setUserId(null);

        // Create the GroupsUser, which fails.

        restGroupsUserMockMvc.perform(post("/api/groups-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupsUser)))
            .andExpect(status().isBadRequest());

        List<GroupsUser> groupsUserList = groupsUserRepository.findAll();
        assertThat(groupsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGroupsUsers() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList
        restGroupsUserMockMvc.perform(get("/api/groups-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupsUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getGroupsUser() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get the groupsUser
        restGroupsUserMockMvc.perform(get("/api/groups-users/{id}", groupsUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groupsUser.getId().intValue()))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllGroupsUsersByGroupIdIsEqualToSomething() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where groupId equals to DEFAULT_GROUP_ID
        defaultGroupsUserShouldBeFound("groupId.equals=" + DEFAULT_GROUP_ID);

        // Get all the groupsUserList where groupId equals to UPDATED_GROUP_ID
        defaultGroupsUserShouldNotBeFound("groupId.equals=" + UPDATED_GROUP_ID);
    }

    @Test
    @Transactional
    public void getAllGroupsUsersByGroupIdIsInShouldWork() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where groupId in DEFAULT_GROUP_ID or UPDATED_GROUP_ID
        defaultGroupsUserShouldBeFound("groupId.in=" + DEFAULT_GROUP_ID + "," + UPDATED_GROUP_ID);

        // Get all the groupsUserList where groupId equals to UPDATED_GROUP_ID
        defaultGroupsUserShouldNotBeFound("groupId.in=" + UPDATED_GROUP_ID);
    }

    @Test
    @Transactional
    public void getAllGroupsUsersByGroupIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where groupId is not null
        defaultGroupsUserShouldBeFound("groupId.specified=true");

        // Get all the groupsUserList where groupId is null
        defaultGroupsUserShouldNotBeFound("groupId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGroupsUsersByGroupIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where groupId greater than or equals to DEFAULT_GROUP_ID
        defaultGroupsUserShouldBeFound("groupId.greaterOrEqualThan=" + DEFAULT_GROUP_ID);

        // Get all the groupsUserList where groupId greater than or equals to UPDATED_GROUP_ID
        defaultGroupsUserShouldNotBeFound("groupId.greaterOrEqualThan=" + UPDATED_GROUP_ID);
    }

    @Test
    @Transactional
    public void getAllGroupsUsersByGroupIdIsLessThanSomething() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where groupId less than or equals to DEFAULT_GROUP_ID
        defaultGroupsUserShouldNotBeFound("groupId.lessThan=" + DEFAULT_GROUP_ID);

        // Get all the groupsUserList where groupId less than or equals to UPDATED_GROUP_ID
        defaultGroupsUserShouldBeFound("groupId.lessThan=" + UPDATED_GROUP_ID);
    }


    @Test
    @Transactional
    public void getAllGroupsUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where userId equals to DEFAULT_USER_ID
        defaultGroupsUserShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the groupsUserList where userId equals to UPDATED_USER_ID
        defaultGroupsUserShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllGroupsUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultGroupsUserShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the groupsUserList where userId equals to UPDATED_USER_ID
        defaultGroupsUserShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllGroupsUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where userId is not null
        defaultGroupsUserShouldBeFound("userId.specified=true");

        // Get all the groupsUserList where userId is null
        defaultGroupsUserShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGroupsUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where userId greater than or equals to DEFAULT_USER_ID
        defaultGroupsUserShouldBeFound("userId.greaterOrEqualThan=" + DEFAULT_USER_ID);

        // Get all the groupsUserList where userId greater than or equals to UPDATED_USER_ID
        defaultGroupsUserShouldNotBeFound("userId.greaterOrEqualThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllGroupsUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        groupsUserRepository.saveAndFlush(groupsUser);

        // Get all the groupsUserList where userId less than or equals to DEFAULT_USER_ID
        defaultGroupsUserShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the groupsUserList where userId less than or equals to UPDATED_USER_ID
        defaultGroupsUserShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGroupsUserShouldBeFound(String filter) throws Exception {
        restGroupsUserMockMvc.perform(get("/api/groups-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupsUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGroupsUserShouldNotBeFound(String filter) throws Exception {
        restGroupsUserMockMvc.perform(get("/api/groups-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingGroupsUser() throws Exception {
        // Get the groupsUser
        restGroupsUserMockMvc.perform(get("/api/groups-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupsUser() throws Exception {
        // Initialize the database
        groupsUserService.save(groupsUser);

        int databaseSizeBeforeUpdate = groupsUserRepository.findAll().size();

        // Update the groupsUser
        GroupsUser updatedGroupsUser = groupsUserRepository.findOne(groupsUser.getId());
        // Disconnect from session so that the updates on updatedGroupsUser are not directly saved in db
        em.detach(updatedGroupsUser);
        updatedGroupsUser
            .groupId(UPDATED_GROUP_ID)
            .userId(UPDATED_USER_ID);

        restGroupsUserMockMvc.perform(put("/api/groups-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGroupsUser)))
            .andExpect(status().isOk());

        // Validate the GroupsUser in the database
        List<GroupsUser> groupsUserList = groupsUserRepository.findAll();
        assertThat(groupsUserList).hasSize(databaseSizeBeforeUpdate);
        GroupsUser testGroupsUser = groupsUserList.get(groupsUserList.size() - 1);
        assertThat(testGroupsUser.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testGroupsUser.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the GroupsUser in Elasticsearch
        GroupsUser groupsUserEs = groupsUserSearchRepository.findOne(testGroupsUser.getId());
        assertThat(groupsUserEs).isEqualToIgnoringGivenFields(testGroupsUser);
    }

    @Test
    @Transactional
    public void updateNonExistingGroupsUser() throws Exception {
        int databaseSizeBeforeUpdate = groupsUserRepository.findAll().size();

        // Create the GroupsUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGroupsUserMockMvc.perform(put("/api/groups-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupsUser)))
            .andExpect(status().isCreated());

        // Validate the GroupsUser in the database
        List<GroupsUser> groupsUserList = groupsUserRepository.findAll();
        assertThat(groupsUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGroupsUser() throws Exception {
        // Initialize the database
        groupsUserService.save(groupsUser);

        int databaseSizeBeforeDelete = groupsUserRepository.findAll().size();

        // Get the groupsUser
        restGroupsUserMockMvc.perform(delete("/api/groups-users/{id}", groupsUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean groupsUserExistsInEs = groupsUserSearchRepository.exists(groupsUser.getId());
        assertThat(groupsUserExistsInEs).isFalse();

        // Validate the database is empty
        List<GroupsUser> groupsUserList = groupsUserRepository.findAll();
        assertThat(groupsUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGroupsUser() throws Exception {
        // Initialize the database
        groupsUserService.save(groupsUser);

        // Search the groupsUser
        restGroupsUserMockMvc.perform(get("/api/_search/groups-users?query=id:" + groupsUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupsUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupsUser.class);
        GroupsUser groupsUser1 = new GroupsUser();
        groupsUser1.setId(1L);
        GroupsUser groupsUser2 = new GroupsUser();
        groupsUser2.setId(groupsUser1.getId());
        assertThat(groupsUser1).isEqualTo(groupsUser2);
        groupsUser2.setId(2L);
        assertThat(groupsUser1).isNotEqualTo(groupsUser2);
        groupsUser1.setId(null);
        assertThat(groupsUser1).isNotEqualTo(groupsUser2);
    }
}
