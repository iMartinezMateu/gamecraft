package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SlackAccount.
 */
@Entity
@Table(name = "slack_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "slackaccount")
public class SlackAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "slack_account_name", length = 100, nullable = false)
    private String slackAccountName;

    @Column(name = "slack_account_description")
    private String slackAccountDescription;

    @Column(name = "slack_account_token")
    private String slackAccountToken;

    @Column(name = "slack_account_enabled")
    private Boolean slackAccountEnabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlackAccountName() {
        return slackAccountName;
    }

    public SlackAccount slackAccountName(String slackAccountName) {
        this.slackAccountName = slackAccountName;
        return this;
    }

    public void setSlackAccountName(String slackAccountName) {
        this.slackAccountName = slackAccountName;
    }

    public String getSlackAccountDescription() {
        return slackAccountDescription;
    }

    public SlackAccount slackAccountDescription(String slackAccountDescription) {
        this.slackAccountDescription = slackAccountDescription;
        return this;
    }

    public void setSlackAccountDescription(String slackAccountDescription) {
        this.slackAccountDescription = slackAccountDescription;
    }

    public String getSlackAccountToken() {
        return slackAccountToken;
    }

    public SlackAccount slackAccountToken(String slackAccountToken) {
        this.slackAccountToken = slackAccountToken;
        return this;
    }

    public void setSlackAccountToken(String slackAccountToken) {
        this.slackAccountToken = slackAccountToken;
    }

    public Boolean isSlackAccountEnabled() {
        return slackAccountEnabled;
    }

    public SlackAccount slackAccountEnabled(Boolean slackAccountEnabled) {
        this.slackAccountEnabled = slackAccountEnabled;
        return this;
    }

    public void setSlackAccountEnabled(Boolean slackAccountEnabled) {
        this.slackAccountEnabled = slackAccountEnabled;
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
        SlackAccount slackAccount = (SlackAccount) o;
        if (slackAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), slackAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SlackAccount{" +
            "id=" + getId() +
            ", slackAccountName='" + getSlackAccountName() + "'" +
            ", slackAccountDescription='" + getSlackAccountDescription() + "'" +
            ", slackAccountToken='" + getSlackAccountToken() + "'" +
            ", slackAccountEnabled='" + isSlackAccountEnabled() + "'" +
            "}";
    }
}
