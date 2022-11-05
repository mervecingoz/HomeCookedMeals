package com.homecookedmeals.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.homecookedmeals.domain.enumeration.Status;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Delivery.
 */
@Entity
@Table(name = "delivery")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JsonIgnoreProperties(value = { "deliveries", "customer" }, allowSetters = true)
    private CustomerAddress customerAddress;

    @ManyToOne
    @JsonIgnoreProperties(value = { "deliveries", "merchant", "meal" }, allowSetters = true)
    private MealEntry mealEntry;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Delivery id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return this.status;
    }

    public Delivery status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Delivery quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CustomerAddress getCustomerAddress() {
        return this.customerAddress;
    }

    public void setCustomerAddress(CustomerAddress customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Delivery customerAddress(CustomerAddress customerAddress) {
        this.setCustomerAddress(customerAddress);
        return this;
    }

    public MealEntry getMealEntry() {
        return this.mealEntry;
    }

    public void setMealEntry(MealEntry mealEntry) {
        this.mealEntry = mealEntry;
    }

    public Delivery mealEntry(MealEntry mealEntry) {
        this.setMealEntry(mealEntry);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Delivery)) {
            return false;
        }
        return id != null && id.equals(((Delivery) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Delivery{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
