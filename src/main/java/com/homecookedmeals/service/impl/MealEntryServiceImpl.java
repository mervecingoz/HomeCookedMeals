package com.homecookedmeals.service.impl;

import com.homecookedmeals.domain.MealEntry;
import com.homecookedmeals.repository.MealEntryRepository;
import com.homecookedmeals.service.MealEntryService;
import com.homecookedmeals.service.dto.MealEntryDTO;
import com.homecookedmeals.service.mapper.MealEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MealEntry}.
 */
@Service
@Transactional
public class MealEntryServiceImpl implements MealEntryService {

    private final Logger log = LoggerFactory.getLogger(MealEntryServiceImpl.class);

    private final MealEntryRepository mealEntryRepository;

    private final MealEntryMapper mealEntryMapper;

    public MealEntryServiceImpl(MealEntryRepository mealEntryRepository, MealEntryMapper mealEntryMapper) {
        this.mealEntryRepository = mealEntryRepository;
        this.mealEntryMapper = mealEntryMapper;
    }

    @Override
    public MealEntryDTO save(MealEntryDTO mealEntryDTO) {
        log.debug("Request to save MealEntry : {}", mealEntryDTO);
        MealEntry mealEntry = mealEntryMapper.toEntity(mealEntryDTO);
        mealEntry = mealEntryRepository.save(mealEntry);
        return mealEntryMapper.toDto(mealEntry);
    }

    @Override
    public MealEntryDTO update(MealEntryDTO mealEntryDTO) {
        log.debug("Request to update MealEntry : {}", mealEntryDTO);
        MealEntry mealEntry = mealEntryMapper.toEntity(mealEntryDTO);
        mealEntry = mealEntryRepository.save(mealEntry);
        return mealEntryMapper.toDto(mealEntry);
    }

    @Override
    public Optional<MealEntryDTO> partialUpdate(MealEntryDTO mealEntryDTO) {
        log.debug("Request to partially update MealEntry : {}", mealEntryDTO);

        return mealEntryRepository
            .findById(mealEntryDTO.getId())
            .map(existingMealEntry -> {
                mealEntryMapper.partialUpdate(existingMealEntry, mealEntryDTO);

                return existingMealEntry;
            })
            .map(mealEntryRepository::save)
            .map(mealEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MealEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MealEntries");
        return mealEntryRepository.findAll(pageable).map(mealEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MealEntryDTO> findOne(Long id) {
        log.debug("Request to get MealEntry : {}", id);
        return mealEntryRepository.findById(id).map(mealEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MealEntry : {}", id);
        mealEntryRepository.deleteById(id);
    }
}
