package com.homecookedmeals.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MealEntryMapperTest {

    private MealEntryMapper mealEntryMapper;

    @BeforeEach
    public void setUp() {
        mealEntryMapper = new MealEntryMapperImpl();
    }
}
