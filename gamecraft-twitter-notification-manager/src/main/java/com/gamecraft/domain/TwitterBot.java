package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TwitterBot.
 */
@Entity
@Table(name = "twitter_bot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "twitterbot")
public class TwitterBot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "twitter_bot_name", length = 100, nullable = false)
    private String twitterBotName;

    @Column(name = "twitter_bot_description")
    private String twitterBotDescription;

    @NotNull
    @Column(name = "twitter_bot_consumer_key", nullable = false)
    private String twitterBotConsumerKey;

    @NotNull
    @Column(name = "twitter_bot_consumer_secret", nullable = false)
    private String twitterBotConsumerSecret;

    @NotNull
    @Column(name = "twitter_bot_access_token", nullable = false)
    private String twitterBotAccessToken;

    @NotNull
    @Column(name = "twitter_bot_access_token_secret", nullable = false)
    private String twitterBotAccessTokenSecret;

    @Column(name = "twitter_bot_enabled")
    private Boolean twitterBotEnabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTwitterBotName() {
        return twitterBotName;
    }

    public TwitterBot twitterBotName(String twitterBotName) {
        this.twitterBotName = twitterBotName;
        return this;
    }

    public void setTwitterBotName(String twitterBotName) {
        this.twitterBotName = twitterBotName;
    }

    public String getTwitterBotDescription() {
        return twitterBotDescription;
    }

    public TwitterBot twitterBotDescription(String twitterBotDescription) {
        this.twitterBotDescription = twitterBotDescription;
        return this;
    }

    public void setTwitterBotDescription(String twitterBotDescription) {
        this.twitterBotDescription = twitterBotDescription;
    }

    public String getTwitterBotConsumerKey() {
        return twitterBotConsumerKey;
    }

    public TwitterBot twitterBotConsumerKey(String twitterBotConsumerKey) {
        this.twitterBotConsumerKey = twitterBotConsumerKey;
        return this;
    }

    public void setTwitterBotConsumerKey(String twitterBotConsumerKey) {
        this.twitterBotConsumerKey = twitterBotConsumerKey;
    }

    public String getTwitterBotConsumerSecret() {
        return twitterBotConsumerSecret;
    }

    public TwitterBot twitterBotConsumerSecret(String twitterBotConsumerSecret) {
        this.twitterBotConsumerSecret = twitterBotConsumerSecret;
        return this;
    }

    public void setTwitterBotConsumerSecret(String twitterBotConsumerSecret) {
        this.twitterBotConsumerSecret = twitterBotConsumerSecret;
    }

    public String getTwitterBotAccessToken() {
        return twitterBotAccessToken;
    }

    public TwitterBot twitterBotAccessToken(String twitterBotAccessToken) {
        this.twitterBotAccessToken = twitterBotAccessToken;
        return this;
    }

    public void setTwitterBotAccessToken(String twitterBotAccessToken) {
        this.twitterBotAccessToken = twitterBotAccessToken;
    }

    public String getTwitterBotAccessTokenSecret() {
        return twitterBotAccessTokenSecret;
    }

    public TwitterBot twitterBotAccessTokenSecret(String twitterBotAccessTokenSecret) {
        this.twitterBotAccessTokenSecret = twitterBotAccessTokenSecret;
        return this;
    }

    public void setTwitterBotAccessTokenSecret(String twitterBotAccessTokenSecret) {
        this.twitterBotAccessTokenSecret = twitterBotAccessTokenSecret;
    }

    public Boolean isTwitterBotEnabled() {
        return twitterBotEnabled;
    }

    public TwitterBot twitterBotEnabled(Boolean twitterBotEnabled) {
        this.twitterBotEnabled = twitterBotEnabled;
        return this;
    }

    public void setTwitterBotEnabled(Boolean twitterBotEnabled) {
        this.twitterBotEnabled = twitterBotEnabled;
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
        TwitterBot twitterBot = (TwitterBot) o;
        if (twitterBot.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), twitterBot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TwitterBot{" +
            "id=" + getId() +
            ", twitterBotName='" + getTwitterBotName() + "'" +
            ", twitterBotDescription='" + getTwitterBotDescription() + "'" +
            ", twitterBotConsumerKey='" + getTwitterBotConsumerKey() + "'" +
            ", twitterBotConsumerSecret='" + getTwitterBotConsumerSecret() + "'" +
            ", twitterBotAccessToken='" + getTwitterBotAccessToken() + "'" +
            ", twitterBotAccessTokenSecret='" + getTwitterBotAccessTokenSecret() + "'" +
            ", twitterBotEnabled='" + isTwitterBotEnabled() + "'" +
            "}";
    }
}
