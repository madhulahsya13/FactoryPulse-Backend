package com.factorypulse.backend.controller;

import com.factorypulse.backend.model.ProductionLine;
import com.factorypulse.backend.service.ProductionLineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production-lines")
public class ProductionLineController {

    private final ProductionLineService productionLineService;

    public ProductionLineController(
            ProductionLineService productionLineService) {
        this.productionLineService = productionLineService;
    }

    @PostMapping
    public ResponseEntity<ProductionLine> createProductionLine(
            @RequestBody ProductionLine productionLine) {

        ProductionLine savedProductionLine =
                productionLineService.createProductionLine(
                        productionLine);

        return ResponseEntity.ok(savedProductionLine);
    }

    @GetMapping
    public ResponseEntity<List<ProductionLine>> getAllProductionLines() {

        return ResponseEntity.ok(
                productionLineService.getAllProductionLines()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductionLineById(
            @PathVariable Long id) {

        return productionLineService
                .getProductionLineById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductionLine(
            @PathVariable Long id) {

        productionLineService.deleteProductionLine(id);

        return ResponseEntity.ok(
                "Production line deleted successfully"
        );
    }
}