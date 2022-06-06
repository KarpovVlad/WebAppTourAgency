package com.mycompany.myapp.controller;

import com.mycompany.myapp.domain.dto.NameDto;
import com.mycompany.myapp.domain.entity.TourCompany;
import com.mycompany.myapp.repository.TourCompanyRepository;
import com.mycompany.myapp.service.TourCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link TourCompany}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TourCompanyController {

    private final Logger log = LoggerFactory.getLogger(TourCompanyController.class);

    private static final String ENTITY_NAME = "tourCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TourCompanyRepository companyRepository;
    private final TourCompanyService companyService;

    public TourCompanyController(TourCompanyRepository companyRepository, TourCompanyService companyService) {
        this.companyRepository = companyRepository;
        this.companyService = companyService;
    }

    /**
     * {@code POST  /tour-companies} : Create a new tourCompany.
     *
     * @param companyRequest the tourCompany to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tourCompany, or with status {@code 400 (Bad Request)} if the tourCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tour-companies")
    public ResponseEntity<NameDto> createTourCompany(@RequestBody NameDto companyRequest) throws URISyntaxException {
        log.debug("REST request to save TourCompany : {}", companyRequest);
        NameDto response = companyService.create(companyRequest);
        return ResponseEntity
            .created(new URI("/api/tour-companies/" + response.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }

    /**
     * {@code PUT  /tour-companies/:id} : Updates an existing tourCompany.
     *
     * @param id             the id of the tourCompany to save.
     * @param companyRequest the tourCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tourCompany,
     * or with status {@code 400 (Bad Request)} if the tourCompany is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tourCompany couldn't be updated.
     */
    @PutMapping("/tour-companies/{id}")
    public ResponseEntity<NameDto> updateTourCompany(@PathVariable final Long id,
                                                     @RequestBody NameDto companyRequest) {
        log.debug("REST request to update TourCompany : {}, {}", id, companyRequest);
        NameDto response = companyService.update(id, companyRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .body(response);
    }

    /**
     * {@code PATCH  /tour-companies/:id} : Partial updates given fields of an existing tourCompany, field will ignore if it is null
     *
     * @param id          the id of the tourCompany to save.
     * @param tourCompany the tourCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tourCompany,
     * or with status {@code 400 (Bad Request)} if the tourCompany is not valid,
     * or with status {@code 404 (Not Found)} if the tourCompany is not found,
     * or with status {@code 500 (Internal Server Error)} if the tourCompany couldn't be updated.
     */
    @PatchMapping(value = "/tour-companies/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<NameDto> partialUpdateTourCompany(@PathVariable final Long id,
                                                            @RequestBody NameDto tourCompany) {
        log.debug("REST request to partial update TourCompany partially : {}, {}", id, tourCompany);
        NameDto response = companyService.update(id, tourCompany);
        return ResponseUtil.wrapOrNotFound(
            Optional.ofNullable(response),
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tourCompany.getId().toString())
        );
    }

    /**
     * {@code GET  /tour-companies} : get all the tourCompanies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tourCompanies in body.
     */
    @GetMapping("/tour-companies")
    public List<NameDto> getAllTourCompanies() {
        log.debug("REST request to get all TourCompanies");
        return companyService.getAllNames();
    }

    /**
     * {@code GET  /tour-companies/:id} : get the "id" tourCompany.
     *
     * @param id the id of the tourCompany to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tourCompany, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tour-companies/{id}")
    public NameDto getTourCompany(@PathVariable Long id) {
        log.debug("REST request to get TourCompany : {}", id);
        return companyService.getNameById(id);
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
        companyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
