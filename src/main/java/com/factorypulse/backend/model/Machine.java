package com.factorypulse.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String machineType;

    private String status;

    private Double operatingHours;

    @ManyToOne
    @JoinColumn(name = "production_line_id", nullable = false)
    private ProductionLine productionLine;

    public Machine() {
    }

    public Machine(String name,
                   String machineType,
                   String status,
                   Double operatingHours,
                   ProductionLine productionLine) {
        this.name = name;
        this.machineType = machineType;
        this.status = status;
        this.operatingHours = operatingHours;
        this.productionLine = productionLine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(Double operatingHours) {
        this.operatingHours = operatingHours;
    }

    public ProductionLine getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(ProductionLine productionLine) {
        this.productionLine = productionLine;
    }
}