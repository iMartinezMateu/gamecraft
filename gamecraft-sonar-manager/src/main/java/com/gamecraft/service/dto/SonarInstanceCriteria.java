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
 * Criteria class for the SonarInstance entity. This class is used in SonarInstanceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sonar-instances?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SonarInstanceCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter sonarInstanceName;

    private StringFilter sonarInstanceDescription;

    private StringFilter sonarInstanceRunnerPath;

    private BooleanFilter sonarInstanceEnabled;

    public SonarInstanceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSonarInstanceName() {
        return sonarInstanceName;
    }

    public void setSonarInstanceName(StringFilter sonarInstanceName) {
        this.sonarInstanceName = sonarInstanceName;
    }

    public StringFilter getSonarInstanceDescription() {
        return sonarInstanceDescription;
    }

    public void setSonarInstanceDescription(StringFilter sonarInstanceDescription) {
        this.sonarInstanceDescription = sonarInstanceDescription;
    }

    public StringFilter getSonarInstanceRunnerPath() {
        return sonarInstanceRunnerPath;
    }

    public void setSonarInstanceRunnerPath(StringFilter sonarInstanceRunnerPath) {
        this.sonarInstanceRunnerPath = sonarInstanceRunnerPath;
    }

    public BooleanFilter getSonarInstanceEnabled() {
        return sonarInstanceEnabled;
    }

    public void setSonarInstanceEnabled(BooleanFilter sonarInstanceEnabled) {
        this.sonarInstanceEnabled = sonarInstanceEnabled;
    }

    @Override
    public String toString() {
        return "SonarInstanceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sonarInstanceName != null ? "sonarInstanceName=" + sonarInstanceName + ", " : "") +
                (sonarInstanceDescription != null ? "sonarInstanceDescription=" + sonarInstanceDescription + ", " : "") +
                (sonarInstanceRunnerPath != null ? "sonarInstanceRunnerPath=" + sonarInstanceRunnerPath + ", " : "") +
                (sonarInstanceEnabled != null ? "sonarInstanceEnabled=" + sonarInstanceEnabled + ", " : "") +
            "}";
    }

}
