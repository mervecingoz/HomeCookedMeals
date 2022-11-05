package com.homecookedmeals.service.impl;

import com.homecookedmeals.domain.CustomerAddress;
import com.homecookedmeals.repository.CustomerAddressRepository;
import com.homecookedmeals.service.CustomerAddressService;
import com.homecookedmeals.service.dto.CustomerAddressDTO;
import com.homecookedmeals.service.mapper.CustomerAddressMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerAddress}.
 */
@Service
@Transactional
public class CustomerAddressServiceImpl implements CustomerAddressService {

    private final Logger log = LoggerFactory.getLogger(CustomerAddressServiceImpl.class);

    private final CustomerAddressRepository customerAddressRepository;

    private final CustomerAddressMapper customerAddressMapper;

    public CustomerAddressServiceImpl(CustomerAddressRepository customerAddressRepository, CustomerAddressMapper customerAddressMapper) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerAddressMapper = customerAddressMapper;
    }

    @Override
    public CustomerAddressDTO save(CustomerAddressDTO customerAddressDTO) {
        log.debug("Request to save CustomerAddress : {}", customerAddressDTO);
        CustomerAddress customerAddress = customerAddressMapper.toEntity(customerAddressDTO);
        customerAddress = customerAddressRepository.save(customerAddress);
        return customerAddressMapper.toDto(customerAddress);
    }

    @Override
    public CustomerAddressDTO update(CustomerAddressDTO customerAddressDTO) {
        log.debug("Request to update CustomerAddress : {}", customerAddressDTO);
        CustomerAddress customerAddress = customerAddressMapper.toEntity(customerAddressDTO);
        customerAddress = customerAddressRepository.save(customerAddress);
        return customerAddressMapper.toDto(customerAddress);
    }

    @Override
    public Optional<CustomerAddressDTO> partialUpdate(CustomerAddressDTO customerAddressDTO) {
        log.debug("Request to partially update CustomerAddress : {}", customerAddressDTO);

        return customerAddressRepository
            .findById(customerAddressDTO.getId())
            .map(existingCustomerAddress -> {
                customerAddressMapper.partialUpdate(existingCustomerAddress, customerAddressDTO);

                return existingCustomerAddress;
            })
            .map(customerAddressRepository::save)
            .map(customerAddressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerAddressDTO> findAll() {
        log.debug("Request to get all CustomerAddresses");
        return customerAddressRepository
            .findAll()
            .stream()
            .map(customerAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerAddressDTO> findOne(Long id) {
        log.debug("Request to get CustomerAddress : {}", id);
        return customerAddressRepository.findById(id).map(customerAddressMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerAddress : {}", id);
        customerAddressRepository.deleteById(id);
    }
}
