package com.gamecraft.service.dto;

import java.io.Serializable;
import com.gamecraft.domain.enumeration.PipelineRepositoryType;
import com.gamecraft.domain.enumeration.PipelineNotificatorType;
import com.gamecraft.domain.enumeration.PipelinePublicationService;
import com.gamecraft.domain.enumeration.PipelineStatus;
import com.gamecraft.domain.enumeration.PipelineScheduleType;
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

    /**
     * Class for filtering PipelinePublicationService
     */
    public static class PipelinePublicationServiceFilter extends Filter<PipelinePublicationService> {
    }

    /**
     * Class for filtering PipelineStatus
     */
    public static class PipelineStatusFilter extends Filter<PipelineStatus> {
    }

    /**
     * Class for filtering PipelineScheduleType
     */
    public static class PipelineScheduleTypeFilter extends Filter<PipelineScheduleType> {
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

    private StringFilter pipelineDropboxAppKey;

    private StringFilter pipelineDropboxToken;

    private PipelinePublicationServiceFilter pipelinePublicationService;

    private IntegerFilter pipelineFtpPort;

    private PipelineStatusFilter pipelineStatus;

    private StringFilter pipelineProjectName;

    private PipelineScheduleTypeFilter pipelineScheduleType;

    private StringFilter pipelineScheduleCronJob;

    private StringFilter pipelineRepositoryBranch;

    private StringFilter pipelineNotificatorRecipient;

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

    public StringFilter getPipelineDropboxAppKey() {
        return pipelineDropboxAppKey;
    }

    public void setPipelineDropboxAppKey(StringFilter pipelineDropboxAppKey) {
        this.pipelineDropboxAppKey = pipelineDropboxAppKey;
    }

    public StringFilter getPipelineDropboxToken() {
        return pipelineDropboxToken;
    }

    public void setPipelineDropboxToken(StringFilter pipelineDropboxToken) {
        this.pipelineDropboxToken = pipelineDropboxToken;
    }

    public PipelinePublicationServiceFilter getPipelinePublicationService() {
        return pipelinePublicationService;
    }

    public void setPipelinePublicationService(PipelinePublicationServiceFilter pipelinePublicationService) {
        this.pipelinePublicationService = pipelinePublicationService;
    }

    public IntegerFilter getPipelineFtpPort() {
        return pipelineFtpPort;
    }

    public void setPipelineFtpPort(IntegerFilter pipelineFtpPort) {
        this.pipelineFtpPort = pipelineFtpPort;
    }

    public PipelineStatusFilter getPipelineStatus() {
        return pipelineStatus;
    }

    public void setPipelineStatus(PipelineStatusFilter pipelineStatus) {
        this.pipelineStatus = pipelineStatus;
    }

    public StringFilter getPipelineProjectName() {
        return pipelineProjectName;
    }

    public void setPipelineProjectName(StringFilter pipelineProjectName) {
        this.pipelineProjectName = pipelineProjectName;
    }

    public PipelineScheduleTypeFilter getPipelineScheduleType() {
        return pipelineScheduleType;
    }

    public void setPipelineScheduleType(PipelineScheduleTypeFilter pipelineScheduleType) {
        this.pipelineScheduleType = pipelineScheduleType;
    }

    public StringFilter getPipelineScheduleCronJob() {
        return pipelineScheduleCronJob;
    }

    public void setPipelineScheduleCronJob(StringFilter pipelineScheduleCronJob) {
        this.pipelineScheduleCronJob = pipelineScheduleCronJob;
    }

    public StringFilter getPipelineRepositoryBranch() {
        return pipelineRepositoryBranch;
    }

    public void setPipelineRepositoryBranch(StringFilter pipelineRepositoryBranch) {
        this.pipelineRepositoryBranch = pipelineRepositoryBranch;
    }

    public StringFilter getPipelineNotificatorRecipient() {
        return pipelineNotificatorRecipient;
    }

    public void setPipelineNotificatorRecipient(StringFilter pipelineNotificatorRecipient) {
        this.pipelineNotificatorRecipient = pipelineNotificatorRecipient;
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
                (pipelineDropboxAppKey != null ? "pipelineDropboxAppKey=" + pipelineDropboxAppKey + ", " : "") +
                (pipelineDropboxToken != null ? "pipelineDropboxToken=" + pipelineDropboxToken + ", " : "") +
                (pipelinePublicationService != null ? "pipelinePublicationService=" + pipelinePublicationService + ", " : "") +
                (pipelineFtpPort != null ? "pipelineFtpPort=" + pipelineFtpPort + ", " : "") +
                (pipelineStatus != null ? "pipelineStatus=" + pipelineStatus + ", " : "") +
                (pipelineProjectName != null ? "pipelineProjectName=" + pipelineProjectName + ", " : "") +
                (pipelineScheduleType != null ? "pipelineScheduleType=" + pipelineScheduleType + ", " : "") +
                (pipelineScheduleCronJob != null ? "pipelineScheduleCronJob=" + pipelineScheduleCronJob + ", " : "") +
                (pipelineRepositoryBranch != null ? "pipelineRepositoryBranch=" + pipelineRepositoryBranch + ", " : "") +
                (pipelineNotificatorRecipient != null ? "pipelineNotificatorRecipient=" + pipelineNotificatorRecipient + ", " : "") +
            "}";
    }

}
