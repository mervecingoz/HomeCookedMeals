package com.homecookedmeals.service.mapper;

import com.homecookedmeals.domain.Customer;
import com.homecookedmeals.domain.CustomerAddress;
import com.homecookedmeals.service.dto.CustomerAddressDTO;
import com.homecookedmeals.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerAddress} and its DTO {@link CustomerAddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerAddressMapper extends EntityMapper<CustomerAddressDTO, CustomerAddress> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    CustomerAddressDTO toDto(CustomerAddress s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
