package com.homecookedmeals.service.mapper;

import com.homecookedmeals.domain.Customer;
import com.homecookedmeals.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {}
