package com.homecookedmeals.service.impl;

import com.homecookedmeals.domain.Meal;
import com.homecookedmeals.repository.MealRepository;
import com.homecookedmeals.service.MealService;
import com.homecookedmeals.service.dto.MealDTO;
import com.homecookedmeals.service.mapper.MealMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Meal}.
 */
@Service
@Transactional
public class MealServiceImpl implements MealService {

    private final Logger log = LoggerFactory.getLogger(MealServiceImpl.class);

    private final MealRepository mealRepository;

    private final MealMapper mealMapper;

    public MealServiceImpl(MealRepository mealRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
    }

    @Override
    public MealDTO save(MealDTO mealDTO) {
        log.debug("Request to save Meal : {}", mealDTO);
        Meal meal = mealMapper.toEntity(mealDTO);
        meal = mealRepository.save(meal);
        return mealMapper.toDto(meal);
    }

    @Override
    public MealDTO update(MealDTO mealDTO) {
        log.debug("Request to update Meal : {}", mealDTO);
        Meal meal = mealMapper.toEntity(mealDTO);
        meal = mealRepository.save(meal);
        return mealMapper.toDto(meal);
    }

    @Override
    public Optional<MealDTO> partialUpdate(MealDTO mealDTO) {
        log.debug("Request to partially update Meal : {}", mealDTO);

        return mealRepository
            .findById(mealDTO.getId())
            .map(existingMeal -> {
                mealMapper.partialUpdate(existingMeal, mealDTO);

                return existingMeal;
            })
            .map(mealRepository::save)
            .map(mealMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealDTO> findAll() {
        log.debug("Request to get all Meals");
        return mealRepository.findAll().stream().map(mealMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MealDTO> findOne(Long id) {
        log.debug("Request to get Meal : {}", id);
        return mealRepository.findById(id).map(mealMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Meal : {}", id);
        mealRepository.deleteById(id);
    }
}
