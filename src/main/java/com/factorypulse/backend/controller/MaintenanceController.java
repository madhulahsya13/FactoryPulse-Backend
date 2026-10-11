
package com.factorypulse.backend.controller;

import com.factorypulse.backend.model.Machine;
import com.factorypulse.backend.model.Maintenance;
import com.factorypulse.backend.repository.MachineRepository;
import com.factorypulse.backend.repository.MaintenanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceRepository maintenanceRepository;
    private final MachineRepository machineRepository;

    public MaintenanceController(
            MaintenanceRepository maintenanceRepository,
            MachineRepository machineRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.machineRepository = machineRepository;
    }

    @GetMapping
    public List<Maintenance> getAllMaintenance() {
        return maintenanceRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Maintenance createMaintenance(
            @RequestBody MaintenanceRequest request) {

        Machine machine = machineRepository.findById(request.machineId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Machine not found"));

        Maintenance maintenance = new Maintenance(
                machine,
                request.maintenanceType(),
                request.description(),
                request.scheduledDate(),
                request.status()
        );

        return maintenanceRepository.save(maintenance);
    }

    @PutMapping("/{id}")
    public Maintenance updateMaintenance(
            @PathVariable Long id,
            @RequestBody MaintenanceRequest request) {

        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Maintenance record not found"));

        Machine machine = machineRepository.findById(request.machineId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Machine not found"));

        maintenance.setMachine(machine);
        maintenance.setMaintenanceType(request.maintenanceType());
        maintenance.setDescription(request.description());
        maintenance.setScheduledDate(request.scheduledDate());
        maintenance.setStatus(request.status());

        return maintenanceRepository.save(maintenance);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMaintenance(@PathVariable Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Maintenance record not found");
        }

        maintenanceRepository.deleteById(id);
    }

    public record MaintenanceRequest(
            Long machineId,
            String maintenanceType,
            String description,
            java.time.LocalDate scheduledDate,
            String status
    ) {
    }
}
