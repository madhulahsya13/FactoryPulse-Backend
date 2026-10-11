
package com.factorypulse.backend.service;

import com.factorypulse.backend.model.Machine;
import com.factorypulse.backend.model.SensorReading;
import org.springframework.stereotype.Service;

@Service
public class MachineHealthService {

    public int calculateHealthScore(SensorReading reading) {
        int score = 100;

        if (reading.getTemperature() != null) {
            if (reading.getTemperature() > 80) {
                score -= 30;
            } else if (reading.getTemperature() > 70) {
                score -= 15;
            }
        }

        if (reading.getVibration() != null) {
            if (reading.getVibration() > 6) {
                score -= 30;
            } else if (reading.getVibration() > 4) {
                score -= 15;
            }
        }

        if (reading.getPressure() != null
                && reading.getPressure() < 80) {
            score -= 15;
        }

        if (reading.getPowerConsumption() != null
                && reading.getPowerConsumption() > 10) {
            score -= 10;
        }

        return Math.max(0, score);
    }

    public String getHealthStatus(int score) {
        if (score >= 80) {
            return "HEALTHY";
        } else if (score >= 50) {
            return "WARNING";
        } else {
            return "CRITICAL";
        }
    }

    public String getRecommendation(SensorReading reading) {
        if (reading.getTemperature() != null
                && reading.getTemperature() > 80) {
            return "Inspect the machine cooling system.";
        }

        if (reading.getVibration() != null
                && reading.getVibration() > 6) {
            return "Inspect bearings and machine alignment.";
        }

        if (reading.getPressure() != null
                && reading.getPressure() < 80) {
            return "Inspect the pressure system.";
        }

        if (reading.getPowerConsumption() != null
                && reading.getPowerConsumption() > 10) {
            return "Investigate high power consumption.";
        }

        return "Continue monitoring the machine.";
    }
}
