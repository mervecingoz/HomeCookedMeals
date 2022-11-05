package com.homecookedmeals.service;

import com.homecookedmeals.service.dto.MealEntryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.homecookedmeals.domain.MealEntry}.
 */
public interface MealEntryService {
    /**
     * Save a mealEntry.
     *
     * @param mealEntryDTO the entity to save.
     * @return the persisted entity.
     */
    MealEntryDTO save(MealEntryDTO mealEntryDTO);

    /**
     * Updates a mealEntry.
     *
     * @param mealEntryDTO the entity to update.
     * @return the persisted entity.
     */
    MealEntryDTO update(MealEntryDTO mealEntryDTO);

    /**
     * Partially updates a mealEntry.
     *
     * @param mealEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MealEntryDTO> partialUpdate(MealEntryDTO mealEntryDTO);

    /**
     * Get all the mealEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MealEntryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mealEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MealEntryDTO> findOne(Long id);

    /**
     * Delete the "id" mealEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
