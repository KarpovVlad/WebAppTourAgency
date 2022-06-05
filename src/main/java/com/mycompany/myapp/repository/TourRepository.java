package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.dto.TourCriteria;
import com.mycompany.myapp.domain.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Spring Data SQL repository for the Tour entity.
 */
@Repository
public interface TourRepository extends TourRepositoryWithBagRelationships, JpaRepository<Tour, Long> {

    default Page<Tour> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct tour from Tour tour left join fetch tour.tourCompany",
        countQuery = "select count(distinct tour) from Tour tour"
    )
    Page<Tour> findAllWithToOneRelationships(Pageable pageable);

    @Query("select tour from Tour tour left join fetch tour.tourCompany where tour.id =:id")
    Optional<Tour> findOneWithToOneRelationships(@Param("id") Long id);
}
