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
 * Criteria class for the TwitterBot entity. This class is used in TwitterBotResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /twitter-bots?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TwitterBotCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter twitterBotName;

    private StringFilter twitterBotDescription;

    private StringFilter twitterBotConsumerKey;

    private StringFilter twitterBotConsumerSecret;

    private StringFilter twitterBotAccessToken;

    private StringFilter twitterBotAccessTokenSecret;

    private BooleanFilter twitterBotEnabled;

    public TwitterBotCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTwitterBotName() {
        return twitterBotName;
    }

    public void setTwitterBotName(StringFilter twitterBotName) {
        this.twitterBotName = twitterBotName;
    }

    public StringFilter getTwitterBotDescription() {
        return twitterBotDescription;
    }

    public void setTwitterBotDescription(StringFilter twitterBotDescription) {
        this.twitterBotDescription = twitterBotDescription;
    }

    public StringFilter getTwitterBotConsumerKey() {
        return twitterBotConsumerKey;
    }

    public void setTwitterBotConsumerKey(StringFilter twitterBotConsumerKey) {
        this.twitterBotConsumerKey = twitterBotConsumerKey;
    }

    public StringFilter getTwitterBotConsumerSecret() {
        return twitterBotConsumerSecret;
    }

    public void setTwitterBotConsumerSecret(StringFilter twitterBotConsumerSecret) {
        this.twitterBotConsumerSecret = twitterBotConsumerSecret;
    }

    public StringFilter getTwitterBotAccessToken() {
        return twitterBotAccessToken;
    }

    public void setTwitterBotAccessToken(StringFilter twitterBotAccessToken) {
        this.twitterBotAccessToken = twitterBotAccessToken;
    }

    public StringFilter getTwitterBotAccessTokenSecret() {
        return twitterBotAccessTokenSecret;
    }

    public void setTwitterBotAccessTokenSecret(StringFilter twitterBotAccessTokenSecret) {
        this.twitterBotAccessTokenSecret = twitterBotAccessTokenSecret;
    }

    public BooleanFilter getTwitterBotEnabled() {
        return twitterBotEnabled;
    }

    public void setTwitterBotEnabled(BooleanFilter twitterBotEnabled) {
        this.twitterBotEnabled = twitterBotEnabled;
    }

    @Override
    public String toString() {
        return "TwitterBotCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (twitterBotName != null ? "twitterBotName=" + twitterBotName + ", " : "") +
                (twitterBotDescription != null ? "twitterBotDescription=" + twitterBotDescription + ", " : "") +
                (twitterBotConsumerKey != null ? "twitterBotConsumerKey=" + twitterBotConsumerKey + ", " : "") +
                (twitterBotConsumerSecret != null ? "twitterBotConsumerSecret=" + twitterBotConsumerSecret + ", " : "") +
                (twitterBotAccessToken != null ? "twitterBotAccessToken=" + twitterBotAccessToken + ", " : "") +
                (twitterBotAccessTokenSecret != null ? "twitterBotAccessTokenSecret=" + twitterBotAccessTokenSecret + ", " : "") +
                (twitterBotEnabled != null ? "twitterBotEnabled=" + twitterBotEnabled + ", " : "") +
            "}";
    }

}
