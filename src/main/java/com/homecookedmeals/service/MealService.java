package com.homecookedmeals.service;

import com.homecookedmeals.service.dto.MealDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.homecookedmeals.domain.Meal}.
 */
public interface MealService {
    /**
     * Save a meal.
     *
     * @param mealDTO the entity to save.
     * @return the persisted entity.
     */
    MealDTO save(MealDTO mealDTO);

    /**
     * Updates a meal.
     *
     * @param mealDTO the entity to update.
     * @return the persisted entity.
     */
    MealDTO update(MealDTO mealDTO);

    /**
     * Partially updates a meal.
     *
     * @param mealDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MealDTO> partialUpdate(MealDTO mealDTO);

    /**
     * Get all the meals.
     *
     * @return the list of entities.
     */
    List<MealDTO> findAll();

    /**
     * Get the "id" meal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MealDTO> findOne(Long id);

    /**
     * Delete the "id" meal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
