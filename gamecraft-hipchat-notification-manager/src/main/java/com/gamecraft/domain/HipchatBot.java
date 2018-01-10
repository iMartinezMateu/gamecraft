package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HipchatBot.
 */
@Entity
@Table(name = "hipchat_bot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hipchatbot")
public class HipchatBot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "hipchat_bot_name", length = 100, nullable = false)
    private String hipchatBotName;

    @Column(name = "hipchat_bot_description")
    private String hipchatBotDescription;

    @NotNull
    @Column(name = "hipchat_bot_token", nullable = false)
    private String hipchatBotToken;

    @Column(name = "hipchat_bot_enabled")
    private Boolean hipchatBotEnabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHipchatBotName() {
        return hipchatBotName;
    }

    public HipchatBot hipchatBotName(String hipchatBotName) {
        this.hipchatBotName = hipchatBotName;
        return this;
    }

    public void setHipchatBotName(String hipchatBotName) {
        this.hipchatBotName = hipchatBotName;
    }

    public String getHipchatBotDescription() {
        return hipchatBotDescription;
    }

    public HipchatBot hipchatBotDescription(String hipchatBotDescription) {
        this.hipchatBotDescription = hipchatBotDescription;
        return this;
    }

    public void setHipchatBotDescription(String hipchatBotDescription) {
        this.hipchatBotDescription = hipchatBotDescription;
    }

    public String getHipchatBotToken() {
        return hipchatBotToken;
    }

    public HipchatBot hipchatBotToken(String hipchatBotToken) {
        this.hipchatBotToken = hipchatBotToken;
        return this;
    }

    public void setHipchatBotToken(String hipchatBotToken) {
        this.hipchatBotToken = hipchatBotToken;
    }

    public Boolean isHipchatBotEnabled() {
        return hipchatBotEnabled;
    }

    public HipchatBot hipchatBotEnabled(Boolean hipchatBotEnabled) {
        this.hipchatBotEnabled = hipchatBotEnabled;
        return this;
    }

    public void setHipchatBotEnabled(Boolean hipchatBotEnabled) {
        this.hipchatBotEnabled = hipchatBotEnabled;
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
        HipchatBot hipchatBot = (HipchatBot) o;
        if (hipchatBot.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hipchatBot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HipchatBot{" +
            "id=" + getId() +
            ", hipchatBotName='" + getHipchatBotName() + "'" +
            ", hipchatBotDescription='" + getHipchatBotDescription() + "'" +
            ", hipchatBotToken='" + getHipchatBotToken() + "'" +
            ", hipchatBotEnabled='" + isHipchatBotEnabled() + "'" +
            "}";
    }
}
