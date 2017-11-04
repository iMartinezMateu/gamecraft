package com.gamecraft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EmailAccount.
 */
@Entity
@Table(name = "email_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "emailaccount")
public class EmailAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "email_account_name", length = 100, nullable = false)
    private String emailAccountName;

    @NotNull
    @Column(name = "email_smtp_server", nullable = false)
    private String emailSmtpServer;

    @Column(name = "email_smtp_username")
    private String emailSmtpUsername;

    @Column(name = "email_smtp_password")
    private String emailSmtpPassword;

    @Column(name = "email_smtp_use_ssl")
    private Boolean emailSmtpUseSSL;

    @Column(name = "email_smtp_port")
    private Integer emailSmtpPort;

    @Column(name = "email_account_description")
    private String emailAccountDescription;

    @Column(name = "email_account_enabled")
    private Boolean emailAccountEnabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAccountName() {
        return emailAccountName;
    }

    public EmailAccount emailAccountName(String emailAccountName) {
        this.emailAccountName = emailAccountName;
        return this;
    }

    public void setEmailAccountName(String emailAccountName) {
        this.emailAccountName = emailAccountName;
    }

    public String getEmailSmtpServer() {
        return emailSmtpServer;
    }

    public EmailAccount emailSmtpServer(String emailSmtpServer) {
        this.emailSmtpServer = emailSmtpServer;
        return this;
    }

    public void setEmailSmtpServer(String emailSmtpServer) {
        this.emailSmtpServer = emailSmtpServer;
    }

    public String getEmailSmtpUsername() {
        return emailSmtpUsername;
    }

    public EmailAccount emailSmtpUsername(String emailSmtpUsername) {
        this.emailSmtpUsername = emailSmtpUsername;
        return this;
    }

    public void setEmailSmtpUsername(String emailSmtpUsername) {
        this.emailSmtpUsername = emailSmtpUsername;
    }

    public String getEmailSmtpPassword() {
        return emailSmtpPassword;
    }

    public EmailAccount emailSmtpPassword(String emailSmtpPassword) {
        this.emailSmtpPassword = emailSmtpPassword;
        return this;
    }

    public void setEmailSmtpPassword(String emailSmtpPassword) {
        this.emailSmtpPassword = emailSmtpPassword;
    }

    public Boolean isEmailSmtpUseSSL() {
        return emailSmtpUseSSL;
    }

    public EmailAccount emailSmtpUseSSL(Boolean emailSmtpUseSSL) {
        this.emailSmtpUseSSL = emailSmtpUseSSL;
        return this;
    }

    public void setEmailSmtpUseSSL(Boolean emailSmtpUseSSL) {
        this.emailSmtpUseSSL = emailSmtpUseSSL;
    }

    public Integer getEmailSmtpPort() {
        return emailSmtpPort;
    }

    public EmailAccount emailSmtpPort(Integer emailSmtpPort) {
        this.emailSmtpPort = emailSmtpPort;
        return this;
    }

    public void setEmailSmtpPort(Integer emailSmtpPort) {
        this.emailSmtpPort = emailSmtpPort;
    }

    public String getEmailAccountDescription() {
        return emailAccountDescription;
    }

    public EmailAccount emailAccountDescription(String emailAccountDescription) {
        this.emailAccountDescription = emailAccountDescription;
        return this;
    }

    public void setEmailAccountDescription(String emailAccountDescription) {
        this.emailAccountDescription = emailAccountDescription;
    }

    public Boolean isEmailAccountEnabled() {
        return emailAccountEnabled;
    }

    public EmailAccount emailAccountEnabled(Boolean emailAccountEnabled) {
        this.emailAccountEnabled = emailAccountEnabled;
        return this;
    }

    public void setEmailAccountEnabled(Boolean emailAccountEnabled) {
        this.emailAccountEnabled = emailAccountEnabled;
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
        EmailAccount emailAccount = (EmailAccount) o;
        if (emailAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailAccount{" +
            "id=" + getId() +
            ", emailAccountName='" + getEmailAccountName() + "'" +
            ", emailSmtpServer='" + getEmailSmtpServer() + "'" +
            ", emailSmtpUsername='" + getEmailSmtpUsername() + "'" +
            ", emailSmtpPassword='" + getEmailSmtpPassword() + "'" +
            ", emailSmtpUseSSL='" + isEmailSmtpUseSSL() + "'" +
            ", emailSmtpPort='" + getEmailSmtpPort() + "'" +
            ", emailAccountDescription='" + getEmailAccountDescription() + "'" +
            ", emailAccountEnabled='" + isEmailAccountEnabled() + "'" +
            "}";
    }
}
