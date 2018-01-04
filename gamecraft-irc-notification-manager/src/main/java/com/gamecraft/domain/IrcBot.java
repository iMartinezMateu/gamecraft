package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A IrcBot.
 */
@Entity
@Table(name = "ircbot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ircbot")
public class IrcBot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "irc_bot_name", length = 50, nullable = false)
    private String ircBotName;

    @Column(name = "irc_bot_description")
    private String ircBotDescription;

    @Column(name = "irc_bot_enabled")
    private Boolean ircBotEnabled;

    @NotNull
    @Column(name = "irc_server_address", nullable = false)
    private String ircServerAddress;

    @NotNull
    @Min(value = 0)
    @Column(name = "irc_server_port", nullable = false)
    private Integer ircServerPort;

    @NotNull
    @Column(name = "irc_bot_nickname", nullable = false)
    private String ircBotNickname;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIrcBotName() {
        return ircBotName;
    }

    public IrcBot ircBotName(String ircBotName) {
        this.ircBotName = ircBotName;
        return this;
    }

    public void setIrcBotName(String ircBotName) {
        this.ircBotName = ircBotName;
    }

    public String getIrcBotDescription() {
        return ircBotDescription;
    }

    public IrcBot ircBotDescription(String ircBotDescription) {
        this.ircBotDescription = ircBotDescription;
        return this;
    }

    public void setIrcBotDescription(String ircBotDescription) {
        this.ircBotDescription = ircBotDescription;
    }

    public Boolean isIrcBotEnabled() {
        return ircBotEnabled;
    }

    public IrcBot ircBotEnabled(Boolean ircBotEnabled) {
        this.ircBotEnabled = ircBotEnabled;
        return this;
    }

    public void setIrcBotEnabled(Boolean ircBotEnabled) {
        this.ircBotEnabled = ircBotEnabled;
    }

    public String getIrcServerAddress() {
        return ircServerAddress;
    }

    public IrcBot ircServerAddress(String ircServerAddress) {
        this.ircServerAddress = ircServerAddress;
        return this;
    }

    public void setIrcServerAddress(String ircServerAddress) {
        this.ircServerAddress = ircServerAddress;
    }

    public Integer getIrcServerPort() {
        return ircServerPort;
    }

    public IrcBot ircServerPort(Integer ircServerPort) {
        this.ircServerPort = ircServerPort;
        return this;
    }

    public void setIrcServerPort(Integer ircServerPort) {
        this.ircServerPort = ircServerPort;
    }

    public String getIrcBotNickname() {
        return ircBotNickname;
    }

    public IrcBot ircBotNickname(String ircBotNickname) {
        this.ircBotNickname = ircBotNickname;
        return this;
    }

    public void setIrcBotNickname(String ircBotNickname) {
        this.ircBotNickname = ircBotNickname;
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
        IrcBot ircBot = (IrcBot) o;
        if (ircBot.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ircBot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IrcBot{" +
            "id=" + getId() +
            ", ircBotName='" + getIrcBotName() + "'" +
            ", ircBotDescription='" + getIrcBotDescription() + "'" +
            ", ircBotEnabled='" + isIrcBotEnabled() + "'" +
            ", ircServerAddress='" + getIrcServerAddress() + "'" +
            ", ircServerPort='" + getIrcServerPort() + "'" +
            ", ircBotNickname='" + getIrcBotNickname() + "'" +
            "}";
    }
}
