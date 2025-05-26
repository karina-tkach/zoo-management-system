package org.university.zoomanagementsystem.gate;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.exception.validation.GateValidationException;

@Component
public class GateValidator {
    public void validate(Gate gate) {
        validateGateNotNull(gate);
        validateName(gate.getName());
        validateLocation(gate.getLocation());
    }

    public void validateGateForUpdate(Gate gateToUpdate, Gate updatedGate) {
        if (updatedGate.getName() == null) {
            updatedGate.setName(gateToUpdate.getName());
        }
        if (updatedGate.getLocation() == null) {
            updatedGate.setLocation(gateToUpdate.getLocation());
        }

        validate(updatedGate);
    }

    private void validateGateNotNull(Gate gate) {
        if (gate == null) {
            throw new GateValidationException("Gate was null");
        }
    }

    private void validateName(String name) {
       if (name == null || name.isBlank()) {
           throw new GateValidationException("Gate name was null or empty");
       }
       else if (name.length() > 100) {
           throw new GateValidationException("Gate name length must be less than 100 characters");
       }
    }

    private void validateLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new GateValidationException("Gate location was null or empty");
        }
        else if(location.length() > 150) {
            throw new GateValidationException("Gate location length must be less than 150 characters");
        }
    }
}
