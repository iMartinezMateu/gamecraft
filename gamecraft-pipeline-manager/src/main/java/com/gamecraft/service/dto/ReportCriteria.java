package com.gamecraft.service.dto;

import java.io.Serializable;
import com.gamecraft.domain.enumeration.ReportStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Report entity. This class is used in ReportResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /reports?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportCriteria implements Serializable {
    /**
     * Class for filtering ReportStatus
     */
    public static class ReportStatusFilter extends Filter<ReportStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter pipelineId;

    private LocalDateFilter reportDate;

    private StringFilter reportContent;

    private ReportStatusFilter reportStatus;

    public ReportCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(LongFilter pipelineId) {
        this.pipelineId = pipelineId;
    }

    public LocalDateFilter getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateFilter reportDate) {
        this.reportDate = reportDate;
    }

    public StringFilter getReportContent() {
        return reportContent;
    }

    public void setReportContent(StringFilter reportContent) {
        this.reportContent = reportContent;
    }

    public ReportStatusFilter getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatusFilter reportStatus) {
        this.reportStatus = reportStatus;
    }

    @Override
    public String toString() {
        return "ReportCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (pipelineId != null ? "pipelineId=" + pipelineId + ", " : "") +
                (reportDate != null ? "reportDate=" + reportDate + ", " : "") +
                (reportContent != null ? "reportContent=" + reportContent + ", " : "") +
                (reportStatus != null ? "reportStatus=" + reportStatus + ", " : "") +
            "}";
    }

}
