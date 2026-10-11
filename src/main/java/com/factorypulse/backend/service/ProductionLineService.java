package com.factorypulse.backend.service;

import com.factorypulse.backend.model.ProductionLine;
import com.factorypulse.backend.repository.ProductionLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionLineService {

    private final ProductionLineRepository productionLineRepository;

    public ProductionLineService(
            ProductionLineRepository productionLineRepository) {
        this.productionLineRepository = productionLineRepository;
    }

    public ProductionLine createProductionLine(
            ProductionLine productionLine) {

        return productionLineRepository.save(productionLine);
    }

    public List<ProductionLine> getAllProductionLines() {

        return productionLineRepository.findAll();
    }

    public Optional<ProductionLine> getProductionLineById(Long id) {

        return productionLineRepository.findById(id);
    }

    public void deleteProductionLine(Long id) {

        productionLineRepository.deleteById(id);
    }
}