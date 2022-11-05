package com.homecookedmeals.service.impl;

import com.homecookedmeals.domain.Merchant;
import com.homecookedmeals.repository.MerchantRepository;
import com.homecookedmeals.service.MerchantService;
import com.homecookedmeals.service.dto.MerchantDTO;
import com.homecookedmeals.service.mapper.MerchantMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Merchant}.
 */
@Service
@Transactional
public class MerchantServiceImpl implements MerchantService {

    private final Logger log = LoggerFactory.getLogger(MerchantServiceImpl.class);

    private final MerchantRepository merchantRepository;

    private final MerchantMapper merchantMapper;

    public MerchantServiceImpl(MerchantRepository merchantRepository, MerchantMapper merchantMapper) {
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
    }

    @Override
    public MerchantDTO save(MerchantDTO merchantDTO) {
        log.debug("Request to save Merchant : {}", merchantDTO);
        Merchant merchant = merchantMapper.toEntity(merchantDTO);
        merchant = merchantRepository.save(merchant);
        return merchantMapper.toDto(merchant);
    }

    @Override
    public MerchantDTO update(MerchantDTO merchantDTO) {
        log.debug("Request to update Merchant : {}", merchantDTO);
        Merchant merchant = merchantMapper.toEntity(merchantDTO);
        merchant = merchantRepository.save(merchant);
        return merchantMapper.toDto(merchant);
    }

    @Override
    public Optional<MerchantDTO> partialUpdate(MerchantDTO merchantDTO) {
        log.debug("Request to partially update Merchant : {}", merchantDTO);

        return merchantRepository
            .findById(merchantDTO.getId())
            .map(existingMerchant -> {
                merchantMapper.partialUpdate(existingMerchant, merchantDTO);

                return existingMerchant;
            })
            .map(merchantRepository::save)
            .map(merchantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantDTO> findAll() {
        log.debug("Request to get all Merchants");
        return merchantRepository.findAll().stream().map(merchantMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MerchantDTO> findOne(Long id) {
        log.debug("Request to get Merchant : {}", id);
        return merchantRepository.findById(id).map(merchantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Merchant : {}", id);
        merchantRepository.deleteById(id);
    }
}
