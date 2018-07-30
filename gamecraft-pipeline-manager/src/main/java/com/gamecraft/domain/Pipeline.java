package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.gamecraft.domain.enumeration.PipelineRepositoryType;

import com.gamecraft.domain.enumeration.PipelineNotificatorType;

/**
 * A Pipeline.
 */
@Entity
@Table(name = "pipeline")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pipeline")
public class Pipeline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "pipeline_name", length = 100, nullable = false)
    private String pipelineName;

    @Column(name = "pipeline_description")
    private String pipelineDescription;

    @NotNull
    @Column(name = "pipeline_project_id", nullable = false)
    private Long pipelineProjectId;

    @Column(name = "pipeline_repository_address")
    private String pipelineRepositoryAddress;

    @Column(name = "pipeline_repository_username")
    private String pipelineRepositoryUsername;

    @Column(name = "pipeline_repository_password")
    private String pipelineRepositoryPassword;

    @Column(name = "pipeline_engine_compiler_path")
    private String pipelineEngineCompilerPath;

    @Column(name = "pipeline_engine_compiler_arguments")
    private String pipelineEngineCompilerArguments;

    @Column(name = "pipeline_ftp_address")
    private String pipelineFtpAddress;

    @Column(name = "pipeline_ftp_username")
    private String pipelineFtpUsername;

    @Column(name = "pipeline_ftp_password")
    private String pipelineFtpPassword;

    @Column(name = "pipeline_notificator_details")
    private String pipelineNotificatorDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "pipeline_repository_type")
    private PipelineRepositoryType pipelineRepositoryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "pipeline_notificator_type")
    private PipelineNotificatorType pipelineNotificatorType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public Pipeline pipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
        return this;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getPipelineDescription() {
        return pipelineDescription;
    }

    public Pipeline pipelineDescription(String pipelineDescription) {
        this.pipelineDescription = pipelineDescription;
        return this;
    }

    public void setPipelineDescription(String pipelineDescription) {
        this.pipelineDescription = pipelineDescription;
    }

    public Long getPipelineProjectId() {
        return pipelineProjectId;
    }

    public Pipeline pipelineProjectId(Long pipelineProjectId) {
        this.pipelineProjectId = pipelineProjectId;
        return this;
    }

    public void setPipelineProjectId(Long pipelineProjectId) {
        this.pipelineProjectId = pipelineProjectId;
    }

    public String getPipelineRepositoryAddress() {
        return pipelineRepositoryAddress;
    }

    public Pipeline pipelineRepositoryAddress(String pipelineRepositoryAddress) {
        this.pipelineRepositoryAddress = pipelineRepositoryAddress;
        return this;
    }

    public void setPipelineRepositoryAddress(String pipelineRepositoryAddress) {
        this.pipelineRepositoryAddress = pipelineRepositoryAddress;
    }

    public String getPipelineRepositoryUsername() {
        return pipelineRepositoryUsername;
    }

    public Pipeline pipelineRepositoryUsername(String pipelineRepositoryUsername) {
        this.pipelineRepositoryUsername = pipelineRepositoryUsername;
        return this;
    }

    public void setPipelineRepositoryUsername(String pipelineRepositoryUsername) {
        this.pipelineRepositoryUsername = pipelineRepositoryUsername;
    }

    public String getPipelineRepositoryPassword() {
        return pipelineRepositoryPassword;
    }

    public Pipeline pipelineRepositoryPassword(String pipelineRepositoryPassword) {
        this.pipelineRepositoryPassword = pipelineRepositoryPassword;
        return this;
    }

    public void setPipelineRepositoryPassword(String pipelineRepositoryPassword) {
        this.pipelineRepositoryPassword = pipelineRepositoryPassword;
    }

    public String getPipelineEngineCompilerPath() {
        return pipelineEngineCompilerPath;
    }

    public Pipeline pipelineEngineCompilerPath(String pipelineEngineCompilerPath) {
        this.pipelineEngineCompilerPath = pipelineEngineCompilerPath;
        return this;
    }

    public void setPipelineEngineCompilerPath(String pipelineEngineCompilerPath) {
        this.pipelineEngineCompilerPath = pipelineEngineCompilerPath;
    }

    public String getPipelineEngineCompilerArguments() {
        return pipelineEngineCompilerArguments;
    }

    public Pipeline pipelineEngineCompilerArguments(String pipelineEngineCompilerArguments) {
        this.pipelineEngineCompilerArguments = pipelineEngineCompilerArguments;
        return this;
    }

    public void setPipelineEngineCompilerArguments(String pipelineEngineCompilerArguments) {
        this.pipelineEngineCompilerArguments = pipelineEngineCompilerArguments;
    }

    public String getPipelineFtpAddress() {
        return pipelineFtpAddress;
    }

    public Pipeline pipelineFtpAddress(String pipelineFtpAddress) {
        this.pipelineFtpAddress = pipelineFtpAddress;
        return this;
    }

    public void setPipelineFtpAddress(String pipelineFtpAddress) {
        this.pipelineFtpAddress = pipelineFtpAddress;
    }

    public String getPipelineFtpUsername() {
        return pipelineFtpUsername;
    }

    public Pipeline pipelineFtpUsername(String pipelineFtpUsername) {
        this.pipelineFtpUsername = pipelineFtpUsername;
        return this;
    }

    public void setPipelineFtpUsername(String pipelineFtpUsername) {
        this.pipelineFtpUsername = pipelineFtpUsername;
    }

    public String getPipelineFtpPassword() {
        return pipelineFtpPassword;
    }

    public Pipeline pipelineFtpPassword(String pipelineFtpPassword) {
        this.pipelineFtpPassword = pipelineFtpPassword;
        return this;
    }

    public void setPipelineFtpPassword(String pipelineFtpPassword) {
        this.pipelineFtpPassword = pipelineFtpPassword;
    }

    public String getPipelineNotificatorDetails() {
        return pipelineNotificatorDetails;
    }

    public Pipeline pipelineNotificatorDetails(String pipelineNotificatorDetails) {
        this.pipelineNotificatorDetails = pipelineNotificatorDetails;
        return this;
    }

    public void setPipelineNotificatorDetails(String pipelineNotificatorDetails) {
        this.pipelineNotificatorDetails = pipelineNotificatorDetails;
    }

    public PipelineRepositoryType getPipelineRepositoryType() {
        return pipelineRepositoryType;
    }

    public Pipeline pipelineRepositoryType(PipelineRepositoryType pipelineRepositoryType) {
        this.pipelineRepositoryType = pipelineRepositoryType;
        return this;
    }

    public void setPipelineRepositoryType(PipelineRepositoryType pipelineRepositoryType) {
        this.pipelineRepositoryType = pipelineRepositoryType;
    }

    public PipelineNotificatorType getPipelineNotificatorType() {
        return pipelineNotificatorType;
    }

    public Pipeline pipelineNotificatorType(PipelineNotificatorType pipelineNotificatorType) {
        this.pipelineNotificatorType = pipelineNotificatorType;
        return this;
    }

    public void setPipelineNotificatorType(PipelineNotificatorType pipelineNotificatorType) {
        this.pipelineNotificatorType = pipelineNotificatorType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pipeline pipeline = (Pipeline) o;
        if (pipeline.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pipeline.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pipeline{" +
            "id=" + getId() +
            ", pipelineName='" + getPipelineName() + "'" +
            ", pipelineDescription='" + getPipelineDescription() + "'" +
            ", pipelineProjectId=" + getPipelineProjectId() +
            ", pipelineRepositoryAddress='" + getPipelineRepositoryAddress() + "'" +
            ", pipelineRepositoryUsername='" + getPipelineRepositoryUsername() + "'" +
            ", pipelineRepositoryPassword='" + getPipelineRepositoryPassword() + "'" +
            ", pipelineEngineCompilerPath='" + getPipelineEngineCompilerPath() + "'" +
            ", pipelineEngineCompilerArguments='" + getPipelineEngineCompilerArguments() + "'" +
            ", pipelineFtpAddress='" + getPipelineFtpAddress() + "'" +
            ", pipelineFtpUsername='" + getPipelineFtpUsername() + "'" +
            ", pipelineFtpPassword='" + getPipelineFtpPassword() + "'" +
            ", pipelineNotificatorDetails='" + getPipelineNotificatorDetails() + "'" +
            ", pipelineRepositoryType='" + getPipelineRepositoryType() + "'" +
            ", pipelineNotificatorType='" + getPipelineNotificatorType() + "'" +
            "}";
    }
}
