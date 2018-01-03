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
 * Criteria class for the TelegramBot entity. This class is used in TelegramBotResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /telegram-bots?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TelegramBotCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter telegramBotName;

    private StringFilter telegramBotDescription;

    private StringFilter telegramBotToken;

    private BooleanFilter telegramBotEnabled;

    public TelegramBotCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTelegramBotName() {
        return telegramBotName;
    }

    public void setTelegramBotName(StringFilter telegramBotName) {
        this.telegramBotName = telegramBotName;
    }

    public StringFilter getTelegramBotDescription() {
        return telegramBotDescription;
    }

    public void setTelegramBotDescription(StringFilter telegramBotDescription) {
        this.telegramBotDescription = telegramBotDescription;
    }

    public StringFilter getTelegramBotToken() {
        return telegramBotToken;
    }

    public void setTelegramBotToken(StringFilter telegramBotToken) {
        this.telegramBotToken = telegramBotToken;
    }

    public BooleanFilter getTelegramBotEnabled() {
        return telegramBotEnabled;
    }

    public void setTelegramBotEnabled(BooleanFilter telegramBotEnabled) {
        this.telegramBotEnabled = telegramBotEnabled;
    }

    @Override
    public String toString() {
        return "TelegramBotCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (telegramBotName != null ? "telegramBotName=" + telegramBotName + ", " : "") +
                (telegramBotDescription != null ? "telegramBotDescription=" + telegramBotDescription + ", " : "") +
                (telegramBotToken != null ? "telegramBotToken=" + telegramBotToken + ", " : "") +
                (telegramBotEnabled != null ? "telegramBotEnabled=" + telegramBotEnabled + ", " : "") +
            "}";
    }

}
