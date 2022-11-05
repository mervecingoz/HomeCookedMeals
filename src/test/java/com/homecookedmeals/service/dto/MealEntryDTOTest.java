package com.homecookedmeals.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.homecookedmeals.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MealEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MealEntryDTO.class);
        MealEntryDTO mealEntryDTO1 = new MealEntryDTO();
        mealEntryDTO1.setId(1L);
        MealEntryDTO mealEntryDTO2 = new MealEntryDTO();
        assertThat(mealEntryDTO1).isNotEqualTo(mealEntryDTO2);
        mealEntryDTO2.setId(mealEntryDTO1.getId());
        assertThat(mealEntryDTO1).isEqualTo(mealEntryDTO2);
        mealEntryDTO2.setId(2L);
        assertThat(mealEntryDTO1).isNotEqualTo(mealEntryDTO2);
        mealEntryDTO1.setId(null);
        assertThat(mealEntryDTO1).isNotEqualTo(mealEntryDTO2);
    }
}
