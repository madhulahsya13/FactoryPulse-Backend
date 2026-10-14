
package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Machine;
import com.factorypulse.backend.model.SensorReading;
import com.factorypulse.backend.repository.MachineRepository;
import com.factorypulse.backend.repository.SensorReadingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final MachineRepository machineRepository;
    private final AccessService accessService;

    public SensorReadingService(
            SensorReadingRepository sensorReadingRepository,
            MachineRepository machineRepository,
            AccessService accessService) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.machineRepository = machineRepository;
        this.accessService = accessService;
    }

    public SensorReading createSensorReading(SensorReading sensorReading) {

        if (sensorReading.getMachine() == null
                || sensorReading.getMachine().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Machine is required");
        }

        Machine machine = machineRepository
                .findById(sensorReading.getMachine().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Machine not found"));

        accessService.require(accessService.canAccessMachine(machine));

        sensorReading.setId(null);
        sensorReading.setMachine(machine);

        return sensorReadingRepository.save(sensorReading);
    }

    public List<SensorReading> getAllSensorReadings() {
        String email = accessService.ownerFilterEmail();
        return email == null
                ? sensorReadingRepository.findAllByOrderByTimestampDesc()
                : sensorReadingRepository
                .findByMachineProductionLineFactoryOwnerEmailOrderByTimestampDesc(email);
    }

    public List<SensorReading> getSensorReadingsByMachine(Long machineId) {
        boolean allowed = machineRepository.findById(machineId)
                .filter(m -> accessService.canAccessMachine(m))
                .isPresent();

        return allowed
                ? sensorReadingRepository.findByMachineIdOrderByTimestampDesc(machineId)
                : List.of();
    }

    public Optional<SensorReading> getSensorReadingById(Long id) {
        return sensorReadingRepository.findById(id)
                .filter(r -> accessService.canAccessMachine(r.getMachine()));
    }

    public void deleteSensorReading(Long id) {
        sensorReadingRepository.findById(id).ifPresent(reading -> {
            accessService.require(accessService.canAccessMachine(reading.getMachine()));
            sensorReadingRepository.delete(reading);
        });
    }
}
