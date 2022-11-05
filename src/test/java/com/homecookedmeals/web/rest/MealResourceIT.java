package com.homecookedmeals.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.homecookedmeals.IntegrationTest;
import com.homecookedmeals.domain.Meal;
import com.homecookedmeals.repository.MealRepository;
import com.homecookedmeals.service.dto.MealDTO;
import com.homecookedmeals.service.mapper.MealMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MealResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MealResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealMapper mealMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMealMockMvc;

    private Meal meal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meal createEntity(EntityManager em) {
        Meal meal = new Meal().name(DEFAULT_NAME);
        return meal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meal createUpdatedEntity(EntityManager em) {
        Meal meal = new Meal().name(UPDATED_NAME);
        return meal;
    }

    @BeforeEach
    public void initTest() {
        meal = createEntity(em);
    }

    @Test
    @Transactional
    void createMeal() throws Exception {
        int databaseSizeBeforeCreate = mealRepository.findAll().size();
        // Create the Meal
        MealDTO mealDTO = mealMapper.toDto(meal);
        restMealMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeCreate + 1);
        Meal testMeal = mealList.get(mealList.size() - 1);
        assertThat(testMeal.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createMealWithExistingId() throws Exception {
        // Create the Meal with an existing ID
        meal.setId(1L);
        MealDTO mealDTO = mealMapper.toDto(meal);

        int databaseSizeBeforeCreate = mealRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMealMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mealRepository.findAll().size();
        // set the field null
        meal.setName(null);

        // Create the Meal, which fails.
        MealDTO mealDTO = mealMapper.toDto(meal);

        restMealMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isBadRequest());

        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMeals() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        // Get all the mealList
        restMealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meal.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getMeal() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        // Get the meal
        restMealMockMvc
            .perform(get(ENTITY_API_URL_ID, meal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meal.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMeal() throws Exception {
        // Get the meal
        restMealMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMeal() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        int databaseSizeBeforeUpdate = mealRepository.findAll().size();

        // Update the meal
        Meal updatedMeal = mealRepository.findById(meal.getId()).get();
        // Disconnect from session so that the updates on updatedMeal are not directly saved in db
        em.detach(updatedMeal);
        updatedMeal.name(UPDATED_NAME);
        MealDTO mealDTO = mealMapper.toDto(updatedMeal);

        restMealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isOk());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
        Meal testMeal = mealList.get(mealList.size() - 1);
        assertThat(testMeal.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingMeal() throws Exception {
        int databaseSizeBeforeUpdate = mealRepository.findAll().size();
        meal.setId(count.incrementAndGet());

        // Create the Meal
        MealDTO mealDTO = mealMapper.toDto(meal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeal() throws Exception {
        int databaseSizeBeforeUpdate = mealRepository.findAll().size();
        meal.setId(count.incrementAndGet());

        // Create the Meal
        MealDTO mealDTO = mealMapper.toDto(meal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeal() throws Exception {
        int databaseSizeBeforeUpdate = mealRepository.findAll().size();
        meal.setId(count.incrementAndGet());

        // Create the Meal
        MealDTO mealDTO = mealMapper.toDto(meal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMealWithPatch() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        int databaseSizeBeforeUpdate = mealRepository.findAll().size();

        // Update the meal using partial update
        Meal partialUpdatedMeal = new Meal();
        partialUpdatedMeal.setId(meal.getId());

        partialUpdatedMeal.name(UPDATED_NAME);

        restMealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeal))
            )
            .andExpect(status().isOk());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
        Meal testMeal = mealList.get(mealList.size() - 1);
        assertThat(testMeal.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateMealWithPatch() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        int databaseSizeBeforeUpdate = mealRepository.findAll().size();

        // Update the meal using partial update
        Meal partialUpdatedMeal = new Meal();
        partialUpdatedMeal.setId(meal.getId());

        partialUpdatedMeal.name(UPDATED_NAME);

        restMealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeal))
            )
            .andExpect(status().isOk());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
        Meal testMeal = mealList.get(mealList.size() - 1);
        assertThat(testMeal.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingMeal() throws Exception {
        int databaseSizeBeforeUpdate = mealRepository.findAll().size();
        meal.setId(count.incrementAndGet());

        // Create the Meal
        MealDTO mealDTO = mealMapper.toDto(meal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mealDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeal() throws Exception {
        int databaseSizeBeforeUpdate = mealRepository.findAll().size();
        meal.setId(count.incrementAndGet());

        // Create the Meal
        MealDTO mealDTO = mealMapper.toDto(meal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeal() throws Exception {
        int databaseSizeBeforeUpdate = mealRepository.findAll().size();
        meal.setId(count.incrementAndGet());

        // Create the Meal
        MealDTO mealDTO = mealMapper.toDto(meal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meal in the database
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeal() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        int databaseSizeBeforeDelete = mealRepository.findAll().size();

        // Delete the meal
        restMealMockMvc
            .perform(delete(ENTITY_API_URL_ID, meal.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Meal> mealList = mealRepository.findAll();
        assertThat(mealList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
