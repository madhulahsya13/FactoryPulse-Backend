package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Alert;
import com.factorypulse.backend.model.Machine;
import com.factorypulse.backend.repository.AlertRepository;
import com.factorypulse.backend.repository.MachineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final MachineRepository machineRepository;
    private final AccessService accessService;

    public AlertService(AlertRepository alertRepository,
                        MachineRepository machineRepository,
                        AccessService accessService) {
        this.alertRepository = alertRepository;
        this.machineRepository = machineRepository;
        this.accessService = accessService;
    }

    public Alert createAlert(Alert alert) {

        if (alert.getMachine() == null || alert.getMachine().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Machine is required");
        }

        Machine machine = machineRepository
                .findById(alert.getMachine().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Machine not found"));

        accessService.require(accessService.canAccessMachine(machine));

        alert.setId(null);
        alert.setMachine(machine);

        return alertRepository.save(alert);
    }

    public List<Alert> getAllAlerts() {
        String email = accessService.ownerFilterEmail();
        return email == null
                ? alertRepository.findAll()
                : alertRepository.findByMachineProductionLineFactoryOwnerEmail(email);
    }

    public Optional<Alert> getAlertById(Long id) {
        return alertRepository.findById(id)
                .filter(a -> accessService.canAccessMachine(a.getMachine()));
    }

    public List<Alert> getAlertsByMachine(Long machineId) {
        boolean allowed = machineRepository.findById(machineId)
                .filter(m -> accessService.canAccessMachine(m))
                .isPresent();

        return allowed ? alertRepository.findByMachineId(machineId) : List.of();
    }

    public List<Alert> getUnresolvedAlerts() {
        String email = accessService.ownerFilterEmail();
        return email == null
                ? alertRepository.findByResolved(false)
                : alertRepository.findByResolvedAndMachineProductionLineFactoryOwnerEmail(
                        false, email);
    }

    public void deleteAlert(Long id) {
        alertRepository.findById(id).ifPresent(alert -> {
            accessService.require(accessService.canAccessMachine(alert.getMachine()));
            alertRepository.delete(alert);
        });
    }

    public Alert resolveAlert(Long id) {

        Optional<Alert> optionalAlert = getAlertById(id);

        if (optionalAlert.isPresent()) {

            Alert alert = optionalAlert.get();

            alert.setResolved(true);

            return alertRepository.save(alert);
        }

        return null;
    }
}
