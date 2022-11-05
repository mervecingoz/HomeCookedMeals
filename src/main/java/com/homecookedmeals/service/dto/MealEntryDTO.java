package com.homecookedmeals.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.homecookedmeals.domain.MealEntry} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MealEntryDTO implements Serializable {

    private Long id;

    private Instant date;

    private Integer quota;

    private Integer remainingQuota;

    private MerchantDTO merchant;

    private MealDTO meal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getRemainingQuota() {
        return remainingQuota;
    }

    public void setRemainingQuota(Integer remainingQuota) {
        this.remainingQuota = remainingQuota;
    }

    public MerchantDTO getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantDTO merchant) {
        this.merchant = merchant;
    }

    public MealDTO getMeal() {
        return meal;
    }

    public void setMeal(MealDTO meal) {
        this.meal = meal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MealEntryDTO)) {
            return false;
        }

        MealEntryDTO mealEntryDTO = (MealEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mealEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealEntryDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", quota=" + getQuota() +
            ", remainingQuota=" + getRemainingQuota() +
            ", merchant=" + getMerchant() +
            ", meal=" + getMeal() +
            "}";
    }
}
