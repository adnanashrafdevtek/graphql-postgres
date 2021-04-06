package com.xcordia.document.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Lookup.
 */
@Entity
@Table(name = "lookup")
public class Lookup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @OneToMany(mappedBy = "lookup")
    private Set<LookupValue> lookupValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Lookup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<LookupValue> getLookupValues() {
        return lookupValues;
    }

    public Lookup lookupValues(Set<LookupValue> lookupValues) {
        this.lookupValues = lookupValues;
        return this;
    }

    public Lookup addLookupValues(LookupValue lookupValue) {
        this.lookupValues.add(lookupValue);
        lookupValue.setLookup(this);
        return this;
    }

    public Lookup removeLookupValues(LookupValue lookupValue) {
        this.lookupValues.remove(lookupValue);
        lookupValue.setLookup(null);
        return this;
    }

    public void setLookupValues(Set<LookupValue> lookupValues) {
        this.lookupValues = lookupValues;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lookup)) {
            return false;
        }
        return id != null && id.equals(((Lookup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lookup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
