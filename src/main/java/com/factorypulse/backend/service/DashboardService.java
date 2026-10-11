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

    public DashboardService(
            MachineRepository machineRepository,
            AlertRepository alertRepository,
            SensorReadingRepository sensorReadingRepository) {

        this.machineRepository = machineRepository;
        this.alertRepository = alertRepository;
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public Map<String, Object> getDashboardSummary() {

        List<Machine> machines = machineRepository.findAll();

        long totalMachines = machines.size();

        long runningMachines = machines.stream()
                .filter(machine ->
                        "RUNNING".equalsIgnoreCase(machine.getStatus()))
                .count();

        long stoppedMachines = machines.stream()
                .filter(machine ->
                        "STOPPED".equalsIgnoreCase(machine.getStatus()))
                .count();

        long activeAlerts =
                alertRepository.findByResolved(false).size();

        List<SensorReading> sensorReadings =
                sensorReadingRepository.findAll();

        SensorReading latestReading = null;

        if (!sensorReadings.isEmpty()) {
            latestReading =
                    sensorReadings.get(sensorReadings.size() - 1);
        }

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("totalMachines", totalMachines);
        dashboard.put("runningMachines", runningMachines);
        dashboard.put("stoppedMachines", stoppedMachines);
        dashboard.put("activeAlerts", activeAlerts);
        dashboard.put("latestReading", latestReading);

        return dashboard;
    }
}