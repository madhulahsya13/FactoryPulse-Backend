package com.factorypulse.backend.controller;

import com.factorypulse.backend.model.Alert;
import com.factorypulse.backend.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(
            @RequestBody Alert alert) {

        Alert savedAlert = alertService.createAlert(alert);

        return ResponseEntity.ok(savedAlert);
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {

        return ResponseEntity.ok(
                alertService.getAllAlerts()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAlertById(
            @PathVariable Long id) {

        return alertService
                .getAlertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<Alert>> getAlertsByMachine(
            @PathVariable Long machineId) {

        return ResponseEntity.ok(
                alertService.getAlertsByMachine(machineId)
        );
    }

    @GetMapping("/unresolved")
    public ResponseEntity<List<Alert>> getUnresolvedAlerts() {

        return ResponseEntity.ok(
                alertService.getUnresolvedAlerts()
        );
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<?> resolveAlert(
            @PathVariable Long id) {

        Alert resolvedAlert =
                alertService.resolveAlert(id);

        if (resolvedAlert == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resolvedAlert);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlert(
            @PathVariable Long id) {

        alertService.deleteAlert(id);

        return ResponseEntity.ok(
                "Alert deleted successfully"
        );
    }
}