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
 * Criteria class for the IrcBot entity. This class is used in IrcBotResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /irc-bots?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IrcBotCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter ircBotName;

    private StringFilter ircBotDescription;

    private BooleanFilter ircBotEnabled;

    private StringFilter ircServerAddress;

    private IntegerFilter ircServerPort;

    private StringFilter ircBotNickname;

    public IrcBotCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIrcBotName() {
        return ircBotName;
    }

    public void setIrcBotName(StringFilter ircBotName) {
        this.ircBotName = ircBotName;
    }

    public StringFilter getIrcBotDescription() {
        return ircBotDescription;
    }

    public void setIrcBotDescription(StringFilter ircBotDescription) {
        this.ircBotDescription = ircBotDescription;
    }

    public BooleanFilter getIrcBotEnabled() {
        return ircBotEnabled;
    }

    public void setIrcBotEnabled(BooleanFilter ircBotEnabled) {
        this.ircBotEnabled = ircBotEnabled;
    }

    public StringFilter getIrcServerAddress() {
        return ircServerAddress;
    }

    public void setIrcServerAddress(StringFilter ircServerAddress) {
        this.ircServerAddress = ircServerAddress;
    }

    public IntegerFilter getIrcServerPort() {
        return ircServerPort;
    }

    public void setIrcServerPort(IntegerFilter ircServerPort) {
        this.ircServerPort = ircServerPort;
    }

    public StringFilter getIrcBotNickname() {
        return ircBotNickname;
    }

    public void setIrcBotNickname(StringFilter ircBotNickname) {
        this.ircBotNickname = ircBotNickname;
    }

    @Override
    public String toString() {
        return "IrcBotCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ircBotName != null ? "ircBotName=" + ircBotName + ", " : "") +
                (ircBotDescription != null ? "ircBotDescription=" + ircBotDescription + ", " : "") +
                (ircBotEnabled != null ? "ircBotEnabled=" + ircBotEnabled + ", " : "") +
                (ircServerAddress != null ? "ircServerAddress=" + ircServerAddress + ", " : "") +
                (ircServerPort != null ? "ircServerPort=" + ircServerPort + ", " : "") +
                (ircBotNickname != null ? "ircBotNickname=" + ircBotNickname + ", " : "") +
            "}";
    }

}
