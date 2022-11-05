package com.homecookedmeals.service.dto;

import com.homecookedmeals.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.homecookedmeals.domain.Delivery} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryDTO implements Serializable {

    private Long id;

    @NotNull
    private Status status;

    private CustomerAddressDTO customerAddress;

    private MealEntryDTO mealEntry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CustomerAddressDTO getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(CustomerAddressDTO customerAddress) {
        this.customerAddress = customerAddress;
    }

    public MealEntryDTO getMealEntry() {
        return mealEntry;
    }

    public void setMealEntry(MealEntryDTO mealEntry) {
        this.mealEntry = mealEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryDTO)) {
            return false;
        }

        DeliveryDTO deliveryDTO = (DeliveryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", customerAddress=" + getCustomerAddress() +
            ", mealEntry=" + getMealEntry() +
            "}";
    }
}
