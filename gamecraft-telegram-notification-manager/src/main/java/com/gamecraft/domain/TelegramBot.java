package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TelegramBot.
 */
@Entity
@Table(name = "telegram_bot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "telegrambot")
public class TelegramBot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "telegram_bot_name", length = 100, nullable = false)
    private String telegramBotName;

    @Column(name = "telegram_bot_description")
    private String telegramBotDescription;

    @NotNull
    @Column(name = "telegram_bot_token", nullable = false)
    private String telegramBotToken;

    @Column(name = "telegram_bot_enabled")
    private Boolean telegramBotEnabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelegramBotName() {
        return telegramBotName;
    }

    public TelegramBot telegramBotName(String telegramBotName) {
        this.telegramBotName = telegramBotName;
        return this;
    }

    public void setTelegramBotName(String telegramBotName) {
        this.telegramBotName = telegramBotName;
    }

    public String getTelegramBotDescription() {
        return telegramBotDescription;
    }

    public TelegramBot telegramBotDescription(String telegramBotDescription) {
        this.telegramBotDescription = telegramBotDescription;
        return this;
    }

    public void setTelegramBotDescription(String telegramBotDescription) {
        this.telegramBotDescription = telegramBotDescription;
    }

    public String getTelegramBotToken() {
        return telegramBotToken;
    }

    public TelegramBot telegramBotToken(String telegramBotToken) {
        this.telegramBotToken = telegramBotToken;
        return this;
    }

    public void setTelegramBotToken(String telegramBotToken) {
        this.telegramBotToken = telegramBotToken;
    }

    public Boolean isTelegramBotEnabled() {
        return telegramBotEnabled;
    }

    public TelegramBot telegramBotEnabled(Boolean telegramBotEnabled) {
        this.telegramBotEnabled = telegramBotEnabled;
        return this;
    }

    public void setTelegramBotEnabled(Boolean telegramBotEnabled) {
        this.telegramBotEnabled = telegramBotEnabled;
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
        TelegramBot telegramBot = (TelegramBot) o;
        if (telegramBot.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), telegramBot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TelegramBot{" +
            "id=" + getId() +
            ", telegramBotName='" + getTelegramBotName() + "'" +
            ", telegramBotDescription='" + getTelegramBotDescription() + "'" +
            ", telegramBotToken='" + getTelegramBotToken() + "'" +
            ", telegramBotEnabled='" + isTelegramBotEnabled() + "'" +
            "}";
    }
}
