package org.university.zoomanagementsystem.enclosure;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.exception.validation.EnclosureValidationException;


@Component
@SuppressWarnings({"java:S1192", "java:S5998"})
public class EnclosureValidator {
    public void validate(Enclosure enclosure) {
        validateEnclosureIsNotNull(enclosure);
        validateName(enclosure.getName());
        validateLocation(enclosure.getLocation());
        validateEnvironmentType(enclosure.getEnvironmentType());
        validateArea(enclosure.getAreaM2());
    }

    public void validateEnclosureForUpdate(Enclosure enclosureToUpdate, Enclosure enclosure) {
        if(enclosure.getName() == null) {
            enclosure.setName(enclosureToUpdate.getName());
        }
        if(enclosure.getLocation() == null) {
            enclosure.setLocation(enclosureToUpdate.getLocation());
        }
        if (enclosure.getEnvironmentType() == null) {
            enclosure.setEnvironmentType(enclosureToUpdate.getEnvironmentType());
        }

        validate(enclosure);
    }

    private void validateEnclosureIsNotNull(Enclosure enclosure) {
        if (enclosure == null) {
            throw new EnclosureValidationException("Enclosure was null");
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new EnclosureValidationException("Enclosure name was null");
        }
        if (name.isBlank()) {
            throw new EnclosureValidationException("Enclosure name was empty");
        }
        if (name.length() > 100 || name.length() < 2) {
            throw new EnclosureValidationException("Enclosure name had wrong length (must be 2 to 100 characters)");
        }
    }

    private void validateLocation(String location) {
        if (location == null) {
            throw new EnclosureValidationException("Enclosure location was null");
        }
        if (location.isBlank()) {
            throw new EnclosureValidationException("Enclosure location was empty");
        }
        if (location.length() > 200 || location.length() < 2) {
            throw new EnclosureValidationException("Enclosure location had wrong length (must be 2 to 200 characters)");
        }

    }

    private void validateEnvironmentType(HabitatType habitatType) {
        if (habitatType == null) {
            throw new EnclosureValidationException("Enclosure environment type was null");
        }
    }

    private void validateArea(int areaM2) {
        if (areaM2 <= 0) {
            throw new EnclosureValidationException("Enclosure area must be positive");
        }
        if (areaM2 > 10000) {
            throw new EnclosureValidationException("Enclosure area must be less than 10000 m2");
        }
    }
}
