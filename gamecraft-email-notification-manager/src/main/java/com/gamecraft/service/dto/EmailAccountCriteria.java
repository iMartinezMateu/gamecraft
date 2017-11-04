package com.gamecraft.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the EmailAccount entity. This class is used in EmailAccountResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /email-accounts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmailAccountCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter emailAccountName;

    private StringFilter emailSmtpServer;

    private StringFilter emailSmtpUsername;

    private StringFilter emailSmtpPassword;

    private BooleanFilter emailSmtpUseSSL;

    private IntegerFilter emailSmtpPort;

    private StringFilter emailAccountDescription;

    private BooleanFilter emailAccountEnabled;

    public EmailAccountCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmailAccountName() {
        return emailAccountName;
    }

    public void setEmailAccountName(StringFilter emailAccountName) {
        this.emailAccountName = emailAccountName;
    }

    public StringFilter getEmailSmtpServer() {
        return emailSmtpServer;
    }

    public void setEmailSmtpServer(StringFilter emailSmtpServer) {
        this.emailSmtpServer = emailSmtpServer;
    }

    public StringFilter getEmailSmtpUsername() {
        return emailSmtpUsername;
    }

    public void setEmailSmtpUsername(StringFilter emailSmtpUsername) {
        this.emailSmtpUsername = emailSmtpUsername;
    }

    public StringFilter getEmailSmtpPassword() {
        return emailSmtpPassword;
    }

    public void setEmailSmtpPassword(StringFilter emailSmtpPassword) {
        this.emailSmtpPassword = emailSmtpPassword;
    }

    public BooleanFilter getEmailSmtpUseSSL() {
        return emailSmtpUseSSL;
    }

    public void setEmailSmtpUseSSL(BooleanFilter emailSmtpUseSSL) {
        this.emailSmtpUseSSL = emailSmtpUseSSL;
    }

    public IntegerFilter getEmailSmtpPort() {
        return emailSmtpPort;
    }

    public void setEmailSmtpPort(IntegerFilter emailSmtpPort) {
        this.emailSmtpPort = emailSmtpPort;
    }

    public StringFilter getEmailAccountDescription() {
        return emailAccountDescription;
    }

    public void setEmailAccountDescription(StringFilter emailAccountDescription) {
        this.emailAccountDescription = emailAccountDescription;
    }

    public BooleanFilter getEmailAccountEnabled() {
        return emailAccountEnabled;
    }

    public void setEmailAccountEnabled(BooleanFilter emailAccountEnabled) {
        this.emailAccountEnabled = emailAccountEnabled;
    }

    @Override
    public String toString() {
        return "EmailAccountCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (emailAccountName != null ? "emailAccountName=" + emailAccountName + ", " : "") +
                (emailSmtpServer != null ? "emailSmtpServer=" + emailSmtpServer + ", " : "") +
                (emailSmtpUsername != null ? "emailSmtpUsername=" + emailSmtpUsername + ", " : "") +
                (emailSmtpPassword != null ? "emailSmtpPassword=" + emailSmtpPassword + ", " : "") +
                (emailSmtpUseSSL != null ? "emailSmtpUseSSL=" + emailSmtpUseSSL + ", " : "") +
                (emailSmtpPort != null ? "emailSmtpPort=" + emailSmtpPort + ", " : "") +
                (emailAccountDescription != null ? "emailAccountDescription=" + emailAccountDescription + ", " : "") +
                (emailAccountEnabled != null ? "emailAccountEnabled=" + emailAccountEnabled + ", " : "") +
            "}";
    }

}
