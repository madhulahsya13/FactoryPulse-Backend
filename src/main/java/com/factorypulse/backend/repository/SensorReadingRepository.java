
package com.factorypulse.backend.repository;

import com.factorypulse.backend.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorReadingRepository
        extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findByMachineIdOrderByTimestampDesc(Long machineId);

    List<SensorReading> findAllByOrderByTimestampDesc();

    List<SensorReading> findByMachineProductionLineFactoryOwnerEmailOrderByTimestampDesc(
            String email);

    Optional<SensorReading> findFirstByOrderByTimestampDesc();

    Optional<SensorReading> findFirstByMachineProductionLineFactoryOwnerEmailOrderByTimestampDesc(
            String email);
}
