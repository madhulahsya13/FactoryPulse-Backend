package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Machine;
import com.factorypulse.backend.model.SensorReading;
import com.factorypulse.backend.repository.AlertRepository;
import com.factorypulse.backend.repository.MachineRepository;
import com.factorypulse.backend.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final MachineRepository machineRepository;
    private final AlertRepository alertRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final AccessService accessService;

    public DashboardService(
            MachineRepository machineRepository,
            AlertRepository alertRepository,
            SensorReadingRepository sensorReadingRepository,
            AccessService accessService) {

        this.machineRepository = machineRepository;
        this.alertRepository = alertRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.accessService = accessService;
    }

    public Map<String, Object> getDashboardSummary() {

        // null = ADMIN (everything). Otherwise only this owner's data.
        String email = accessService.ownerFilterEmail();

        List<Machine> machines = email == null
                ? machineRepository.findAll()
                : machineRepository.findByProductionLineFactoryOwnerEmail(email);

        long totalMachines = machines.size();

        long runningMachines = machines.stream()
                .filter(machine ->
                        "RUNNING".equalsIgnoreCase(machine.getStatus()))
                .count();

        long stoppedMachines = machines.stream()
                .filter(machine ->
                        "STOPPED".equalsIgnoreCase(machine.getStatus()))
                .count();

        long activeAlerts = email == null
                ? alertRepository.findByResolved(false).size()
                : alertRepository
                .findByResolvedAndMachineProductionLineFactoryOwnerEmail(false, email)
                .size();

        SensorReading latestReading = (email == null
                ? sensorReadingRepository.findFirstByOrderByTimestampDesc()
                : sensorReadingRepository
                .findFirstByMachineProductionLineFactoryOwnerEmailOrderByTimestampDesc(email))
                .orElse(null);

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("totalMachines", totalMachines);
        dashboard.put("runningMachines", runningMachines);
        dashboard.put("stoppedMachines", stoppedMachines);
        dashboard.put("activeAlerts", activeAlerts);
        dashboard.put("latestReading", latestReading);

        return dashboard;
    }
}
