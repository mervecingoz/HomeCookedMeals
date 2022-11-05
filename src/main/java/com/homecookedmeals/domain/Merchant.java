package com.homecookedmeals.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Merchant.
 */
@Entity
@Table(name = "merchant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rating")
    private Integer rating;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "merchant")
    @JsonIgnoreProperties(value = { "deliveries", "merchant", "meal" }, allowSetters = true)
    private Set<MealEntry> mealEntries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Merchant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return this.rating;
    }

    public Merchant rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Merchant user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<MealEntry> getMealEntries() {
        return this.mealEntries;
    }

    public void setMealEntries(Set<MealEntry> mealEntries) {
        if (this.mealEntries != null) {
            this.mealEntries.forEach(i -> i.setMerchant(null));
        }
        if (mealEntries != null) {
            mealEntries.forEach(i -> i.setMerchant(this));
        }
        this.mealEntries = mealEntries;
    }

    public Merchant mealEntries(Set<MealEntry> mealEntries) {
        this.setMealEntries(mealEntries);
        return this;
    }

    public Merchant addMealEntry(MealEntry mealEntry) {
        this.mealEntries.add(mealEntry);
        mealEntry.setMerchant(this);
        return this;
    }

    public Merchant removeMealEntry(MealEntry mealEntry) {
        this.mealEntries.remove(mealEntry);
        mealEntry.setMerchant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Merchant)) {
            return false;
        }
        return id != null && id.equals(((Merchant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Merchant{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            "}";
    }
}
