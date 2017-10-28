package com.gamecraft.web.rest;

import com.gamecraft.GamecraftprojectApp;

import com.gamecraft.domain.Project;
import com.gamecraft.repository.ProjectRepository;
import com.gamecraft.service.ProjectService;
import com.gamecraft.repository.search.ProjectSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.ProjectCriteria;
import com.gamecraft.service.ProjectQueryService;

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
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftprojectApp.class)
public class ProjectResourceIntTest {

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_LOGO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PROJECT_ARCHIVED = false;
    private static final Boolean UPDATED_PROJECT_ARCHIVED = true;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectSearchRepository projectSearchRepository;

    @Autowired
    private ProjectQueryService projectQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectMockMvc;

    private Project project;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectResource projectResource = new ProjectResource(projectService, projectQueryService);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
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
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .projectName(DEFAULT_PROJECT_NAME)
            .projectDescription(DEFAULT_PROJECT_DESCRIPTION)
            .projectWebsite(DEFAULT_PROJECT_WEBSITE)
            .projectLogo(DEFAULT_PROJECT_LOGO)
            .projectArchived(DEFAULT_PROJECT_ARCHIVED);
        return project;
    }

    @Before
    public void initTest() {
        projectSearchRepository.deleteAll();
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testProject.getProjectDescription()).isEqualTo(DEFAULT_PROJECT_DESCRIPTION);
        assertThat(testProject.getProjectWebsite()).isEqualTo(DEFAULT_PROJECT_WEBSITE);
        assertThat(testProject.getProjectLogo()).isEqualTo(DEFAULT_PROJECT_LOGO);
        assertThat(testProject.isProjectArchived()).isEqualTo(DEFAULT_PROJECT_ARCHIVED);

        // Validate the Project in Elasticsearch
        Project projectEs = projectSearchRepository.findOne(testProject.getId());
        assertThat(projectEs).isEqualToComparingFieldByField(testProject);
    }

    @Test
    @Transactional
    public void createProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project with an existing ID
        project.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setProjectName(null);

        // Create the Project, which fails.

        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME.toString())))
            .andExpect(jsonPath("$.[*].projectDescription").value(hasItem(DEFAULT_PROJECT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].projectWebsite").value(hasItem(DEFAULT_PROJECT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].projectLogo").value(hasItem(DEFAULT_PROJECT_LOGO.toString())))
            .andExpect(jsonPath("$.[*].projectArchived").value(hasItem(DEFAULT_PROJECT_ARCHIVED.booleanValue())));
    }

    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME.toString()))
            .andExpect(jsonPath("$.projectDescription").value(DEFAULT_PROJECT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.projectWebsite").value(DEFAULT_PROJECT_WEBSITE.toString()))
            .andExpect(jsonPath("$.projectLogo").value(DEFAULT_PROJECT_LOGO.toString()))
            .andExpect(jsonPath("$.projectArchived").value(DEFAULT_PROJECT_ARCHIVED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName equals to DEFAULT_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.equals=" + DEFAULT_PROJECT_NAME);

        // Get all the projectList where projectName equals to UPDATED_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.equals=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName in DEFAULT_PROJECT_NAME or UPDATED_PROJECT_NAME
        defaultProjectShouldBeFound("projectName.in=" + DEFAULT_PROJECT_NAME + "," + UPDATED_PROJECT_NAME);

        // Get all the projectList where projectName equals to UPDATED_PROJECT_NAME
        defaultProjectShouldNotBeFound("projectName.in=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName is not null
        defaultProjectShouldBeFound("projectName.specified=true");

        // Get all the projectList where projectName is null
        defaultProjectShouldNotBeFound("projectName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectDescription equals to DEFAULT_PROJECT_DESCRIPTION
        defaultProjectShouldBeFound("projectDescription.equals=" + DEFAULT_PROJECT_DESCRIPTION);

        // Get all the projectList where projectDescription equals to UPDATED_PROJECT_DESCRIPTION
        defaultProjectShouldNotBeFound("projectDescription.equals=" + UPDATED_PROJECT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectDescription in DEFAULT_PROJECT_DESCRIPTION or UPDATED_PROJECT_DESCRIPTION
        defaultProjectShouldBeFound("projectDescription.in=" + DEFAULT_PROJECT_DESCRIPTION + "," + UPDATED_PROJECT_DESCRIPTION);

        // Get all the projectList where projectDescription equals to UPDATED_PROJECT_DESCRIPTION
        defaultProjectShouldNotBeFound("projectDescription.in=" + UPDATED_PROJECT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectDescription is not null
        defaultProjectShouldBeFound("projectDescription.specified=true");

        // Get all the projectList where projectDescription is null
        defaultProjectShouldNotBeFound("projectDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectWebsite equals to DEFAULT_PROJECT_WEBSITE
        defaultProjectShouldBeFound("projectWebsite.equals=" + DEFAULT_PROJECT_WEBSITE);

        // Get all the projectList where projectWebsite equals to UPDATED_PROJECT_WEBSITE
        defaultProjectShouldNotBeFound("projectWebsite.equals=" + UPDATED_PROJECT_WEBSITE);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectWebsite in DEFAULT_PROJECT_WEBSITE or UPDATED_PROJECT_WEBSITE
        defaultProjectShouldBeFound("projectWebsite.in=" + DEFAULT_PROJECT_WEBSITE + "," + UPDATED_PROJECT_WEBSITE);

        // Get all the projectList where projectWebsite equals to UPDATED_PROJECT_WEBSITE
        defaultProjectShouldNotBeFound("projectWebsite.in=" + UPDATED_PROJECT_WEBSITE);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectWebsite is not null
        defaultProjectShouldBeFound("projectWebsite.specified=true");

        // Get all the projectList where projectWebsite is null
        defaultProjectShouldNotBeFound("projectWebsite.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectLogoIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectLogo equals to DEFAULT_PROJECT_LOGO
        defaultProjectShouldBeFound("projectLogo.equals=" + DEFAULT_PROJECT_LOGO);

        // Get all the projectList where projectLogo equals to UPDATED_PROJECT_LOGO
        defaultProjectShouldNotBeFound("projectLogo.equals=" + UPDATED_PROJECT_LOGO);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectLogoIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectLogo in DEFAULT_PROJECT_LOGO or UPDATED_PROJECT_LOGO
        defaultProjectShouldBeFound("projectLogo.in=" + DEFAULT_PROJECT_LOGO + "," + UPDATED_PROJECT_LOGO);

        // Get all the projectList where projectLogo equals to UPDATED_PROJECT_LOGO
        defaultProjectShouldNotBeFound("projectLogo.in=" + UPDATED_PROJECT_LOGO);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectLogoIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectLogo is not null
        defaultProjectShouldBeFound("projectLogo.specified=true");

        // Get all the projectList where projectLogo is null
        defaultProjectShouldNotBeFound("projectLogo.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectArchivedIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectArchived equals to DEFAULT_PROJECT_ARCHIVED
        defaultProjectShouldBeFound("projectArchived.equals=" + DEFAULT_PROJECT_ARCHIVED);

        // Get all the projectList where projectArchived equals to UPDATED_PROJECT_ARCHIVED
        defaultProjectShouldNotBeFound("projectArchived.equals=" + UPDATED_PROJECT_ARCHIVED);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectArchivedIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectArchived in DEFAULT_PROJECT_ARCHIVED or UPDATED_PROJECT_ARCHIVED
        defaultProjectShouldBeFound("projectArchived.in=" + DEFAULT_PROJECT_ARCHIVED + "," + UPDATED_PROJECT_ARCHIVED);

        // Get all the projectList where projectArchived equals to UPDATED_PROJECT_ARCHIVED
        defaultProjectShouldNotBeFound("projectArchived.in=" + UPDATED_PROJECT_ARCHIVED);
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectArchivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectArchived is not null
        defaultProjectShouldBeFound("projectArchived.specified=true");

        // Get all the projectList where projectArchived is null
        defaultProjectShouldNotBeFound("projectArchived.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectShouldBeFound(String filter) throws Exception {
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME.toString())))
            .andExpect(jsonPath("$.[*].projectDescription").value(hasItem(DEFAULT_PROJECT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].projectWebsite").value(hasItem(DEFAULT_PROJECT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].projectLogo").value(hasItem(DEFAULT_PROJECT_LOGO.toString())))
            .andExpect(jsonPath("$.[*].projectArchived").value(hasItem(DEFAULT_PROJECT_ARCHIVED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectShouldNotBeFound(String filter) throws Exception {
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findOne(project.getId());
        updatedProject
            .projectName(UPDATED_PROJECT_NAME)
            .projectDescription(UPDATED_PROJECT_DESCRIPTION)
            .projectWebsite(UPDATED_PROJECT_WEBSITE)
            .projectLogo(UPDATED_PROJECT_LOGO)
            .projectArchived(UPDATED_PROJECT_ARCHIVED);

        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProject.getProjectDescription()).isEqualTo(UPDATED_PROJECT_DESCRIPTION);
        assertThat(testProject.getProjectWebsite()).isEqualTo(UPDATED_PROJECT_WEBSITE);
        assertThat(testProject.getProjectLogo()).isEqualTo(UPDATED_PROJECT_LOGO);
        assertThat(testProject.isProjectArchived()).isEqualTo(UPDATED_PROJECT_ARCHIVED);

        // Validate the Project in Elasticsearch
        Project projectEs = projectSearchRepository.findOne(testProject.getId());
        assertThat(projectEs).isEqualToComparingFieldByField(testProject);
    }

    @Test
    @Transactional
    public void updateNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Create the Project

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Get the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean projectExistsInEs = projectSearchRepository.exists(project.getId());
        assertThat(projectExistsInEs).isFalse();

        // Validate the database is empty
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        // Search the project
        restProjectMockMvc.perform(get("/api/_search/projects?query=id:" + project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME.toString())))
            .andExpect(jsonPath("$.[*].projectDescription").value(hasItem(DEFAULT_PROJECT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].projectWebsite").value(hasItem(DEFAULT_PROJECT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].projectLogo").value(hasItem(DEFAULT_PROJECT_LOGO.toString())))
            .andExpect(jsonPath("$.[*].projectArchived").value(hasItem(DEFAULT_PROJECT_ARCHIVED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = new Project();
        project1.setId(1L);
        Project project2 = new Project();
        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);
        project2.setId(2L);
        assertThat(project1).isNotEqualTo(project2);
        project1.setId(null);
        assertThat(project1).isNotEqualTo(project2);
    }
}
