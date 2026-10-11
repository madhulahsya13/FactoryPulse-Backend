package com.factorypulse.backend.controller;

import com.factorypulse.backend.model.Factory;
import com.factorypulse.backend.service.FactoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factories")
public class FactoryController {

    private final FactoryService factoryService;

    public FactoryController(FactoryService factoryService) {
        this.factoryService = factoryService;
    }

    @PostMapping
    public ResponseEntity<Factory> createFactory(
            @RequestBody Factory factory) {

        Factory savedFactory =
                factoryService.createFactory(factory);

        return ResponseEntity.ok(savedFactory);
    }

    @GetMapping
    public ResponseEntity<List<Factory>> getAllFactories() {

        return ResponseEntity.ok(
                factoryService.getAllFactories()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFactoryById(
            @PathVariable Long id) {

        return factoryService.getFactoryById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFactory(
            @PathVariable Long id) {

        factoryService.deleteFactory(id);

        return ResponseEntity.ok(
                "Factory deleted successfully"
        );
    }
}