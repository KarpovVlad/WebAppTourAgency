package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.entity.TourCompany;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface TourCompanyRepository extends JpaRepository<TourCompany, Long> {}
