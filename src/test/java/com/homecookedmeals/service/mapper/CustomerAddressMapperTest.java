package com.homecookedmeals.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerAddressMapperTest {

    private CustomerAddressMapper customerAddressMapper;

    @BeforeEach
    public void setUp() {
        customerAddressMapper = new CustomerAddressMapperImpl();
    }
}
