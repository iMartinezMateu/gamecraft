package com.gamecraft.web.rest;

import com.gamecraft.GamecraftpipelinemanagerApp;

import com.gamecraft.domain.Report;
import com.gamecraft.repository.ReportRepository;
import com.gamecraft.service.ReportService;
import com.gamecraft.repository.search.ReportSearchRepository;
import com.gamecraft.web.rest.errors.ExceptionTranslator;
import com.gamecraft.service.dto.ReportCriteria;
import com.gamecraft.service.ReportQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.gamecraft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gamecraft.domain.enumeration.ReportStatus;
/**
 * Test class for the ReportResource REST controller.
 *
 * @see ReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GamecraftpipelinemanagerApp.class)
public class ReportResourceIntTest {

    private static final Long DEFAULT_PIPELINE_ID = 1L;
    private static final Long UPDATED_PIPELINE_ID = 2L;

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REPORT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_CONTENT = "BBBBBBBBBB";

    private static final ReportStatus DEFAULT_REPORT_STATUS = ReportStatus.FAIL;
    private static final ReportStatus UPDATED_REPORT_STATUS = ReportStatus.GOOD;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportSearchRepository reportSearchRepository;

    @Autowired
    private ReportQueryService reportQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportMockMvc;

    private Report report;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReportResource reportResource = new ReportResource(reportService, reportQueryService);
        this.restReportMockMvc = MockMvcBuilders.standaloneSetup(reportResource)
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
    public static Report createEntity(EntityManager em) {
        Report report = new Report()
            .pipelineId(DEFAULT_PIPELINE_ID)
            .reportDate(DEFAULT_REPORT_DATE)
            .reportContent(DEFAULT_REPORT_CONTENT)
            .reportStatus(DEFAULT_REPORT_STATUS);
        return report;
    }

    @Before
    public void initTest() {
        reportSearchRepository.deleteAll();
        report = createEntity(em);
    }

    @Test
    @Transactional
    public void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // Create the Report
        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getPipelineId()).isEqualTo(DEFAULT_PIPELINE_ID);
        assertThat(testReport.getReportDate()).isEqualTo(DEFAULT_REPORT_DATE);
        assertThat(testReport.getReportContent()).isEqualTo(DEFAULT_REPORT_CONTENT);
        assertThat(testReport.getReportStatus()).isEqualTo(DEFAULT_REPORT_STATUS);

        // Validate the Report in Elasticsearch
        Report reportEs = reportSearchRepository.findOne(testReport.getId());
        assertThat(reportEs).isEqualToIgnoringGivenFields(testReport);
    }

    @Test
    @Transactional
    public void createReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // Create the Report with an existing ID
        report.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPipelineIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setPipelineId(null);

        // Create the Report, which fails.

        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReportDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setReportDate(null);

        // Create the Report, which fails.

        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReportContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setReportContent(null);

        // Create the Report, which fails.

        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList
        restReportMockMvc.perform(get("/api/reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].pipelineId").value(hasItem(DEFAULT_PIPELINE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportContent").value(hasItem(DEFAULT_REPORT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
            .andExpect(jsonPath("$.pipelineId").value(DEFAULT_PIPELINE_ID.intValue()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.reportContent").value(DEFAULT_REPORT_CONTENT.toString()))
            .andExpect(jsonPath("$.reportStatus").value(DEFAULT_REPORT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllReportsByPipelineIdIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where pipelineId equals to DEFAULT_PIPELINE_ID
        defaultReportShouldBeFound("pipelineId.equals=" + DEFAULT_PIPELINE_ID);

        // Get all the reportList where pipelineId equals to UPDATED_PIPELINE_ID
        defaultReportShouldNotBeFound("pipelineId.equals=" + UPDATED_PIPELINE_ID);
    }

    @Test
    @Transactional
    public void getAllReportsByPipelineIdIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where pipelineId in DEFAULT_PIPELINE_ID or UPDATED_PIPELINE_ID
        defaultReportShouldBeFound("pipelineId.in=" + DEFAULT_PIPELINE_ID + "," + UPDATED_PIPELINE_ID);

        // Get all the reportList where pipelineId equals to UPDATED_PIPELINE_ID
        defaultReportShouldNotBeFound("pipelineId.in=" + UPDATED_PIPELINE_ID);
    }

    @Test
    @Transactional
    public void getAllReportsByPipelineIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where pipelineId is not null
        defaultReportShouldBeFound("pipelineId.specified=true");

        // Get all the reportList where pipelineId is null
        defaultReportShouldNotBeFound("pipelineId.specified=false");
    }

    @Test
    @Transactional
    public void getAllReportsByPipelineIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where pipelineId greater than or equals to DEFAULT_PIPELINE_ID
        defaultReportShouldBeFound("pipelineId.greaterOrEqualThan=" + DEFAULT_PIPELINE_ID);

        // Get all the reportList where pipelineId greater than or equals to UPDATED_PIPELINE_ID
        defaultReportShouldNotBeFound("pipelineId.greaterOrEqualThan=" + UPDATED_PIPELINE_ID);
    }

    @Test
    @Transactional
    public void getAllReportsByPipelineIdIsLessThanSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where pipelineId less than or equals to DEFAULT_PIPELINE_ID
        defaultReportShouldNotBeFound("pipelineId.lessThan=" + DEFAULT_PIPELINE_ID);

        // Get all the reportList where pipelineId less than or equals to UPDATED_PIPELINE_ID
        defaultReportShouldBeFound("pipelineId.lessThan=" + UPDATED_PIPELINE_ID);
    }


    @Test
    @Transactional
    public void getAllReportsByReportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportDate equals to DEFAULT_REPORT_DATE
        defaultReportShouldBeFound("reportDate.equals=" + DEFAULT_REPORT_DATE);

        // Get all the reportList where reportDate equals to UPDATED_REPORT_DATE
        defaultReportShouldNotBeFound("reportDate.equals=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    public void getAllReportsByReportDateIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportDate in DEFAULT_REPORT_DATE or UPDATED_REPORT_DATE
        defaultReportShouldBeFound("reportDate.in=" + DEFAULT_REPORT_DATE + "," + UPDATED_REPORT_DATE);

        // Get all the reportList where reportDate equals to UPDATED_REPORT_DATE
        defaultReportShouldNotBeFound("reportDate.in=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    public void getAllReportsByReportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportDate is not null
        defaultReportShouldBeFound("reportDate.specified=true");

        // Get all the reportList where reportDate is null
        defaultReportShouldNotBeFound("reportDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllReportsByReportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportDate greater than or equals to DEFAULT_REPORT_DATE
        defaultReportShouldBeFound("reportDate.greaterOrEqualThan=" + DEFAULT_REPORT_DATE);

        // Get all the reportList where reportDate greater than or equals to UPDATED_REPORT_DATE
        defaultReportShouldNotBeFound("reportDate.greaterOrEqualThan=" + UPDATED_REPORT_DATE);
    }

    @Test
    @Transactional
    public void getAllReportsByReportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportDate less than or equals to DEFAULT_REPORT_DATE
        defaultReportShouldNotBeFound("reportDate.lessThan=" + DEFAULT_REPORT_DATE);

        // Get all the reportList where reportDate less than or equals to UPDATED_REPORT_DATE
        defaultReportShouldBeFound("reportDate.lessThan=" + UPDATED_REPORT_DATE);
    }


    @Test
    @Transactional
    public void getAllReportsByReportContentIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportContent equals to DEFAULT_REPORT_CONTENT
        defaultReportShouldBeFound("reportContent.equals=" + DEFAULT_REPORT_CONTENT);

        // Get all the reportList where reportContent equals to UPDATED_REPORT_CONTENT
        defaultReportShouldNotBeFound("reportContent.equals=" + UPDATED_REPORT_CONTENT);
    }

    @Test
    @Transactional
    public void getAllReportsByReportContentIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportContent in DEFAULT_REPORT_CONTENT or UPDATED_REPORT_CONTENT
        defaultReportShouldBeFound("reportContent.in=" + DEFAULT_REPORT_CONTENT + "," + UPDATED_REPORT_CONTENT);

        // Get all the reportList where reportContent equals to UPDATED_REPORT_CONTENT
        defaultReportShouldNotBeFound("reportContent.in=" + UPDATED_REPORT_CONTENT);
    }

    @Test
    @Transactional
    public void getAllReportsByReportContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportContent is not null
        defaultReportShouldBeFound("reportContent.specified=true");

        // Get all the reportList where reportContent is null
        defaultReportShouldNotBeFound("reportContent.specified=false");
    }

    @Test
    @Transactional
    public void getAllReportsByReportStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportStatus equals to DEFAULT_REPORT_STATUS
        defaultReportShouldBeFound("reportStatus.equals=" + DEFAULT_REPORT_STATUS);

        // Get all the reportList where reportStatus equals to UPDATED_REPORT_STATUS
        defaultReportShouldNotBeFound("reportStatus.equals=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    public void getAllReportsByReportStatusIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportStatus in DEFAULT_REPORT_STATUS or UPDATED_REPORT_STATUS
        defaultReportShouldBeFound("reportStatus.in=" + DEFAULT_REPORT_STATUS + "," + UPDATED_REPORT_STATUS);

        // Get all the reportList where reportStatus equals to UPDATED_REPORT_STATUS
        defaultReportShouldNotBeFound("reportStatus.in=" + UPDATED_REPORT_STATUS);
    }

    @Test
    @Transactional
    public void getAllReportsByReportStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportStatus is not null
        defaultReportShouldBeFound("reportStatus.specified=true");

        // Get all the reportList where reportStatus is null
        defaultReportShouldNotBeFound("reportStatus.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReportShouldBeFound(String filter) throws Exception {
        restReportMockMvc.perform(get("/api/reports?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].pipelineId").value(hasItem(DEFAULT_PIPELINE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportContent").value(hasItem(DEFAULT_REPORT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReportShouldNotBeFound(String filter) throws Exception {
        restReportMockMvc.perform(get("/api/reports?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReport() throws Exception {
        // Initialize the database
        reportService.save(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        Report updatedReport = reportRepository.findOne(report.getId());
        // Disconnect from session so that the updates on updatedReport are not directly saved in db
        em.detach(updatedReport);
        updatedReport
            .pipelineId(UPDATED_PIPELINE_ID)
            .reportDate(UPDATED_REPORT_DATE)
            .reportContent(UPDATED_REPORT_CONTENT)
            .reportStatus(UPDATED_REPORT_STATUS);

        restReportMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReport)))
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getPipelineId()).isEqualTo(UPDATED_PIPELINE_ID);
        assertThat(testReport.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testReport.getReportContent()).isEqualTo(UPDATED_REPORT_CONTENT);
        assertThat(testReport.getReportStatus()).isEqualTo(UPDATED_REPORT_STATUS);

        // Validate the Report in Elasticsearch
        Report reportEs = reportSearchRepository.findOne(testReport.getId());
        assertThat(reportEs).isEqualToIgnoringGivenFields(testReport);
    }

    @Test
    @Transactional
    public void updateNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Create the Report

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReport() throws Exception {
        // Initialize the database
        reportService.save(report);

        int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Get the report
        restReportMockMvc.perform(delete("/api/reports/{id}", report.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reportExistsInEs = reportSearchRepository.exists(report.getId());
        assertThat(reportExistsInEs).isFalse();

        // Validate the database is empty
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReport() throws Exception {
        // Initialize the database
        reportService.save(report);

        // Search the report
        restReportMockMvc.perform(get("/api/_search/reports?query=id:" + report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].pipelineId").value(hasItem(DEFAULT_PIPELINE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportContent").value(hasItem(DEFAULT_REPORT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].reportStatus").value(hasItem(DEFAULT_REPORT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Report.class);
        Report report1 = new Report();
        report1.setId(1L);
        Report report2 = new Report();
        report2.setId(report1.getId());
        assertThat(report1).isEqualTo(report2);
        report2.setId(2L);
        assertThat(report1).isNotEqualTo(report2);
        report1.setId(null);
        assertThat(report1).isNotEqualTo(report2);
    }
}
