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
 * Criteria class for the SlackAccount entity. This class is used in SlackAccountResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /slack-accounts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SlackAccountCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter slackAccountName;

    private StringFilter slackAccountDescription;

    private StringFilter slackAccountToken;

    private BooleanFilter slackAccountEnabled;

    public SlackAccountCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSlackAccountName() {
        return slackAccountName;
    }

    public void setSlackAccountName(StringFilter slackAccountName) {
        this.slackAccountName = slackAccountName;
    }

    public StringFilter getSlackAccountDescription() {
        return slackAccountDescription;
    }

    public void setSlackAccountDescription(StringFilter slackAccountDescription) {
        this.slackAccountDescription = slackAccountDescription;
    }

    public StringFilter getSlackAccountToken() {
        return slackAccountToken;
    }

    public void setSlackAccountToken(StringFilter slackAccountToken) {
        this.slackAccountToken = slackAccountToken;
    }

    public BooleanFilter getSlackAccountEnabled() {
        return slackAccountEnabled;
    }

    public void setSlackAccountEnabled(BooleanFilter slackAccountEnabled) {
        this.slackAccountEnabled = slackAccountEnabled;
    }

    @Override
    public String toString() {
        return "SlackAccountCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (slackAccountName != null ? "slackAccountName=" + slackAccountName + ", " : "") +
                (slackAccountDescription != null ? "slackAccountDescription=" + slackAccountDescription + ", " : "") +
                (slackAccountToken != null ? "slackAccountToken=" + slackAccountToken + ", " : "") +
                (slackAccountEnabled != null ? "slackAccountEnabled=" + slackAccountEnabled + ", " : "") +
            "}";
    }

}
