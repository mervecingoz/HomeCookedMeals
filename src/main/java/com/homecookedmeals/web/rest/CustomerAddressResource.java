package com.homecookedmeals.web.rest;

import com.homecookedmeals.repository.CustomerAddressRepository;
import com.homecookedmeals.service.CustomerAddressService;
import com.homecookedmeals.service.dto.CustomerAddressDTO;
import com.homecookedmeals.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.homecookedmeals.domain.CustomerAddress}.
 */
@RestController
@RequestMapping("/api")
public class CustomerAddressResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAddressResource.class);

    private static final String ENTITY_NAME = "customerAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerAddressService customerAddressService;

    private final CustomerAddressRepository customerAddressRepository;

    public CustomerAddressResource(CustomerAddressService customerAddressService, CustomerAddressRepository customerAddressRepository) {
        this.customerAddressService = customerAddressService;
        this.customerAddressRepository = customerAddressRepository;
    }

    /**
     * {@code POST  /customer-addresses} : Create a new customerAddress.
     *
     * @param customerAddressDTO the customerAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerAddressDTO, or with status {@code 400 (Bad Request)} if the customerAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-addresses")
    public ResponseEntity<CustomerAddressDTO> createCustomerAddress(@Valid @RequestBody CustomerAddressDTO customerAddressDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomerAddress : {}", customerAddressDTO);
        if (customerAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerAddressDTO result = customerAddressService.save(customerAddressDTO);
        return ResponseEntity
            .created(new URI("/api/customer-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-addresses/:id} : Updates an existing customerAddress.
     *
     * @param id the id of the customerAddressDTO to save.
     * @param customerAddressDTO the customerAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAddressDTO,
     * or with status {@code 400 (Bad Request)} if the customerAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-addresses/{id}")
    public ResponseEntity<CustomerAddressDTO> updateCustomerAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerAddressDTO customerAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerAddress : {}, {}", id, customerAddressDTO);
        if (customerAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerAddressDTO result = customerAddressService.update(customerAddressDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-addresses/:id} : Partial updates given fields of an existing customerAddress, field will ignore if it is null
     *
     * @param id the id of the customerAddressDTO to save.
     * @param customerAddressDTO the customerAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAddressDTO,
     * or with status {@code 400 (Bad Request)} if the customerAddressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerAddressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-addresses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerAddressDTO> partialUpdateCustomerAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerAddressDTO customerAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerAddress partially : {}, {}", id, customerAddressDTO);
        if (customerAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerAddressDTO> result = customerAddressService.partialUpdate(customerAddressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerAddressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-addresses} : get all the customerAddresses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerAddresses in body.
     */
    @GetMapping("/customer-addresses")
    public List<CustomerAddressDTO> getAllCustomerAddresses() {
        log.debug("REST request to get all CustomerAddresses");
        return customerAddressService.findAll();
    }

    /**
     * {@code GET  /customer-addresses/:id} : get the "id" customerAddress.
     *
     * @param id the id of the customerAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-addresses/{id}")
    public ResponseEntity<CustomerAddressDTO> getCustomerAddress(@PathVariable Long id) {
        log.debug("REST request to get CustomerAddress : {}", id);
        Optional<CustomerAddressDTO> customerAddressDTO = customerAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerAddressDTO);
    }

    /**
     * {@code DELETE  /customer-addresses/:id} : delete the "id" customerAddress.
     *
     * @param id the id of the customerAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-addresses/{id}")
    public ResponseEntity<Void> deleteCustomerAddress(@PathVariable Long id) {
        log.debug("REST request to delete CustomerAddress : {}", id);
        customerAddressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
