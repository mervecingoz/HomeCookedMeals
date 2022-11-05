package com.homecookedmeals.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A MealEntry.
 */
@Entity
@Table(name = "meal_entry")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MealEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    @Column(name = "quota")
    private Integer quota;

    @Column(name = "remaining_quota")
    private Integer remainingQuota;

    @OneToMany(mappedBy = "mealEntry")
    @JsonIgnoreProperties(value = { "customerAddress", "mealEntry" }, allowSetters = true)
    private Set<Delivery> deliveries = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "mealEntries" }, allowSetters = true)
    private Merchant merchant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "mealEntries" }, allowSetters = true)
    private Meal meal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MealEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public MealEntry date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getQuota() {
        return this.quota;
    }

    public MealEntry quota(Integer quota) {
        this.setQuota(quota);
        return this;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getRemainingQuota() {
        return this.remainingQuota;
    }

    public MealEntry remainingQuota(Integer remainingQuota) {
        this.setRemainingQuota(remainingQuota);
        return this;
    }

    public void setRemainingQuota(Integer remainingQuota) {
        this.remainingQuota = remainingQuota;
    }

    public Set<Delivery> getDeliveries() {
        return this.deliveries;
    }

    public void setDeliveries(Set<Delivery> deliveries) {
        if (this.deliveries != null) {
            this.deliveries.forEach(i -> i.setMealEntry(null));
        }
        if (deliveries != null) {
            deliveries.forEach(i -> i.setMealEntry(this));
        }
        this.deliveries = deliveries;
    }

    public MealEntry deliveries(Set<Delivery> deliveries) {
        this.setDeliveries(deliveries);
        return this;
    }

    public MealEntry addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
        delivery.setMealEntry(this);
        return this;
    }

    public MealEntry removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
        delivery.setMealEntry(null);
        return this;
    }

    public Merchant getMerchant() {
        return this.merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public MealEntry merchant(Merchant merchant) {
        this.setMerchant(merchant);
        return this;
    }

    public Meal getMeal() {
        return this.meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public MealEntry meal(Meal meal) {
        this.setMeal(meal);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MealEntry)) {
            return false;
        }
        return id != null && id.equals(((MealEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealEntry{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", quota=" + getQuota() +
            ", remainingQuota=" + getRemainingQuota() +
            "}";
    }
}
