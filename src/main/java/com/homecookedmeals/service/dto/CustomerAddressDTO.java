package com.homecookedmeals.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.homecookedmeals.domain.CustomerAddress} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerAddressDTO implements Serializable {

    private Long id;

    @NotNull
    private String addressName;

    @NotNull
    private String adress;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerAddressDTO)) {
            return false;
        }

        CustomerAddressDTO customerAddressDTO = (CustomerAddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerAddressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerAddressDTO{" +
            "id=" + getId() +
            ", addressName='" + getAddressName() + "'" +
            ", adress='" + getAdress() + "'" +
            ", customer=" + getCustomer() +
            "}";
    }
}
