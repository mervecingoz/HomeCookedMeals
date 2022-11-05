package com.homecookedmeals.service.mapper;

import com.homecookedmeals.domain.Meal;
import com.homecookedmeals.service.dto.MealDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Meal} and its DTO {@link MealDTO}.
 */
@Mapper(componentModel = "spring")
public interface MealMapper extends EntityMapper<MealDTO, Meal> {}
