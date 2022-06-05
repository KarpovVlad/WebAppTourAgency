package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.entity.TourCompany;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TourCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TourCompanyRepository extends JpaRepository<TourCompany, Long> {}
