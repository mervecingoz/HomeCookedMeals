package com.homecookedmeals.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Meal.
 */
@Entity
@Table(name = "meal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Meal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "meal")
    @JsonIgnoreProperties(value = { "deliveries", "merchant", "meal" }, allowSetters = true)
    private Set<MealEntry> mealEntries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Meal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Meal name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MealEntry> getMealEntries() {
        return this.mealEntries;
    }

    public void setMealEntries(Set<MealEntry> mealEntries) {
        if (this.mealEntries != null) {
            this.mealEntries.forEach(i -> i.setMeal(null));
        }
        if (mealEntries != null) {
            mealEntries.forEach(i -> i.setMeal(this));
        }
        this.mealEntries = mealEntries;
    }

    public Meal mealEntries(Set<MealEntry> mealEntries) {
        this.setMealEntries(mealEntries);
        return this;
    }

    public Meal addMealEntry(MealEntry mealEntry) {
        this.mealEntries.add(mealEntry);
        mealEntry.setMeal(this);
        return this;
    }

    public Meal removeMealEntry(MealEntry mealEntry) {
        this.mealEntries.remove(mealEntry);
        mealEntry.setMeal(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Meal)) {
            return false;
        }
        return id != null && id.equals(((Meal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Meal{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
