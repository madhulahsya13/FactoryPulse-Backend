package com.factorypulse.backend.repository;

import com.factorypulse.backend.model.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionLineRepository
        extends JpaRepository<ProductionLine, Long> {
}