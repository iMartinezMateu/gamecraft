package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SonarInstance.
 */
@Entity
@Table(name = "sonar_instance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sonarinstance")
public class SonarInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "sonar_instance_name", length = 100, nullable = false)
    private String sonarInstanceName;

    @Column(name = "sonar_instance_description")
    private String sonarInstanceDescription;

    @Column(name = "sonar_instance_runner_path")
    private String sonarInstanceRunnerPath;

    @NotNull
    @Column(name = "sonar_instance_enabled", nullable = false)
    private Boolean sonarInstanceEnabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSonarInstanceName() {
        return sonarInstanceName;
    }

    public SonarInstance sonarInstanceName(String sonarInstanceName) {
        this.sonarInstanceName = sonarInstanceName;
        return this;
    }

    public void setSonarInstanceName(String sonarInstanceName) {
        this.sonarInstanceName = sonarInstanceName;
    }

    public String getSonarInstanceDescription() {
        return sonarInstanceDescription;
    }

    public SonarInstance sonarInstanceDescription(String sonarInstanceDescription) {
        this.sonarInstanceDescription = sonarInstanceDescription;
        return this;
    }

    public void setSonarInstanceDescription(String sonarInstanceDescription) {
        this.sonarInstanceDescription = sonarInstanceDescription;
    }

    public String getSonarInstanceRunnerPath() {
        return sonarInstanceRunnerPath;
    }

    public SonarInstance sonarInstanceRunnerPath(String sonarInstanceRunnerPath) {
        this.sonarInstanceRunnerPath = sonarInstanceRunnerPath;
        return this;
    }

    public void setSonarInstanceRunnerPath(String sonarInstanceRunnerPath) {
        this.sonarInstanceRunnerPath = sonarInstanceRunnerPath;
    }

    public Boolean isSonarInstanceEnabled() {
        return sonarInstanceEnabled;
    }

    public SonarInstance sonarInstanceEnabled(Boolean sonarInstanceEnabled) {
        this.sonarInstanceEnabled = sonarInstanceEnabled;
        return this;
    }

    public void setSonarInstanceEnabled(Boolean sonarInstanceEnabled) {
        this.sonarInstanceEnabled = sonarInstanceEnabled;
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
        SonarInstance sonarInstance = (SonarInstance) o;
        if (sonarInstance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sonarInstance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SonarInstance{" +
            "id=" + getId() +
            ", sonarInstanceName='" + getSonarInstanceName() + "'" +
            ", sonarInstanceDescription='" + getSonarInstanceDescription() + "'" +
            ", sonarInstanceRunnerPath='" + getSonarInstanceRunnerPath() + "'" +
            ", sonarInstanceEnabled='" + isSonarInstanceEnabled() + "'" +
            "}";
    }
}
