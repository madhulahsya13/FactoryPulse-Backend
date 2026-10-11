package com.factorypulse.backend.repository;

import com.factorypulse.backend.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Long> {
}