package com.homecookedmeals.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.homecookedmeals.IntegrationTest;
import com.homecookedmeals.domain.CustomerAddress;
import com.homecookedmeals.repository.CustomerAddressRepository;
import com.homecookedmeals.service.dto.CustomerAddressDTO;
import com.homecookedmeals.service.mapper.CustomerAddressMapper;
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
 * Integration tests for the {@link CustomerAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerAddressResourceIT {

    private static final String DEFAULT_ADDRESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private CustomerAddressMapper customerAddressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerAddressMockMvc;

    private CustomerAddress customerAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAddress createEntity(EntityManager em) {
        CustomerAddress customerAddress = new CustomerAddress().addressName(DEFAULT_ADDRESS_NAME).adress(DEFAULT_ADRESS);
        return customerAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAddress createUpdatedEntity(EntityManager em) {
        CustomerAddress customerAddress = new CustomerAddress().addressName(UPDATED_ADDRESS_NAME).adress(UPDATED_ADRESS);
        return customerAddress;
    }

    @BeforeEach
    public void initTest() {
        customerAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerAddress() throws Exception {
        int databaseSizeBeforeCreate = customerAddressRepository.findAll().size();
        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);
        restCustomerAddressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerAddress testCustomerAddress = customerAddressList.get(customerAddressList.size() - 1);
        assertThat(testCustomerAddress.getAddressName()).isEqualTo(DEFAULT_ADDRESS_NAME);
        assertThat(testCustomerAddress.getAdress()).isEqualTo(DEFAULT_ADRESS);
    }

    @Test
    @Transactional
    void createCustomerAddressWithExistingId() throws Exception {
        // Create the CustomerAddress with an existing ID
        customerAddress.setId(1L);
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        int databaseSizeBeforeCreate = customerAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerAddressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddressNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setAddressName(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        restCustomerAddressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setAdress(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        restCustomerAddressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerAddresses() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList
        restCustomerAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressName").value(hasItem(DEFAULT_ADDRESS_NAME)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)));
    }

    @Test
    @Transactional
    void getCustomerAddress() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        // Get the customerAddress
        restCustomerAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, customerAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerAddress.getId().intValue()))
            .andExpect(jsonPath("$.addressName").value(DEFAULT_ADDRESS_NAME))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS));
    }

    @Test
    @Transactional
    void getNonExistingCustomerAddress() throws Exception {
        // Get the customerAddress
        restCustomerAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerAddress() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();

        // Update the customerAddress
        CustomerAddress updatedCustomerAddress = customerAddressRepository.findById(customerAddress.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerAddress are not directly saved in db
        em.detach(updatedCustomerAddress);
        updatedCustomerAddress.addressName(UPDATED_ADDRESS_NAME).adress(UPDATED_ADRESS);
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(updatedCustomerAddress);

        restCustomerAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerAddressDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeUpdate);
        CustomerAddress testCustomerAddress = customerAddressList.get(customerAddressList.size() - 1);
        assertThat(testCustomerAddress.getAddressName()).isEqualTo(UPDATED_ADDRESS_NAME);
        assertThat(testCustomerAddress.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void putNonExistingCustomerAddress() throws Exception {
        int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();
        customerAddress.setId(count.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerAddressDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerAddress() throws Exception {
        int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();
        customerAddress.setId(count.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerAddress() throws Exception {
        int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();
        customerAddress.setId(count.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerAddressWithPatch() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();

        // Update the customerAddress using partial update
        CustomerAddress partialUpdatedCustomerAddress = new CustomerAddress();
        partialUpdatedCustomerAddress.setId(customerAddress.getId());

        partialUpdatedCustomerAddress.addressName(UPDATED_ADDRESS_NAME);

        restCustomerAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAddress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerAddress))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeUpdate);
        CustomerAddress testCustomerAddress = customerAddressList.get(customerAddressList.size() - 1);
        assertThat(testCustomerAddress.getAddressName()).isEqualTo(UPDATED_ADDRESS_NAME);
        assertThat(testCustomerAddress.getAdress()).isEqualTo(DEFAULT_ADRESS);
    }

    @Test
    @Transactional
    void fullUpdateCustomerAddressWithPatch() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();

        // Update the customerAddress using partial update
        CustomerAddress partialUpdatedCustomerAddress = new CustomerAddress();
        partialUpdatedCustomerAddress.setId(customerAddress.getId());

        partialUpdatedCustomerAddress.addressName(UPDATED_ADDRESS_NAME).adress(UPDATED_ADRESS);

        restCustomerAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAddress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerAddress))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeUpdate);
        CustomerAddress testCustomerAddress = customerAddressList.get(customerAddressList.size() - 1);
        assertThat(testCustomerAddress.getAddressName()).isEqualTo(UPDATED_ADDRESS_NAME);
        assertThat(testCustomerAddress.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerAddress() throws Exception {
        int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();
        customerAddress.setId(count.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerAddressDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerAddress() throws Exception {
        int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();
        customerAddress.setId(count.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerAddress() throws Exception {
        int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();
        customerAddress.setId(count.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerAddress() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        int databaseSizeBeforeDelete = customerAddressRepository.findAll().size();

        // Delete the customerAddress
        restCustomerAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerAddress.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerAddress> customerAddressList = customerAddressRepository.findAll();
        assertThat(customerAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
