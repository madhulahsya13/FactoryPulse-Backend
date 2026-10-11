package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Alert;
import com.factorypulse.backend.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Optional<Alert> getAlertById(Long id) {
        return alertRepository.findById(id);
    }

    public List<Alert> getAlertsByMachine(Long machineId) {
        return alertRepository.findByMachineId(machineId);
    }

    public List<Alert> getUnresolvedAlerts() {
        return alertRepository.findByResolved(false);
    }

    public void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }

    public Alert resolveAlert(Long id) {

        Optional<Alert> optionalAlert =
                alertRepository.findById(id);

        if (optionalAlert.isPresent()) {

            Alert alert = optionalAlert.get();

            alert.setResolved(true);

            return alertRepository.save(alert);
        }

        return null;
    }
}