package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.entity.Category;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
