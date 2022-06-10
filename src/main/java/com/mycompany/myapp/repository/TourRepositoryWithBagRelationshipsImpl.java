package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.entity.Tour;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class TourRepositoryWithBagRelationshipsImpl implements TourRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tour> fetchBagRelationships(Optional<Tour> tour) {
        return tour.map(this::fetchCategories);
    }

    @Override
    public Page<Tour> fetchBagRelationships(Page<Tour> tours) {
        return new PageImpl<>(fetchBagRelationships(tours.getContent()), tours.getPageable(), tours.getTotalElements());
    }

    @Override
    public List<Tour> fetchBagRelationships(List<Tour> tours) {
        return Optional.of(tours).map(this::fetchCategories).orElse(Collections.emptyList());
    }

    Tour fetchCategories(Tour result) {
        return entityManager
            .createQuery("select tour from Tour tour left join fetch tour.categories where tour is :tour", Tour.class)
            .setParameter("tour", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Tour> fetchCategories(List<Tour> tours) {
        return entityManager
            .createQuery("select distinct tour from Tour tour left join fetch tour.categories where tour in :tours", Tour.class)
            .setParameter("tours", tours)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
