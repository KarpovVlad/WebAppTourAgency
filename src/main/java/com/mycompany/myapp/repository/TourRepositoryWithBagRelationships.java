package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tour;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TourRepositoryWithBagRelationships {
    Optional<Tour> fetchBagRelationships(Optional<Tour> tour);

    List<Tour> fetchBagRelationships(List<Tour> tours);

    Page<Tour> fetchBagRelationships(Page<Tour> tours);
}
