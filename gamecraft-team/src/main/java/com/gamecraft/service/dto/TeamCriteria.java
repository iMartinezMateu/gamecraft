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
 * Criteria class for the Team entity. This class is used in TeamResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /teams?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TeamCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter teamName;

    private StringFilter teamDescription;

    private LongFilter teamUserId;

    public TeamCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTeamName() {
        return teamName;
    }

    public void setTeamName(StringFilter teamName) {
        this.teamName = teamName;
    }

    public StringFilter getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(StringFilter teamDescription) {
        this.teamDescription = teamDescription;
    }

    public LongFilter getTeamUserId() {
        return teamUserId;
    }

    public void setTeamUserId(LongFilter teamUserId) {
        this.teamUserId = teamUserId;
    }

    @Override
    public String toString() {
        return "TeamCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (teamName != null ? "teamName=" + teamName + ", " : "") +
                (teamDescription != null ? "teamDescription=" + teamDescription + ", " : "") +
                (teamUserId != null ? "teamUserId=" + teamUserId + ", " : "") +
            "}";
    }

}
