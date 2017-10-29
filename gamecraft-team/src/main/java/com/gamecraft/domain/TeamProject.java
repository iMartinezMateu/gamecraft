package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TeamProject.
 */
@Entity
@Table(name = "team_project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "teamproject")
public class TeamProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @NotNull
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public TeamProject teamId(Long teamId) {
        this.teamId = teamId;
        return this;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public TeamProject projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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
        TeamProject teamProject = (TeamProject) o;
        if (teamProject.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamProject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TeamProject{" +
            "id=" + getId() +
            ", teamId='" + getTeamId() + "'" +
            ", projectId='" + getProjectId() + "'" +
            "}";
    }
}
