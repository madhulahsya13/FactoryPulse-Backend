
package com.factorypulse.backend.repository;

import com.factorypulse.backend.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRepository
        extends JpaRepository<Maintenance, Long> {
}
