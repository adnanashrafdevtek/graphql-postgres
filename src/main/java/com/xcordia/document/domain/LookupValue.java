package com.xcordia.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A LookupValue.
 */
@Entity
@Table(name = "lookup_value")
public class LookupValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "value", length = 50, nullable = false)
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = "lookupValues", allowSetters = true)
    private Lookup lookup;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public LookupValue value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Lookup getLookup() {
        return lookup;
    }

    public LookupValue lookup(Lookup lookup) {
        this.lookup = lookup;
        return this;
    }

    public void setLookup(Lookup lookup) {
        this.lookup = lookup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LookupValue)) {
            return false;
        }
        return id != null && id.equals(((LookupValue) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LookupValue{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
