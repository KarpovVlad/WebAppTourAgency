package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {}
