package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Factory;
import com.factorypulse.backend.model.ProductionLine;
import com.factorypulse.backend.repository.FactoryRepository;
import com.factorypulse.backend.repository.ProductionLineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionLineService {

    private final ProductionLineRepository productionLineRepository;
    private final FactoryRepository factoryRepository;
    private final AccessService accessService;

    public ProductionLineService(
            ProductionLineRepository productionLineRepository,
            FactoryRepository factoryRepository,
            AccessService accessService) {
        this.productionLineRepository = productionLineRepository;
        this.factoryRepository = factoryRepository;
        this.accessService = accessService;
    }

    public ProductionLine createProductionLine(
            ProductionLine productionLine) {

        if (productionLine.getFactory() == null
                || productionLine.getFactory().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Factory is required");
        }

        Factory factory = factoryRepository
                .findById(productionLine.getFactory().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Factory not found"));

        accessService.require(accessService.canAccessFactory(factory));

        productionLine.setId(null);
        productionLine.setFactory(factory);

        return productionLineRepository.save(productionLine);
    }

    public List<ProductionLine> getAllProductionLines() {
        String email = accessService.ownerFilterEmail();
        return email == null
                ? productionLineRepository.findAll()
                : productionLineRepository.findByFactoryOwnerEmail(email);
    }

    public Optional<ProductionLine> getProductionLineById(Long id) {
        return productionLineRepository.findById(id)
                .filter(l -> accessService.canAccessLine(l));
    }

    public void deleteProductionLine(Long id) {
        productionLineRepository.findById(id).ifPresent(line -> {
            accessService.require(accessService.canAccessLine(line));
            productionLineRepository.delete(line);
        });
    }
}
