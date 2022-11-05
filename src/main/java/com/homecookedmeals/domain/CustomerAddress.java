package com.homecookedmeals.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CustomerAddress.
 */
@Entity
@Table(name = "customer_address")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "address_name", nullable = false)
    private String addressName;

    @NotNull
    @Column(name = "adress", nullable = false)
    private String adress;

    @OneToMany(mappedBy = "customerAddress")
    @JsonIgnoreProperties(value = { "customerAddress", "mealEntry" }, allowSetters = true)
    private Set<Delivery> deliveries = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "customerAddresses" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerAddress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressName() {
        return this.addressName;
    }

    public CustomerAddress addressName(String addressName) {
        this.setAddressName(addressName);
        return this;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAdress() {
        return this.adress;
    }

    public CustomerAddress adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Set<Delivery> getDeliveries() {
        return this.deliveries;
    }

    public void setDeliveries(Set<Delivery> deliveries) {
        if (this.deliveries != null) {
            this.deliveries.forEach(i -> i.setCustomerAddress(null));
        }
        if (deliveries != null) {
            deliveries.forEach(i -> i.setCustomerAddress(this));
        }
        this.deliveries = deliveries;
    }

    public CustomerAddress deliveries(Set<Delivery> deliveries) {
        this.setDeliveries(deliveries);
        return this;
    }

    public CustomerAddress addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
        delivery.setCustomerAddress(this);
        return this;
    }

    public CustomerAddress removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
        delivery.setCustomerAddress(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerAddress customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerAddress)) {
            return false;
        }
        return id != null && id.equals(((CustomerAddress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerAddress{" +
            "id=" + getId() +
            ", addressName='" + getAddressName() + "'" +
            ", adress='" + getAdress() + "'" +
            "}";
    }
}
