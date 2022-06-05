package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.entity.Tour;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tour entity.
 */
@Repository
public interface TourRepository extends TourRepositoryWithBagRelationships, JpaRepository<Tour, Long> {
    default Optional<Tour> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Tour> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Tour> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct tour from Tour tour left join fetch tour.tourCompany",
        countQuery = "select count(distinct tour) from Tour tour"
    )
    Page<Tour> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct tour from Tour tour left join fetch tour.tourCompany")
    List<Tour> findAllWithToOneRelationships();

    @Query("select tour from Tour tour left join fetch tour.tourCompany where tour.id =:id")
    Optional<Tour> findOneWithToOneRelationships(@Param("id") Long id);
}
