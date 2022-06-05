package com.mycompany.myapp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.entity.TourCompany;
import com.mycompany.myapp.repository.TourCompanyRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TourCompanyController} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TourCompanyControllerIT {

    private static final String DEFAULT_TOUR_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TOUR_COMPANY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tour-companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TourCompanyRepository tourCompanyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTourCompanyMockMvc;

    private TourCompany tourCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourCompany createEntity(EntityManager em) {
        TourCompany tourCompany = new TourCompany().tourCompanyName(DEFAULT_TOUR_COMPANY_NAME);
        return tourCompany;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TourCompany createUpdatedEntity(EntityManager em) {
        TourCompany tourCompany = new TourCompany().tourCompanyName(UPDATED_TOUR_COMPANY_NAME);
        return tourCompany;
    }

    @BeforeEach
    public void initTest() {
        tourCompany = createEntity(em);
    }

    @Test
    @Transactional
    void createTourCompany() throws Exception {
        int databaseSizeBeforeCreate = tourCompanyRepository.findAll().size();
        // Create the TourCompany
        restTourCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourCompany)))
            .andExpect(status().isCreated());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        TourCompany testTourCompany = tourCompanyList.get(tourCompanyList.size() - 1);
        assertThat(testTourCompany.getTourCompanyName()).isEqualTo(DEFAULT_TOUR_COMPANY_NAME);
    }

    @Test
    @Transactional
    void createTourCompanyWithExistingId() throws Exception {
        // Create the TourCompany with an existing ID
        tourCompany.setId(1L);

        int databaseSizeBeforeCreate = tourCompanyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTourCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourCompany)))
            .andExpect(status().isBadRequest());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTourCompanies() throws Exception {
        // Initialize the database
        tourCompanyRepository.saveAndFlush(tourCompany);

        // Get all the tourCompanyList
        restTourCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tourCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].tourCompanyName").value(hasItem(DEFAULT_TOUR_COMPANY_NAME)));
    }

    @Test
    @Transactional
    void getTourCompany() throws Exception {
        // Initialize the database
        tourCompanyRepository.saveAndFlush(tourCompany);

        // Get the tourCompany
        restTourCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, tourCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tourCompany.getId().intValue()))
            .andExpect(jsonPath("$.tourCompanyName").value(DEFAULT_TOUR_COMPANY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTourCompany() throws Exception {
        // Get the tourCompany
        restTourCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTourCompany() throws Exception {
        // Initialize the database
        tourCompanyRepository.saveAndFlush(tourCompany);

        int databaseSizeBeforeUpdate = tourCompanyRepository.findAll().size();

        // Update the tourCompany
        TourCompany updatedTourCompany = tourCompanyRepository.findById(tourCompany.getId()).get();
        // Disconnect from session so that the updates on updatedTourCompany are not directly saved in db
        em.detach(updatedTourCompany);
        updatedTourCompany.tourCompanyName(UPDATED_TOUR_COMPANY_NAME);

        restTourCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTourCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTourCompany))
            )
            .andExpect(status().isOk());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeUpdate);
        TourCompany testTourCompany = tourCompanyList.get(tourCompanyList.size() - 1);
        assertThat(testTourCompany.getTourCompanyName()).isEqualTo(UPDATED_TOUR_COMPANY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTourCompany() throws Exception {
        int databaseSizeBeforeUpdate = tourCompanyRepository.findAll().size();
        tourCompany.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTourCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tourCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tourCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTourCompany() throws Exception {
        int databaseSizeBeforeUpdate = tourCompanyRepository.findAll().size();
        tourCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tourCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTourCompany() throws Exception {
        int databaseSizeBeforeUpdate = tourCompanyRepository.findAll().size();
        tourCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourCompany)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTourCompanyWithPatch() throws Exception {
        // Initialize the database
        tourCompanyRepository.saveAndFlush(tourCompany);

        int databaseSizeBeforeUpdate = tourCompanyRepository.findAll().size();

        // Update the tourCompany using partial update
        TourCompany partialUpdatedTourCompany = new TourCompany();
        partialUpdatedTourCompany.setId(tourCompany.getId());

        restTourCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTourCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTourCompany))
            )
            .andExpect(status().isOk());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeUpdate);
        TourCompany testTourCompany = tourCompanyList.get(tourCompanyList.size() - 1);
        assertThat(testTourCompany.getTourCompanyName()).isEqualTo(DEFAULT_TOUR_COMPANY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTourCompanyWithPatch() throws Exception {
        // Initialize the database
        tourCompanyRepository.saveAndFlush(tourCompany);

        int databaseSizeBeforeUpdate = tourCompanyRepository.findAll().size();

        // Update the tourCompany using partial update
        TourCompany partialUpdatedTourCompany = new TourCompany();
        partialUpdatedTourCompany.setId(tourCompany.getId());

        partialUpdatedTourCompany.tourCompanyName(UPDATED_TOUR_COMPANY_NAME);

        restTourCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTourCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTourCompany))
            )
            .andExpect(status().isOk());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeUpdate);
        TourCompany testTourCompany = tourCompanyList.get(tourCompanyList.size() - 1);
        assertThat(testTourCompany.getTourCompanyName()).isEqualTo(UPDATED_TOUR_COMPANY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTourCompany() throws Exception {
        int databaseSizeBeforeUpdate = tourCompanyRepository.findAll().size();
        tourCompany.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTourCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tourCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tourCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTourCompany() throws Exception {
        int databaseSizeBeforeUpdate = tourCompanyRepository.findAll().size();
        tourCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tourCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTourCompany() throws Exception {
        int databaseSizeBeforeUpdate = tourCompanyRepository.findAll().size();
        tourCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tourCompany))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TourCompany in the database
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTourCompany() throws Exception {
        // Initialize the database
        tourCompanyRepository.saveAndFlush(tourCompany);

        int databaseSizeBeforeDelete = tourCompanyRepository.findAll().size();

        // Delete the tourCompany
        restTourCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, tourCompany.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TourCompany> tourCompanyList = tourCompanyRepository.findAll();
        assertThat(tourCompanyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
