
package com.factorypulse.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_readings")
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperature;
    private Double vibration;
    private Double pressure;
    private Double rpm;
    private Double powerConsumption;
    private Integer productionCount;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    public SensorReading() {
    }

    public SensorReading(
            Double temperature,
            Double vibration,
            Double pressure,
            Double rpm,
            Double powerConsumption,
            Integer productionCount,
            LocalDateTime timestamp,
            Machine machine) {

        this.temperature = temperature;
        this.vibration = vibration;
        this.pressure = pressure;
        this.rpm = rpm;
        this.powerConsumption = powerConsumption;
        this.productionCount = productionCount;
        this.timestamp = timestamp != null
                ? timestamp
                : LocalDateTime.now();
        this.machine = machine;
    }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getVibration() {
        return vibration;
    }

    public void setVibration(Double vibration) {
        this.vibration = vibration;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getRpm() {
        return rpm;
    }

    public void setRpm(Double rpm) {
        this.rpm = rpm;
    }

    public Double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(Double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public Integer getProductionCount() {
        return productionCount;
    }

    public void setProductionCount(Integer productionCount) {
        this.productionCount = productionCount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }
}
