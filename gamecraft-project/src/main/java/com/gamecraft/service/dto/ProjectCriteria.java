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
 * Criteria class for the Project entity. This class is used in ProjectResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /projects?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter projectName;

    private StringFilter projectDescription;

    private StringFilter projectWebsite;

    private StringFilter projectLogo;

    private BooleanFilter projectArchived;

    public ProjectCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProjectName() {
        return projectName;
    }

    public void setProjectName(StringFilter projectName) {
        this.projectName = projectName;
    }

    public StringFilter getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(StringFilter projectDescription) {
        this.projectDescription = projectDescription;
    }

    public StringFilter getProjectWebsite() {
        return projectWebsite;
    }

    public void setProjectWebsite(StringFilter projectWebsite) {
        this.projectWebsite = projectWebsite;
    }

    public StringFilter getProjectLogo() {
        return projectLogo;
    }

    public void setProjectLogo(StringFilter projectLogo) {
        this.projectLogo = projectLogo;
    }

    public BooleanFilter getProjectArchived() {
        return projectArchived;
    }

    public void setProjectArchived(BooleanFilter projectArchived) {
        this.projectArchived = projectArchived;
    }

    @Override
    public String toString() {
        return "ProjectCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (projectName != null ? "projectName=" + projectName + ", " : "") +
                (projectDescription != null ? "projectDescription=" + projectDescription + ", " : "") +
                (projectWebsite != null ? "projectWebsite=" + projectWebsite + ", " : "") +
                (projectLogo != null ? "projectLogo=" + projectLogo + ", " : "") +
                (projectArchived != null ? "projectArchived=" + projectArchived + ", " : "") +
            "}";
    }

}
