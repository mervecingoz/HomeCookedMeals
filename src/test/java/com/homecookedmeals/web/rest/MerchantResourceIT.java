package com.homecookedmeals.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.homecookedmeals.IntegrationTest;
import com.homecookedmeals.domain.Merchant;
import com.homecookedmeals.repository.MerchantRepository;
import com.homecookedmeals.service.dto.MerchantDTO;
import com.homecookedmeals.service.mapper.MerchantMapper;
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
 * Integration tests for the {@link MerchantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MerchantResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final String ENTITY_API_URL = "/api/merchants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMerchantMockMvc;

    private Merchant merchant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchant createEntity(EntityManager em) {
        Merchant merchant = new Merchant()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .rating(DEFAULT_RATING);
        return merchant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchant createUpdatedEntity(EntityManager em) {
        Merchant merchant = new Merchant()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .rating(UPDATED_RATING);
        return merchant;
    }

    @BeforeEach
    public void initTest() {
        merchant = createEntity(em);
    }

    @Test
    @Transactional
    void createMerchant() throws Exception {
        int databaseSizeBeforeCreate = merchantRepository.findAll().size();
        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);
        restMerchantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeCreate + 1);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMerchant.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMerchant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMerchant.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testMerchant.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    void createMerchantWithExistingId() throws Exception {
        // Create the Merchant with an existing ID
        merchant.setId(1L);
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        int databaseSizeBeforeCreate = merchantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMerchantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMerchants() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }

    @Test
    @Transactional
    void getMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get the merchant
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL_ID, merchant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(merchant.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING));
    }

    @Test
    @Transactional
    void getNonExistingMerchant() throws Exception {
        // Get the merchant
        restMerchantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant
        Merchant updatedMerchant = merchantRepository.findById(merchant.getId()).get();
        // Disconnect from session so that the updates on updatedMerchant are not directly saved in db
        em.detach(updatedMerchant);
        updatedMerchant
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .rating(UPDATED_RATING);
        MerchantDTO merchantDTO = merchantMapper.toDto(updatedMerchant);

        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, merchantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMerchant.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMerchant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMerchant.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testMerchant.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    void putNonExistingMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, merchantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMerchantWithPatch() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant using partial update
        Merchant partialUpdatedMerchant = new Merchant();
        partialUpdatedMerchant.setId(merchant.getId());

        partialUpdatedMerchant.lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMerchant))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMerchant.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMerchant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMerchant.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testMerchant.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    void fullUpdateMerchantWithPatch() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant using partial update
        Merchant partialUpdatedMerchant = new Merchant();
        partialUpdatedMerchant.setId(merchant.getId());

        partialUpdatedMerchant
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .rating(UPDATED_RATING);

        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMerchant))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMerchant.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMerchant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMerchant.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testMerchant.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    void patchNonExistingMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, merchantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeDelete = merchantRepository.findAll().size();

        // Delete the merchant
        restMerchantMockMvc
            .perform(delete(ENTITY_API_URL_ID, merchant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
