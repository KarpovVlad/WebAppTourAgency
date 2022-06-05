package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.domain.entity.TourCompany;
import com.mycompany.myapp.controller.TestUtil;
import org.junit.jupiter.api.Test;

class TourCompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TourCompany.class);
        TourCompany tourCompany1 = new TourCompany();
        tourCompany1.setId(1L);
        TourCompany tourCompany2 = new TourCompany();
        tourCompany2.setId(tourCompany1.getId());
        assertThat(tourCompany1).isEqualTo(tourCompany2);
        tourCompany2.setId(2L);
        assertThat(tourCompany1).isNotEqualTo(tourCompany2);
        tourCompany1.setId(null);
        assertThat(tourCompany1).isNotEqualTo(tourCompany2);
    }
}
