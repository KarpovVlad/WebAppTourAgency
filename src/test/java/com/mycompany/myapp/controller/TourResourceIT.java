package com.mycompany.myapp.controller;

import static com.mycompany.myapp.controller.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.entity.Tour;
import com.mycompany.myapp.repository.TourRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TourResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TourResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_PERSONS = 1;
    private static final Integer UPDATED_PERSONS = 2;

    private static final Boolean DEFAULT_HOT = false;
    private static final Boolean UPDATED_HOT = true;

    private static final Integer DEFAULT_DISCOINT = 1;
    private static final Integer UPDATED_DISCOINT = 2;

    private static final String ENTITY_API_URL = "/api/tours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TourRepository tourRepository;

    @Mock
    private TourRepository tourRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTourMockMvc;

    private Tour tour;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tour createEntity(EntityManager em) {
        Tour tour = new Tour()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .price(DEFAULT_PRICE)
            .persons(DEFAULT_PERSONS)
            .hot(DEFAULT_HOT)
            .discoint(DEFAULT_DISCOINT);
        return tour;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tour createUpdatedEntity(EntityManager em) {
        Tour tour = new Tour()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .price(UPDATED_PRICE)
            .persons(UPDATED_PERSONS)
            .hot(UPDATED_HOT)
            .discoint(UPDATED_DISCOINT);
        return tour;
    }

    @BeforeEach
    public void initTest() {
        tour = createEntity(em);
    }

    @Test
    @Transactional
    void createTour() throws Exception {
        int databaseSizeBeforeCreate = tourRepository.findAll().size();
        // Create the Tour
        restTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tour)))
            .andExpect(status().isCreated());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeCreate + 1);
        Tour testTour = tourList.get(tourList.size() - 1);
        assertThat(testTour.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTour.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTour.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testTour.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testTour.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testTour.getPersons()).isEqualTo(DEFAULT_PERSONS);
        assertThat(testTour.getHot()).isEqualTo(DEFAULT_HOT);
        assertThat(testTour.getDiscoint()).isEqualTo(DEFAULT_DISCOINT);
    }

    @Test
    @Transactional
    void createTourWithExistingId() throws Exception {
        // Create the Tour with an existing ID
        tour.setId(1L);

        int databaseSizeBeforeCreate = tourRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tour)))
            .andExpect(status().isBadRequest());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTours() throws Exception {
        // Initialize the database
        tourRepository.saveAndFlush(tour);

        // Get all the tourList
        restTourMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tour.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].persons").value(hasItem(DEFAULT_PERSONS)))
            .andExpect(jsonPath("$.[*].hot").value(hasItem(DEFAULT_HOT.booleanValue())))
            .andExpect(jsonPath("$.[*].discoint").value(hasItem(DEFAULT_DISCOINT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllToursWithEagerRelationshipsIsEnabled() throws Exception {
        when(tourRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTourMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tourRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllToursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tourRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTourMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tourRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTour() throws Exception {
        // Initialize the database
        tourRepository.saveAndFlush(tour);

        // Get the tour
        restTourMockMvc
            .perform(get(ENTITY_API_URL_ID, tour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tour.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.persons").value(DEFAULT_PERSONS))
            .andExpect(jsonPath("$.hot").value(DEFAULT_HOT.booleanValue()))
            .andExpect(jsonPath("$.discoint").value(DEFAULT_DISCOINT));
    }

    @Test
    @Transactional
    void getNonExistingTour() throws Exception {
        // Get the tour
        restTourMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTour() throws Exception {
        // Initialize the database
        tourRepository.saveAndFlush(tour);

        int databaseSizeBeforeUpdate = tourRepository.findAll().size();

        // Update the tour
        Tour updatedTour = tourRepository.findById(tour.getId()).get();
        // Disconnect from session so that the updates on updatedTour are not directly saved in db
        em.detach(updatedTour);
        updatedTour
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .price(UPDATED_PRICE)
            .persons(UPDATED_PERSONS)
            .hot(UPDATED_HOT)
            .discoint(UPDATED_DISCOINT);

        restTourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTour.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTour))
            )
            .andExpect(status().isOk());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeUpdate);
        Tour testTour = tourList.get(tourList.size() - 1);
        assertThat(testTour.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTour.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTour.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testTour.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testTour.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testTour.getPersons()).isEqualTo(UPDATED_PERSONS);
        assertThat(testTour.getHot()).isEqualTo(UPDATED_HOT);
        assertThat(testTour.getDiscoint()).isEqualTo(UPDATED_DISCOINT);
    }

    @Test
    @Transactional
    void putNonExistingTour() throws Exception {
        int databaseSizeBeforeUpdate = tourRepository.findAll().size();
        tour.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tour.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tour))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTour() throws Exception {
        int databaseSizeBeforeUpdate = tourRepository.findAll().size();
        tour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tour))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTour() throws Exception {
        int databaseSizeBeforeUpdate = tourRepository.findAll().size();
        tour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tour)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTourWithPatch() throws Exception {
        // Initialize the database
        tourRepository.saveAndFlush(tour);

        int databaseSizeBeforeUpdate = tourRepository.findAll().size();

        // Update the tour using partial update
        Tour partialUpdatedTour = new Tour();
        partialUpdatedTour.setId(tour.getId());

        partialUpdatedTour.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE).price(UPDATED_PRICE);

        restTourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTour.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTour))
            )
            .andExpect(status().isOk());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeUpdate);
        Tour testTour = tourList.get(tourList.size() - 1);
        assertThat(testTour.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTour.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTour.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testTour.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testTour.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testTour.getPersons()).isEqualTo(DEFAULT_PERSONS);
        assertThat(testTour.getHot()).isEqualTo(DEFAULT_HOT);
        assertThat(testTour.getDiscoint()).isEqualTo(DEFAULT_DISCOINT);
    }

    @Test
    @Transactional
    void fullUpdateTourWithPatch() throws Exception {
        // Initialize the database
        tourRepository.saveAndFlush(tour);

        int databaseSizeBeforeUpdate = tourRepository.findAll().size();

        // Update the tour using partial update
        Tour partialUpdatedTour = new Tour();
        partialUpdatedTour.setId(tour.getId());

        partialUpdatedTour
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .price(UPDATED_PRICE)
            .persons(UPDATED_PERSONS)
            .hot(UPDATED_HOT)
            .discoint(UPDATED_DISCOINT);

        restTourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTour.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTour))
            )
            .andExpect(status().isOk());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeUpdate);
        Tour testTour = tourList.get(tourList.size() - 1);
        assertThat(testTour.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTour.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTour.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testTour.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testTour.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testTour.getPersons()).isEqualTo(UPDATED_PERSONS);
        assertThat(testTour.getHot()).isEqualTo(UPDATED_HOT);
        assertThat(testTour.getDiscoint()).isEqualTo(UPDATED_DISCOINT);
    }

    @Test
    @Transactional
    void patchNonExistingTour() throws Exception {
        int databaseSizeBeforeUpdate = tourRepository.findAll().size();
        tour.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tour.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tour))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTour() throws Exception {
        int databaseSizeBeforeUpdate = tourRepository.findAll().size();
        tour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tour))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTour() throws Exception {
        int databaseSizeBeforeUpdate = tourRepository.findAll().size();
        tour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTourMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tour)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tour in the database
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTour() throws Exception {
        // Initialize the database
        tourRepository.saveAndFlush(tour);

        int databaseSizeBeforeDelete = tourRepository.findAll().size();

        // Delete the tour
        restTourMockMvc
            .perform(delete(ENTITY_API_URL_ID, tour.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tour> tourList = tourRepository.findAll();
        assertThat(tourList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
