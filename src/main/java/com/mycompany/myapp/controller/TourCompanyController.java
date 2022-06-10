package com.mycompany.myapp.controller;

import com.mycompany.myapp.domain.dto.NameDto;
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

    @PostMapping("/tour-companies")
    public ResponseEntity<NameDto> createTourCompany(@RequestBody NameDto companyRequest) throws URISyntaxException {
        log.debug("REST request to save TourCompany : {}", companyRequest);
        NameDto response = companyService.create(companyRequest);
        return ResponseEntity
            .created(new URI("/api/tour-companies/" + response.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, response.getId().toString()))
            .body(response);
    }


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

    @GetMapping("/tour-companies")
    public List<NameDto> getAllTourCompanies() {
        log.debug("REST request to get all TourCompanies");
        return companyService.getAllNames();
    }

    @GetMapping("/tour-companies/{id}")
    public NameDto getTourCompany(@PathVariable Long id) {
        log.debug("REST request to get TourCompany : {}", id);
        return companyService.getNameById(id);
    }

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
