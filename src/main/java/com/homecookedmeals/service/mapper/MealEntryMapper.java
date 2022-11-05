package com.homecookedmeals.service.mapper;

import com.homecookedmeals.domain.Meal;
import com.homecookedmeals.domain.MealEntry;
import com.homecookedmeals.domain.Merchant;
import com.homecookedmeals.service.dto.MealDTO;
import com.homecookedmeals.service.dto.MealEntryDTO;
import com.homecookedmeals.service.dto.MerchantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MealEntry} and its DTO {@link MealEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MealEntryMapper extends EntityMapper<MealEntryDTO, MealEntry> {
    @Mapping(target = "meal", source = "meal", qualifiedByName = "mealId")
    @Mapping(target = "merchant", source = "merchant", qualifiedByName = "merchantId")
    MealEntryDTO toDto(MealEntry s);

    @Named("mealId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MealDTO toDtoMealId(Meal meal);

    @Named("merchantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MerchantDTO toDtoMerchantId(Merchant merchant);
}
