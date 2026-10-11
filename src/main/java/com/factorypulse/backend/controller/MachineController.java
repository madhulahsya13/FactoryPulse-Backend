package com.factorypulse.backend.controller;

import com.factorypulse.backend.model.Machine;
import com.factorypulse.backend.service.MachineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @PostMapping
    public ResponseEntity<Machine> createMachine(
            @RequestBody Machine machine) {

        Machine savedMachine =
                machineService.createMachine(machine);

        return ResponseEntity.ok(savedMachine);
    }

    @GetMapping
    public ResponseEntity<List<Machine>> getAllMachines() {

        return ResponseEntity.ok(
                machineService.getAllMachines()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMachineById(
            @PathVariable Long id) {

        return machineService
                .getMachineById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMachine(
            @PathVariable Long id) {

        machineService.deleteMachine(id);

        return ResponseEntity.ok(
                "Machine deleted successfully"
        );
    }
}