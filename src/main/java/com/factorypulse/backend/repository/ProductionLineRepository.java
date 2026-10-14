package com.factorypulse.backend.repository;

import com.factorypulse.backend.model.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductionLineRepository
        extends JpaRepository<ProductionLine, Long> {

    List<ProductionLine> findByFactoryOwnerEmail(String email);
}
