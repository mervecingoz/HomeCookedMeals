package com.homecookedmeals.service.mapper;

import com.homecookedmeals.domain.CustomerAddress;
import com.homecookedmeals.domain.Delivery;
import com.homecookedmeals.domain.MealEntry;
import com.homecookedmeals.service.dto.CustomerAddressDTO;
import com.homecookedmeals.service.dto.DeliveryDTO;
import com.homecookedmeals.service.dto.MealEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Delivery} and its DTO {@link DeliveryDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeliveryMapper extends EntityMapper<DeliveryDTO, Delivery> {
    @Mapping(target = "customerAddress", source = "customerAddress", qualifiedByName = "customerAddressId")
    @Mapping(target = "mealEntry", source = "mealEntry", qualifiedByName = "mealEntryId")
    DeliveryDTO toDto(Delivery s);

    @Named("customerAddressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerAddressDTO toDtoCustomerAddressId(CustomerAddress customerAddress);

    @Named("mealEntryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MealEntryDTO toDtoMealEntryId(MealEntry mealEntry);
}
