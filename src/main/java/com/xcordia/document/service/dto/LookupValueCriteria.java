package com.xcordia.document.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.xcordia.document.domain.LookupValue} entity. This class is used
 * in {@link com.xcordia.document.web.rest.LookupValueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lookup-values?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LookupValueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter value;

    private LongFilter lookupId;

    public LookupValueCriteria() {
    }

    public LookupValueCriteria(LookupValueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.lookupId = other.lookupId == null ? null : other.lookupId.copy();
    }

    @Override
    public LookupValueCriteria copy() {
        return new LookupValueCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getValue() {
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public LongFilter getLookupId() {
        return lookupId;
    }

    public void setLookupId(LongFilter lookupId) {
        this.lookupId = lookupId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LookupValueCriteria that = (LookupValueCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(value, that.value) &&
            Objects.equals(lookupId, that.lookupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        value,
        lookupId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LookupValueCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (lookupId != null ? "lookupId=" + lookupId + ", " : "") +
            "}";
    }

}
