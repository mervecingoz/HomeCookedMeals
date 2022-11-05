package com.homecookedmeals.repository;

import com.homecookedmeals.domain.MealEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MealEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MealEntryRepository extends JpaRepository<MealEntry, Long> {}
