package com.homecookedmeals.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.homecookedmeals.IntegrationTest;
import com.homecookedmeals.domain.MealEntry;
import com.homecookedmeals.repository.MealEntryRepository;
import com.homecookedmeals.service.dto.MealEntryDTO;
import com.homecookedmeals.service.mapper.MealEntryMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link MealEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MealEntryResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUOTA = 1;
    private static final Integer UPDATED_QUOTA = 2;

    private static final Integer DEFAULT_REMAINING_QUOTA = 1;
    private static final Integer UPDATED_REMAINING_QUOTA = 2;

    private static final String ENTITY_API_URL = "/api/meal-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MealEntryRepository mealEntryRepository;

    @Autowired
    private MealEntryMapper mealEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMealEntryMockMvc;

    private MealEntry mealEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MealEntry createEntity(EntityManager em) {
        MealEntry mealEntry = new MealEntry().date(DEFAULT_DATE).quota(DEFAULT_QUOTA).remainingQuota(DEFAULT_REMAINING_QUOTA);
        return mealEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MealEntry createUpdatedEntity(EntityManager em) {
        MealEntry mealEntry = new MealEntry().date(UPDATED_DATE).quota(UPDATED_QUOTA).remainingQuota(UPDATED_REMAINING_QUOTA);
        return mealEntry;
    }

    @BeforeEach
    public void initTest() {
        mealEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createMealEntry() throws Exception {
        int databaseSizeBeforeCreate = mealEntryRepository.findAll().size();
        // Create the MealEntry
        MealEntryDTO mealEntryDTO = mealEntryMapper.toDto(mealEntry);
        restMealEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeCreate + 1);
        MealEntry testMealEntry = mealEntryList.get(mealEntryList.size() - 1);
        assertThat(testMealEntry.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMealEntry.getQuota()).isEqualTo(DEFAULT_QUOTA);
        assertThat(testMealEntry.getRemainingQuota()).isEqualTo(DEFAULT_REMAINING_QUOTA);
    }

    @Test
    @Transactional
    void createMealEntryWithExistingId() throws Exception {
        // Create the MealEntry with an existing ID
        mealEntry.setId(1L);
        MealEntryDTO mealEntryDTO = mealEntryMapper.toDto(mealEntry);

        int databaseSizeBeforeCreate = mealEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMealEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMealEntries() throws Exception {
        // Initialize the database
        mealEntryRepository.saveAndFlush(mealEntry);

        // Get all the mealEntryList
        restMealEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mealEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].quota").value(hasItem(DEFAULT_QUOTA)))
            .andExpect(jsonPath("$.[*].remainingQuota").value(hasItem(DEFAULT_REMAINING_QUOTA)));
    }

    @Test
    @Transactional
    void getMealEntry() throws Exception {
        // Initialize the database
        mealEntryRepository.saveAndFlush(mealEntry);

        // Get the mealEntry
        restMealEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, mealEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mealEntry.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.quota").value(DEFAULT_QUOTA))
            .andExpect(jsonPath("$.remainingQuota").value(DEFAULT_REMAINING_QUOTA));
    }

    @Test
    @Transactional
    void getNonExistingMealEntry() throws Exception {
        // Get the mealEntry
        restMealEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMealEntry() throws Exception {
        // Initialize the database
        mealEntryRepository.saveAndFlush(mealEntry);

        int databaseSizeBeforeUpdate = mealEntryRepository.findAll().size();

        // Update the mealEntry
        MealEntry updatedMealEntry = mealEntryRepository.findById(mealEntry.getId()).get();
        // Disconnect from session so that the updates on updatedMealEntry are not directly saved in db
        em.detach(updatedMealEntry);
        updatedMealEntry.date(UPDATED_DATE).quota(UPDATED_QUOTA).remainingQuota(UPDATED_REMAINING_QUOTA);
        MealEntryDTO mealEntryDTO = mealEntryMapper.toDto(updatedMealEntry);

        restMealEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealEntryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeUpdate);
        MealEntry testMealEntry = mealEntryList.get(mealEntryList.size() - 1);
        assertThat(testMealEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMealEntry.getQuota()).isEqualTo(UPDATED_QUOTA);
        assertThat(testMealEntry.getRemainingQuota()).isEqualTo(UPDATED_REMAINING_QUOTA);
    }

    @Test
    @Transactional
    void putNonExistingMealEntry() throws Exception {
        int databaseSizeBeforeUpdate = mealEntryRepository.findAll().size();
        mealEntry.setId(count.incrementAndGet());

        // Create the MealEntry
        MealEntryDTO mealEntryDTO = mealEntryMapper.toDto(mealEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealEntryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMealEntry() throws Exception {
        int databaseSizeBeforeUpdate = mealEntryRepository.findAll().size();
        mealEntry.setId(count.incrementAndGet());

        // Create the MealEntry
        MealEntryDTO mealEntryDTO = mealEntryMapper.toDto(mealEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMealEntry() throws Exception {
        int databaseSizeBeforeUpdate = mealEntryRepository.findAll().size();
        mealEntry.setId(count.incrementAndGet());

        // Create the MealEntry
        MealEntryDTO mealEntryDTO = mealEntryMapper.toDto(mealEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealEntryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMealEntryWithPatch() throws Exception {
        // Initialize the database
        mealEntryRepository.saveAndFlush(mealEntry);

        int databaseSizeBeforeUpdate = mealEntryRepository.findAll().size();

        // Update the mealEntry using partial update
        MealEntry partialUpdatedMealEntry = new MealEntry();
        partialUpdatedMealEntry.setId(mealEntry.getId());

        partialUpdatedMealEntry.date(UPDATED_DATE);

        restMealEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMealEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMealEntry))
            )
            .andExpect(status().isOk());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeUpdate);
        MealEntry testMealEntry = mealEntryList.get(mealEntryList.size() - 1);
        assertThat(testMealEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMealEntry.getQuota()).isEqualTo(DEFAULT_QUOTA);
        assertThat(testMealEntry.getRemainingQuota()).isEqualTo(DEFAULT_REMAINING_QUOTA);
    }

    @Test
    @Transactional
    void fullUpdateMealEntryWithPatch() throws Exception {
        // Initialize the database
        mealEntryRepository.saveAndFlush(mealEntry);

        int databaseSizeBeforeUpdate = mealEntryRepository.findAll().size();

        // Update the mealEntry using partial update
        MealEntry partialUpdatedMealEntry = new MealEntry();
        partialUpdatedMealEntry.setId(mealEntry.getId());

        partialUpdatedMealEntry.date(UPDATED_DATE).quota(UPDATED_QUOTA).remainingQuota(UPDATED_REMAINING_QUOTA);

        restMealEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMealEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMealEntry))
            )
            .andExpect(status().isOk());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeUpdate);
        MealEntry testMealEntry = mealEntryList.get(mealEntryList.size() - 1);
        assertThat(testMealEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMealEntry.getQuota()).isEqualTo(UPDATED_QUOTA);
        assertThat(testMealEntry.getRemainingQuota()).isEqualTo(UPDATED_REMAINING_QUOTA);
    }

    @Test
    @Transactional
    void patchNonExistingMealEntry() throws Exception {
        int databaseSizeBeforeUpdate = mealEntryRepository.findAll().size();
        mealEntry.setId(count.incrementAndGet());

        // Create the MealEntry
        MealEntryDTO mealEntryDTO = mealEntryMapper.toDto(mealEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mealEntryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMealEntry() throws Exception {
        int databaseSizeBeforeUpdate = mealEntryRepository.findAll().size();
        mealEntry.setId(count.incrementAndGet());

        // Create the MealEntry
        MealEntryDTO mealEntryDTO = mealEntryMapper.toDto(mealEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMealEntry() throws Exception {
        int databaseSizeBeforeUpdate = mealEntryRepository.findAll().size();
        mealEntry.setId(count.incrementAndGet());

        // Create the MealEntry
        MealEntryDTO mealEntryDTO = mealEntryMapper.toDto(mealEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MealEntry in the database
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMealEntry() throws Exception {
        // Initialize the database
        mealEntryRepository.saveAndFlush(mealEntry);

        int databaseSizeBeforeDelete = mealEntryRepository.findAll().size();

        // Delete the mealEntry
        restMealEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, mealEntry.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MealEntry> mealEntryList = mealEntryRepository.findAll();
        assertThat(mealEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
