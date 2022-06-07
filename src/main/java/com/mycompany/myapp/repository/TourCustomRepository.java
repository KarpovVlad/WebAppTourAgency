package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.dto.TourCriteria;
import com.mycompany.myapp.domain.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TourCustomRepository extends JpaRepository<Tour, Long> {

    @Query(value = "select tour from Tour tour " +
                   "left join tour.categories category " +
                   "where (:#{#criteria.getIsExcursion} is null or category.excursion = :#{#criteria.getIsExcursion}) " +
                   "and (:#{#criteria.getIsRelax} is null or category.relax = :#{#criteria.getIsRelax}) " +
                   "and (:#{#criteria.getIsShopping} is null or category.shopping = :#{#criteria.getIsShopping}) " +
                   "and (:#{#criteria.getPriceFrom} is null or tour.price >= :#{#criteria.getPriceFrom}) " +
                   "and (:#{#criteria.getPriceTo} is null or tour.price <= :#{#criteria.getPriceTo}) " +
                   "and (:#{#criteria.getPersonsFrom} is null or tour.persons >= :#{#criteria.getPersonsFrom}) " +
                   "and (:#{#criteria.getPersonsTo} is null or tour.persons <= :#{#criteria.getPersonsTo}) " +
                   "order by tour.hot desc, tour.name")
    Set<Tour> findByCriteria(@Param("criteria") TourCriteria criteria);

}
