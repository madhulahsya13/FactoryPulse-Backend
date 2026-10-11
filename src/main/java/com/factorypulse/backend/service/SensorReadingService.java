
package com.factorypulse.backend.service;

import com.factorypulse.backend.model.SensorReading;
import com.factorypulse.backend.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingService(
            SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public SensorReading createSensorReading(SensorReading sensorReading) {
        return sensorReadingRepository.save(sensorReading);
    }

    public List<SensorReading> getAllSensorReadings() {
        return sensorReadingRepository.findAllByOrderByTimestampDesc();
    }

    public List<SensorReading> getSensorReadingsByMachine(Long machineId) {
        return sensorReadingRepository
                .findByMachineIdOrderByTimestampDesc(machineId);
    }

    public Optional<SensorReading> getSensorReadingById(Long id) {
        return sensorReadingRepository.findById(id);
    }

    public void deleteSensorReading(Long id) {
        sensorReadingRepository.deleteById(id);
    }
}