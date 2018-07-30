package com.gamecraft.service.dto;

import java.io.Serializable;
import com.gamecraft.domain.enumeration.PipelineRepositoryType;
import com.gamecraft.domain.enumeration.PipelineNotificatorType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Pipeline entity. This class is used in PipelineResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /pipelines?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PipelineCriteria implements Serializable {
    /**
     * Class for filtering PipelineRepositoryType
     */
    public static class PipelineRepositoryTypeFilter extends Filter<PipelineRepositoryType> {
    }

    /**
     * Class for filtering PipelineNotificatorType
     */
    public static class PipelineNotificatorTypeFilter extends Filter<PipelineNotificatorType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter pipelineName;

    private StringFilter pipelineDescription;

    private LongFilter pipelineProjectId;

    private StringFilter pipelineRepositoryAddress;

    private StringFilter pipelineRepositoryUsername;

    private StringFilter pipelineRepositoryPassword;

    private StringFilter pipelineEngineCompilerPath;

    private StringFilter pipelineEngineCompilerArguments;

    private StringFilter pipelineFtpAddress;

    private StringFilter pipelineFtpUsername;

    private StringFilter pipelineFtpPassword;

    private StringFilter pipelineNotificatorDetails;

    private PipelineRepositoryTypeFilter pipelineRepositoryType;

    private PipelineNotificatorTypeFilter pipelineNotificatorType;

    public PipelineCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(StringFilter pipelineName) {
        this.pipelineName = pipelineName;
    }

    public StringFilter getPipelineDescription() {
        return pipelineDescription;
    }

    public void setPipelineDescription(StringFilter pipelineDescription) {
        this.pipelineDescription = pipelineDescription;
    }

    public LongFilter getPipelineProjectId() {
        return pipelineProjectId;
    }

    public void setPipelineProjectId(LongFilter pipelineProjectId) {
        this.pipelineProjectId = pipelineProjectId;
    }

    public StringFilter getPipelineRepositoryAddress() {
        return pipelineRepositoryAddress;
    }

    public void setPipelineRepositoryAddress(StringFilter pipelineRepositoryAddress) {
        this.pipelineRepositoryAddress = pipelineRepositoryAddress;
    }

    public StringFilter getPipelineRepositoryUsername() {
        return pipelineRepositoryUsername;
    }

    public void setPipelineRepositoryUsername(StringFilter pipelineRepositoryUsername) {
        this.pipelineRepositoryUsername = pipelineRepositoryUsername;
    }

    public StringFilter getPipelineRepositoryPassword() {
        return pipelineRepositoryPassword;
    }

    public void setPipelineRepositoryPassword(StringFilter pipelineRepositoryPassword) {
        this.pipelineRepositoryPassword = pipelineRepositoryPassword;
    }

    public StringFilter getPipelineEngineCompilerPath() {
        return pipelineEngineCompilerPath;
    }

    public void setPipelineEngineCompilerPath(StringFilter pipelineEngineCompilerPath) {
        this.pipelineEngineCompilerPath = pipelineEngineCompilerPath;
    }

    public StringFilter getPipelineEngineCompilerArguments() {
        return pipelineEngineCompilerArguments;
    }

    public void setPipelineEngineCompilerArguments(StringFilter pipelineEngineCompilerArguments) {
        this.pipelineEngineCompilerArguments = pipelineEngineCompilerArguments;
    }

    public StringFilter getPipelineFtpAddress() {
        return pipelineFtpAddress;
    }

    public void setPipelineFtpAddress(StringFilter pipelineFtpAddress) {
        this.pipelineFtpAddress = pipelineFtpAddress;
    }

    public StringFilter getPipelineFtpUsername() {
        return pipelineFtpUsername;
    }

    public void setPipelineFtpUsername(StringFilter pipelineFtpUsername) {
        this.pipelineFtpUsername = pipelineFtpUsername;
    }

    public StringFilter getPipelineFtpPassword() {
        return pipelineFtpPassword;
    }

    public void setPipelineFtpPassword(StringFilter pipelineFtpPassword) {
        this.pipelineFtpPassword = pipelineFtpPassword;
    }

    public StringFilter getPipelineNotificatorDetails() {
        return pipelineNotificatorDetails;
    }

    public void setPipelineNotificatorDetails(StringFilter pipelineNotificatorDetails) {
        this.pipelineNotificatorDetails = pipelineNotificatorDetails;
    }

    public PipelineRepositoryTypeFilter getPipelineRepositoryType() {
        return pipelineRepositoryType;
    }

    public void setPipelineRepositoryType(PipelineRepositoryTypeFilter pipelineRepositoryType) {
        this.pipelineRepositoryType = pipelineRepositoryType;
    }

    public PipelineNotificatorTypeFilter getPipelineNotificatorType() {
        return pipelineNotificatorType;
    }

    public void setPipelineNotificatorType(PipelineNotificatorTypeFilter pipelineNotificatorType) {
        this.pipelineNotificatorType = pipelineNotificatorType;
    }

    @Override
    public String toString() {
        return "PipelineCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (pipelineName != null ? "pipelineName=" + pipelineName + ", " : "") +
                (pipelineDescription != null ? "pipelineDescription=" + pipelineDescription + ", " : "") +
                (pipelineProjectId != null ? "pipelineProjectId=" + pipelineProjectId + ", " : "") +
                (pipelineRepositoryAddress != null ? "pipelineRepositoryAddress=" + pipelineRepositoryAddress + ", " : "") +
                (pipelineRepositoryUsername != null ? "pipelineRepositoryUsername=" + pipelineRepositoryUsername + ", " : "") +
                (pipelineRepositoryPassword != null ? "pipelineRepositoryPassword=" + pipelineRepositoryPassword + ", " : "") +
                (pipelineEngineCompilerPath != null ? "pipelineEngineCompilerPath=" + pipelineEngineCompilerPath + ", " : "") +
                (pipelineEngineCompilerArguments != null ? "pipelineEngineCompilerArguments=" + pipelineEngineCompilerArguments + ", " : "") +
                (pipelineFtpAddress != null ? "pipelineFtpAddress=" + pipelineFtpAddress + ", " : "") +
                (pipelineFtpUsername != null ? "pipelineFtpUsername=" + pipelineFtpUsername + ", " : "") +
                (pipelineFtpPassword != null ? "pipelineFtpPassword=" + pipelineFtpPassword + ", " : "") +
                (pipelineNotificatorDetails != null ? "pipelineNotificatorDetails=" + pipelineNotificatorDetails + ", " : "") +
                (pipelineRepositoryType != null ? "pipelineRepositoryType=" + pipelineRepositoryType + ", " : "") +
                (pipelineNotificatorType != null ? "pipelineNotificatorType=" + pipelineNotificatorType + ", " : "") +
            "}";
    }

}
