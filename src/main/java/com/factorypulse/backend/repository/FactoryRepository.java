package com.factorypulse.backend.repository;

import com.factorypulse.backend.model.Factory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryRepository extends JpaRepository<Factory, Long> {
}