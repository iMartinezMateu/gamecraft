package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Engine.
 */
@Entity
@Table(name = "engine")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "engine")
public class Engine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "engine_name", length = 100, nullable = false)
    private String engineName;

    @Column(name = "engine_description")
    private String engineDescription;

    @NotNull
    @Column(name = "engine_compiler_path", nullable = false)
    private String engineCompilerPath;

    @Column(name = "engine_compiler_arguments")
    private String engineCompilerArguments;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEngineName() {
        return engineName;
    }

    public Engine engineName(String engineName) {
        this.engineName = engineName;
        return this;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public String getEngineDescription() {
        return engineDescription;
    }

    public Engine engineDescription(String engineDescription) {
        this.engineDescription = engineDescription;
        return this;
    }

    public void setEngineDescription(String engineDescription) {
        this.engineDescription = engineDescription;
    }

    public String getEngineCompilerPath() {
        return engineCompilerPath;
    }

    public Engine engineCompilerPath(String engineCompilerPath) {
        this.engineCompilerPath = engineCompilerPath;
        return this;
    }

    public void setEngineCompilerPath(String engineCompilerPath) {
        this.engineCompilerPath = engineCompilerPath;
    }

    public String getEngineCompilerArguments() {
        return engineCompilerArguments;
    }

    public Engine engineCompilerArguments(String engineCompilerArguments) {
        this.engineCompilerArguments = engineCompilerArguments;
        return this;
    }

    public void setEngineCompilerArguments(String engineCompilerArguments) {
        this.engineCompilerArguments = engineCompilerArguments;
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
        Engine engine = (Engine) o;
        if (engine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), engine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Engine{" +
            "id=" + getId() +
            ", engineName='" + getEngineName() + "'" +
            ", engineDescription='" + getEngineDescription() + "'" +
            ", engineCompilerPath='" + getEngineCompilerPath() + "'" +
            ", engineCompilerArguments='" + getEngineCompilerArguments() + "'" +
            "}";
    }
}
