package com.factorypulse.backend.repository;

import com.factorypulse.backend.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    List<Machine> findByProductionLineFactoryOwnerEmail(String email);
}
