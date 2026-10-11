
package com.factorypulse.backend.controller;

import com.factorypulse.backend.model.SensorReading;
import com.factorypulse.backend.service.SensorReadingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor-readings")
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    public SensorReadingController(
            SensorReadingService sensorReadingService) {
        this.sensorReadingService = sensorReadingService;
    }

    @PostMapping
    public ResponseEntity<SensorReading> createSensorReading(
            @RequestBody SensorReading sensorReading) {
        SensorReading savedReading =
                sensorReadingService.createSensorReading(sensorReading);
        return ResponseEntity.ok(savedReading);
    }

    @GetMapping
    public ResponseEntity<List<SensorReading>> getAllSensorReadings() {
        return ResponseEntity.ok(
                sensorReadingService.getAllSensorReadings());
    }

    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<SensorReading>> getSensorReadingsByMachine(
            @PathVariable Long machineId) {
        return ResponseEntity.ok(
                sensorReadingService.getSensorReadingsByMachine(machineId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSensorReadingById(
            @PathVariable Long id) {
        return sensorReadingService.getSensorReadingById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSensorReading(
            @PathVariable Long id) {
        sensorReadingService.deleteSensorReading(id);
        return ResponseEntity.ok("Sensor reading deleted successfully");
    }
}