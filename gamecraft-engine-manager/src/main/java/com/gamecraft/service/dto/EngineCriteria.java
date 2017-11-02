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
 * Criteria class for the Engine entity. This class is used in EngineResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /engines?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EngineCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter engineName;

    private StringFilter engineDescription;

    private StringFilter engineCompilerPath;

    private StringFilter engineCompilerArguments;

    public EngineCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEngineName() {
        return engineName;
    }

    public void setEngineName(StringFilter engineName) {
        this.engineName = engineName;
    }

    public StringFilter getEngineDescription() {
        return engineDescription;
    }

    public void setEngineDescription(StringFilter engineDescription) {
        this.engineDescription = engineDescription;
    }

    public StringFilter getEngineCompilerPath() {
        return engineCompilerPath;
    }

    public void setEngineCompilerPath(StringFilter engineCompilerPath) {
        this.engineCompilerPath = engineCompilerPath;
    }

    public StringFilter getEngineCompilerArguments() {
        return engineCompilerArguments;
    }

    public void setEngineCompilerArguments(StringFilter engineCompilerArguments) {
        this.engineCompilerArguments = engineCompilerArguments;
    }

    @Override
    public String toString() {
        return "EngineCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (engineName != null ? "engineName=" + engineName + ", " : "") +
                (engineDescription != null ? "engineDescription=" + engineDescription + ", " : "") +
                (engineCompilerPath != null ? "engineCompilerPath=" + engineCompilerPath + ", " : "") +
                (engineCompilerArguments != null ? "engineCompilerArguments=" + engineCompilerArguments + ", " : "") +
            "}";
    }

}
