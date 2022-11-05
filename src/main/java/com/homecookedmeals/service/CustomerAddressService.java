package com.homecookedmeals.service;

import com.homecookedmeals.service.dto.CustomerAddressDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.homecookedmeals.domain.CustomerAddress}.
 */
public interface CustomerAddressService {
    /**
     * Save a customerAddress.
     *
     * @param customerAddressDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerAddressDTO save(CustomerAddressDTO customerAddressDTO);

    /**
     * Updates a customerAddress.
     *
     * @param customerAddressDTO the entity to update.
     * @return the persisted entity.
     */
    CustomerAddressDTO update(CustomerAddressDTO customerAddressDTO);

    /**
     * Partially updates a customerAddress.
     *
     * @param customerAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerAddressDTO> partialUpdate(CustomerAddressDTO customerAddressDTO);

    /**
     * Get all the customerAddresses.
     *
     * @return the list of entities.
     */
    List<CustomerAddressDTO> findAll();

    /**
     * Get the "id" customerAddress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerAddressDTO> findOne(Long id);

    /**
     * Delete the "id" customerAddress.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
