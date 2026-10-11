package com.factorypulse.backend.repository;

import com.factorypulse.backend.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByMachineId(Long machineId);

    List<Alert> findByResolved(Boolean resolved);
}