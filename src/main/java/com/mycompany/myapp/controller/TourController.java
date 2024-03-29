package com.mycompany.myapp.controller;

import com.mycompany.myapp.domain.dto.TourCriteria;
import com.mycompany.myapp.domain.dto.TourDto;
import com.mycompany.myapp.domain.entity.Tour;
import com.mycompany.myapp.exceptions.BadRequestAlertException;
import com.mycompany.myapp.repository.TourRepository;
import com.mycompany.myapp.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
public class TourController {

    private final Logger log = LoggerFactory.getLogger(TourController.class);

    private static final String ENTITY_NAME = "tour";

    private final String applicationName;

    private final TourRepository tourRepository;

    private final TourService tourService;

    public TourController(@Value("${jhipster.clientApp.name}") String applicationName,
                          TourRepository tourRepository,
                          TourService tourService) {
        this.applicationName = applicationName;
        this.tourRepository = tourRepository;
        this.tourService = tourService;
    }

    @PostMapping("/tours")
    public ResponseEntity<Tour> createTour(@RequestBody Tour tour) throws URISyntaxException {
        log.debug("REST request to save Tour : {}", tour);
        if (tour.getId() != null) {
            throw new BadRequestAlertException("A new tour cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tour result = tourRepository.save(tour);
        return ResponseEntity
            .created(new URI("/api/tours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/tours/{id}")
    public ResponseEntity<Tour> updateTour(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tour tour) {
        log.debug("REST request to update Tour : {}, {}", id, tour);
        if (tour.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tour.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tourRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tour result = tourRepository.save(tour);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tour.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/tours/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<Tour> partialUpdateTour(@PathVariable final Long id, @RequestBody Tour tour) {
        log.debug("REST request to partial update Tour partially : {}, {}", id, tour);
        if (!Objects.equals(id, tour.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tourRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tour> result = tourRepository
            .findById(tour.getId())
            .map(existingTour -> {
                if (tour.getName() != null) {
                    existingTour.setName(tour.getName());
                }
                if (tour.getDescription() != null) {
                    existingTour.setDescription(tour.getDescription());
                }
                if (tour.getImage() != null) {
                    existingTour.setImage(tour.getImage());
                }
                if (tour.getImageContentType() != null) {
                    existingTour.setImageContentType(tour.getImageContentType());
                }
                if (tour.getPrice() != null) {
                    existingTour.setPrice(tour.getPrice());
                }
                if (tour.getPersons() != null) {
                    existingTour.setPersons(tour.getPersons());
                }
                if (tour.getHot() != null) {
                    existingTour.setHot(tour.getHot());
                }
                if (tour.getDiscoint() != null) {
                    existingTour.setDiscoint(tour.getDiscoint());
                }

                return existingTour;
            })
            .map(tourRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tour.getId().toString())
        );
    }

    @GetMapping(value = "/tours", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<TourDto> getAllTours(@RequestBody(required = false) TourCriteria criteria) {
        log.debug("REST request to get all Tours: priceFrom");
        if (Objects.isNull(criteria)) { // todo: remove then front send criteria object
            criteria = new TourCriteria();
        }
        return tourService.getAll(criteria);
    }

    @GetMapping("/tours/{id}")
    public TourDto getTour(@PathVariable Long id) {
        log.debug("REST request to get Tour : {}", id);
        return tourService.getDtoById(id);
    }

    @DeleteMapping("/tours/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        log.debug("REST request to delete Tour : {}", id);
        tourRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/tours/{id}/book")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void bookTour(@PathVariable Long id) {
        log.debug("Booking tour with id: {}", id);
        tourService.book(id);
    }
}
