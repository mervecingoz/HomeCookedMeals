package com.homecookedmeals.repository;

import com.homecookedmeals.domain.Meal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Meal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {}
