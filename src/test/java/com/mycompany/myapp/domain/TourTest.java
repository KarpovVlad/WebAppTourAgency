package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TourTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tour.class);
        Tour tour1 = new Tour();
        tour1.setId(1L);
        Tour tour2 = new Tour();
        tour2.setId(tour1.getId());
        assertThat(tour1).isEqualTo(tour2);
        tour2.setId(2L);
        assertThat(tour1).isNotEqualTo(tour2);
        tour1.setId(null);
        assertThat(tour1).isNotEqualTo(tour2);
    }
}
