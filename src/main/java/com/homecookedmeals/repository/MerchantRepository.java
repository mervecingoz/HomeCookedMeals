package com.homecookedmeals.repository;

import com.homecookedmeals.domain.Merchant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Merchant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {}
