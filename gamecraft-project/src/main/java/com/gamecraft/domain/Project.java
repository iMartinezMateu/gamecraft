package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "project_name", length = 100, nullable = false)
    private String projectName;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "project_website")
    private String projectWebsite;

    @Column(name = "project_logo")
    private String projectLogo;

    @Column(name = "project_archived")
    private Boolean projectArchived;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public Project projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public Project projectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
        return this;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectWebsite() {
        return projectWebsite;
    }

    public Project projectWebsite(String projectWebsite) {
        this.projectWebsite = projectWebsite;
        return this;
    }

    public void setProjectWebsite(String projectWebsite) {
        this.projectWebsite = projectWebsite;
    }

    public String getProjectLogo() {
        return projectLogo;
    }

    public Project projectLogo(String projectLogo) {
        this.projectLogo = projectLogo;
        return this;
    }

    public void setProjectLogo(String projectLogo) {
        this.projectLogo = projectLogo;
    }

    public Boolean isProjectArchived() {
        return projectArchived;
    }

    public Project projectArchived(Boolean projectArchived) {
        this.projectArchived = projectArchived;
        return this;
    }

    public void setProjectArchived(Boolean projectArchived) {
        this.projectArchived = projectArchived;
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
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", projectName='" + getProjectName() + "'" +
            ", projectDescription='" + getProjectDescription() + "'" +
            ", projectWebsite='" + getProjectWebsite() + "'" +
            ", projectLogo='" + getProjectLogo() + "'" +
            ", projectArchived='" + isProjectArchived() + "'" +
            "}";
    }
}
