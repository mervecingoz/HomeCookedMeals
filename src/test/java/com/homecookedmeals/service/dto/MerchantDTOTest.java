package com.homecookedmeals.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.homecookedmeals.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MerchantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MerchantDTO.class);
        MerchantDTO merchantDTO1 = new MerchantDTO();
        merchantDTO1.setId(1L);
        MerchantDTO merchantDTO2 = new MerchantDTO();
        assertThat(merchantDTO1).isNotEqualTo(merchantDTO2);
        merchantDTO2.setId(merchantDTO1.getId());
        assertThat(merchantDTO1).isEqualTo(merchantDTO2);
        merchantDTO2.setId(2L);
        assertThat(merchantDTO1).isNotEqualTo(merchantDTO2);
        merchantDTO1.setId(null);
        assertThat(merchantDTO1).isNotEqualTo(merchantDTO2);
    }
}
