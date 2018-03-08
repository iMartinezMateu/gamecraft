package com.gamecraft.service.dto;

import java.io.Serializable;
import com.gamecraft.domain.enumeration.Role;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Groups entity. This class is used in GroupsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /groups?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GroupsCriteria implements Serializable {
    /**
     * Class for filtering Role
     */
    public static class RoleFilter extends Filter<Role> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter groupName;

    private StringFilter groupDescription;

    private RoleFilter groupRole;

    private BooleanFilter groupEnabled;

    public GroupsCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getGroupName() {
        return groupName;
    }

    public void setGroupName(StringFilter groupName) {
        this.groupName = groupName;
    }

    public StringFilter getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(StringFilter groupDescription) {
        this.groupDescription = groupDescription;
    }

    public RoleFilter getGroupRole() {
        return groupRole;
    }

    public void setGroupRole(RoleFilter groupRole) {
        this.groupRole = groupRole;
    }

    public BooleanFilter getGroupEnabled() {
        return groupEnabled;
    }

    public void setGroupEnabled(BooleanFilter groupEnabled) {
        this.groupEnabled = groupEnabled;
    }

    @Override
    public String toString() {
        return "GroupsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (groupName != null ? "groupName=" + groupName + ", " : "") +
                (groupDescription != null ? "groupDescription=" + groupDescription + ", " : "") +
                (groupRole != null ? "groupRole=" + groupRole + ", " : "") +
                (groupEnabled != null ? "groupEnabled=" + groupEnabled + ", " : "") +
            "}";
    }

}
