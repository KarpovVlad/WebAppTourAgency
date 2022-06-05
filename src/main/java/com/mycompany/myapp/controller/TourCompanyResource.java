package com.mycompany.myapp.controller;

import com.mycompany.myapp.domain.entity.TourCompany;
import com.mycompany.myapp.repository.TourCompanyRepository;
import com.mycompany.myapp.exceptions.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link TourCompany}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TourCompanyResource {

    private final Logger log = LoggerFactory.getLogger(TourCompanyResource.class);

    private static final String ENTITY_NAME = "tourCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TourCompanyRepository tourCompanyRepository;

    public TourCompanyResource(TourCompanyRepository tourCompanyRepository) {
        this.tourCompanyRepository = tourCompanyRepository;
    }

    /**
     * {@code POST  /tour-companies} : Create a new tourCompany.
     *
     * @param tourCompany the tourCompany to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tourCompany, or with status {@code 400 (Bad Request)} if the tourCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tour-companies")
    public ResponseEntity<TourCompany> createTourCompany(@RequestBody TourCompany tourCompany) throws URISyntaxException {
        log.debug("REST request to save TourCompany : {}", tourCompany);
        if (tourCompany.getId() != null) {
            throw new BadRequestAlertException("A new tourCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TourCompany result = tourCompanyRepository.save(tourCompany);
        return ResponseEntity
            .created(new URI("/api/tour-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tour-companies/:id} : Updates an existing tourCompany.
     *
     * @param id the id of the tourCompany to save.
     * @param tourCompany the tourCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tourCompany,
     * or with status {@code 400 (Bad Request)} if the tourCompany is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tourCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tour-companies/{id}")
    public ResponseEntity<TourCompany> updateTourCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TourCompany tourCompany
    ) throws URISyntaxException {
        log.debug("REST request to update TourCompany : {}, {}", id, tourCompany);
        if (tourCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tourCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tourCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TourCompany result = tourCompanyRepository.save(tourCompany);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tourCompany.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tour-companies/:id} : Partial updates given fields of an existing tourCompany, field will ignore if it is null
     *
     * @param id the id of the tourCompany to save.
     * @param tourCompany the tourCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tourCompany,
     * or with status {@code 400 (Bad Request)} if the tourCompany is not valid,
     * or with status {@code 404 (Not Found)} if the tourCompany is not found,
     * or with status {@code 500 (Internal Server Error)} if the tourCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tour-companies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TourCompany> partialUpdateTourCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TourCompany tourCompany
    ) throws URISyntaxException {
        log.debug("REST request to partial update TourCompany partially : {}, {}", id, tourCompany);
        if (tourCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tourCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tourCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TourCompany> result = tourCompanyRepository
            .findById(tourCompany.getId())
            .map(existingTourCompany -> {
                if (tourCompany.getTourCompanyName() != null) {
                    existingTourCompany.setTourCompanyName(tourCompany.getTourCompanyName());
                }

                return existingTourCompany;
            })
            .map(tourCompanyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tourCompany.getId().toString())
        );
    }

    /**
     * {@code GET  /tour-companies} : get all the tourCompanies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tourCompanies in body.
     */
    @GetMapping("/tour-companies")
    public List<TourCompany> getAllTourCompanies() {
        log.debug("REST request to get all TourCompanies");
        return tourCompanyRepository.findAll();
    }

    /**
     * {@code GET  /tour-companies/:id} : get the "id" tourCompany.
     *
     * @param id the id of the tourCompany to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tourCompany, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tour-companies/{id}")
    public ResponseEntity<TourCompany> getTourCompany(@PathVariable Long id) {
        log.debug("REST request to get TourCompany : {}", id);
        Optional<TourCompany> tourCompany = tourCompanyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tourCompany);
    }

    /**
     * {@code DELETE  /tour-companies/:id} : delete the "id" tourCompany.
     *
     * @param id the id of the tourCompany to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tour-companies/{id}")
    public ResponseEntity<Void> deleteTourCompany(@PathVariable Long id) {
        log.debug("REST request to delete TourCompany : {}", id);
        tourCompanyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
