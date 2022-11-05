package com.homecookedmeals.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.homecookedmeals.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MealEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MealEntry.class);
        MealEntry mealEntry1 = new MealEntry();
        mealEntry1.setId(1L);
        MealEntry mealEntry2 = new MealEntry();
        mealEntry2.setId(mealEntry1.getId());
        assertThat(mealEntry1).isEqualTo(mealEntry2);
        mealEntry2.setId(2L);
        assertThat(mealEntry1).isNotEqualTo(mealEntry2);
        mealEntry1.setId(null);
        assertThat(mealEntry1).isNotEqualTo(mealEntry2);
    }
}
