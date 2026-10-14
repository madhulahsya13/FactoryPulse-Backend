package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Machine;
import com.factorypulse.backend.model.ProductionLine;
import com.factorypulse.backend.repository.MachineRepository;
import com.factorypulse.backend.repository.ProductionLineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final ProductionLineRepository productionLineRepository;
    private final AccessService accessService;

    public MachineService(MachineRepository machineRepository,
                          ProductionLineRepository productionLineRepository,
                          AccessService accessService) {
        this.machineRepository = machineRepository;
        this.productionLineRepository = productionLineRepository;
        this.accessService = accessService;
    }

    public Machine createMachine(Machine machine) {

        if (machine.getProductionLine() == null
                || machine.getProductionLine().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Production line is required");
        }

        ProductionLine line = productionLineRepository
                .findById(machine.getProductionLine().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Production line not found"));

        accessService.require(accessService.canAccessLine(line));

        machine.setId(null);
        machine.setProductionLine(line);

        return machineRepository.save(machine);
    }

    public List<Machine> getAllMachines() {
        String email = accessService.ownerFilterEmail();
        return email == null
                ? machineRepository.findAll()
                : machineRepository.findByProductionLineFactoryOwnerEmail(email);
    }

    public Optional<Machine> getMachineById(Long id) {
        return machineRepository.findById(id)
                .filter(m -> accessService.canAccessMachine(m));
    }

    public void deleteMachine(Long id) {
        machineRepository.findById(id).ifPresent(machine -> {
            accessService.require(accessService.canAccessMachine(machine));
            machineRepository.delete(machine);
        });
    }
}
