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
 * Criteria class for the HipchatBot entity. This class is used in HipchatBotResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /hipchat-bots?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HipchatBotCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter hipchatBotName;

    private StringFilter hipchatBotDescription;

    private StringFilter hipchatBotToken;

    private BooleanFilter hipchatBotEnabled;

    public HipchatBotCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getHipchatBotName() {
        return hipchatBotName;
    }

    public void setHipchatBotName(StringFilter hipchatBotName) {
        this.hipchatBotName = hipchatBotName;
    }

    public StringFilter getHipchatBotDescription() {
        return hipchatBotDescription;
    }

    public void setHipchatBotDescription(StringFilter hipchatBotDescription) {
        this.hipchatBotDescription = hipchatBotDescription;
    }

    public StringFilter getHipchatBotToken() {
        return hipchatBotToken;
    }

    public void setHipchatBotToken(StringFilter hipchatBotToken) {
        this.hipchatBotToken = hipchatBotToken;
    }

    public BooleanFilter getHipchatBotEnabled() {
        return hipchatBotEnabled;
    }

    public void setHipchatBotEnabled(BooleanFilter hipchatBotEnabled) {
        this.hipchatBotEnabled = hipchatBotEnabled;
    }

    @Override
    public String toString() {
        return "HipchatBotCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (hipchatBotName != null ? "hipchatBotName=" + hipchatBotName + ", " : "") +
                (hipchatBotDescription != null ? "hipchatBotDescription=" + hipchatBotDescription + ", " : "") +
                (hipchatBotToken != null ? "hipchatBotToken=" + hipchatBotToken + ", " : "") +
                (hipchatBotEnabled != null ? "hipchatBotEnabled=" + hipchatBotEnabled + ", " : "") +
            "}";
    }

}
