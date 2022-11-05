package com.homecookedmeals.repository;

import com.homecookedmeals.domain.CustomerAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {}
