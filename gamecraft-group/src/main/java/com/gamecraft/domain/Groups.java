package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.gamecraft.domain.enumeration.Role;

/**
 * A Groups.
 */
@Entity
@Table(name = "groups")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "groups")
public class Groups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "group_name", length = 100, nullable = false)
    private String groupName;

    @Column(name = "group_description")
    private String groupDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "group_role", nullable = false)
    private Role groupRole;

    @NotNull
    @Column(name = "group_enabled", nullable = false)
    private Boolean groupEnabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Groups groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public Groups groupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
        return this;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Role getGroupRole() {
        return groupRole;
    }

    public Groups groupRole(Role groupRole) {
        this.groupRole = groupRole;
        return this;
    }

    public void setGroupRole(Role groupRole) {
        this.groupRole = groupRole;
    }

    public Boolean isGroupEnabled() {
        return groupEnabled;
    }

    public Groups groupEnabled(Boolean groupEnabled) {
        this.groupEnabled = groupEnabled;
        return this;
    }

    public void setGroupEnabled(Boolean groupEnabled) {
        this.groupEnabled = groupEnabled;
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
        Groups groups = (Groups) o;
        if (groups.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groups.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Groups{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", groupDescription='" + getGroupDescription() + "'" +
            ", groupRole='" + getGroupRole() + "'" +
            ", groupEnabled='" + isGroupEnabled() + "'" +
            "}";
    }
}
