
package com.factorypulse.backend.controller;

import com.factorypulse.backend.model.SensorReading;
import com.factorypulse.backend.repository.SensorReadingRepository;
import com.factorypulse.backend.service.MachineHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/machine-health")
public class MachineHealthController {

    private final SensorReadingRepository sensorReadingRepository;
    private final MachineHealthService machineHealthService;

    public MachineHealthController(
            SensorReadingRepository sensorReadingRepository,
            MachineHealthService machineHealthService) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.machineHealthService = machineHealthService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllMachineHealth() {

        List<SensorReading> readings =
                sensorReadingRepository.findAllByOrderByTimestampDesc();

        Map<Long, Map<String, Object>> machineHealth = new LinkedHashMap<>();

        for (SensorReading reading : readings) {
            if (reading.getMachine() == null
                    || reading.getMachine().getId() == null) {
                continue;
            }

            Long machineId = reading.getMachine().getId();

            // The readings are ordered newest first.
            // Keep only the latest reading for each machine.
            if (machineHealth.containsKey(machineId)) {
                continue;
            }

            int score =
                    machineHealthService.calculateHealthScore(reading);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("machineId", machineId);
            result.put("machineName", reading.getMachine().getName());
            result.put("healthScore", score);
            result.put("healthStatus",
                    machineHealthService.getHealthStatus(score));
            result.put("recommendation",
                    machineHealthService.getRecommendation(reading));
            result.put("temperature", reading.getTemperature());
            result.put("vibration", reading.getVibration());
            result.put("pressure", reading.getPressure());
            result.put("powerConsumption", reading.getPowerConsumption());
            result.put("lastUpdated", reading.getTimestamp());

            machineHealth.put(machineId, result);
        }

        return ResponseEntity.ok(
                List.copyOf(machineHealth.values()));
    }

    @GetMapping("/{machineId}")
    public ResponseEntity<Map<String, Object>> getMachineHealth(
            @PathVariable Long machineId) {

        List<SensorReading> readings =
                sensorReadingRepository
                        .findByMachineIdOrderByTimestampDesc(machineId);

        if (readings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SensorReading reading = readings.get(0);

        int score =
                machineHealthService.calculateHealthScore(reading);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("machineId", machineId);
        result.put("machineName", reading.getMachine().getName());
        result.put("healthScore", score);
        result.put("healthStatus",
                machineHealthService.getHealthStatus(score));
        result.put("recommendation",
                machineHealthService.getRecommendation(reading));
        result.put("temperature", reading.getTemperature());
        result.put("vibration", reading.getVibration());
        result.put("pressure", reading.getPressure());
        result.put("powerConsumption", reading.getPowerConsumption());
        result.put("lastUpdated", reading.getTimestamp());

        return ResponseEntity.ok(result);
    }
}
