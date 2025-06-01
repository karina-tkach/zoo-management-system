package org.university.zoomanagementsystem.animal;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.animal.enums.AnimalGender;
import org.university.zoomanagementsystem.animal.enums.AnimalGroup;
import org.university.zoomanagementsystem.animal.enums.HealthStatus;
import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.enclosure.HabitatType;
import org.university.zoomanagementsystem.exception.validation.AnimalValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@SuppressWarnings({"java:S1192", "java:S5998"})
public class AnimalValidator {
    public void validate(Animal animal) {
        validateAnimalIsNotNull(animal);
        validateName(animal.getName());
        validateSpecies(animal.getSpecies());
        validateAnimalGroup(animal.getAnimalGroup());
        validateHabitatType(animal.getHabitatType());
        validateAnimalGender(animal.getGender());
        validateAnimalDate(animal.getBirthDate());
        validateEnclosure(animal.getEnclosure());
        validateHealthStatus(animal.getHealthStatus());
        validateImage(animal.getImage());
        validateAnimalInteractions(animal.getLastCheckedUpAt());
        validateAnimalInteractions(animal.getLastFedUpAt());
    }

    public void validateAnimalForUpdate(Animal animalToUpdate, Animal animal) {
        if(animal.getName() == null) {
            animal.setName(animalToUpdate.getName());
        }
        if(animal.getSpecies() == null) {
            animal.setSpecies(animalToUpdate.getSpecies());
        }
        if (animal.getAnimalGroup() == null) {
            animal.setAnimalGroup(animalToUpdate.getAnimalGroup());
        }
        if (animal.getHabitatType() == null) {
            animal.setHabitatType(animalToUpdate.getHabitatType());
        }
        if (animal.getGender() == null) {
            animal.setGender(animalToUpdate.getGender());
        }
        if (animal.getBirthDate() == null) {
            animal.setBirthDate(animalToUpdate.getBirthDate());
        }
        if (animal.getEnclosure() == null) {
            animal.setEnclosure(animalToUpdate.getEnclosure());
        }
        if (animal.getHealthStatus() == null) {
            animal.setHealthStatus(animalToUpdate.getHealthStatus());
        }
        if (animal.getImage() == null) {
            animal.setImage(animalToUpdate.getImage());
        }
        if (animal.getLastCheckedUpAt() == null) {
            animal.setLastCheckedUpAt(animalToUpdate.getLastCheckedUpAt());
        }
        if (animal.getLastFedUpAt() == null) {
            animal.setLastFedUpAt(animalToUpdate.getLastFedUpAt());
        }

        validate(animal);
    }

    private void validateAnimalIsNotNull(Animal animal) {
        if (animal == null) {
            throw new AnimalValidationException("Animal was null");
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new AnimalValidationException("Animal name was null");
        }
        if (name.isBlank()) {
            throw new AnimalValidationException("Animal name was empty");
        }
        if (name.length() > 100 || name.length() < 2) {
            throw new AnimalValidationException("Animal name had wrong length (must be 2 to 100 characters)");
        }
    }

    private void validateSpecies(String species) {
        if (species == null) {
            throw new AnimalValidationException("Animal species was null");
        }
        if (species.isBlank()) {
            throw new AnimalValidationException("Animal species was empty");
        }
        if (species.length() > 255 || species.length() < 2) {
            throw new AnimalValidationException("Animal species had wrong length (must be 2 to 255 characters)");
        }

    }

    private void validateAnimalGroup(AnimalGroup animalGroup) {
        if (animalGroup == null) {
            throw new AnimalValidationException("Animal group was null");
        }
    }

    private void validateHabitatType(HabitatType habitatType) {
        if (habitatType == null) {
            throw new AnimalValidationException("Animal habitat type was null");
        }
    }

    private void validateAnimalGender(AnimalGender gender) {
        if (gender == null) {
            throw new AnimalValidationException("Animal gender was null");
        }
    }

    private void validateAnimalDate(LocalDate date) {
        if(date == null) {
            throw new AnimalValidationException("Animal date of birth was null");
        }
        else if(date.isAfter(LocalDate.now())) {
            throw new AnimalValidationException("Animal date of birth can't be in the future");
        }
    }

    private void validateEnclosure(Enclosure enclosure) {
        if (enclosure == null) {
            throw new AnimalValidationException("Animal enclosure was null");
        }
        else if (enclosure.getId() < 0) {
            throw new AnimalValidationException("Animal enclosure id was negative");
        }
    }

    private void validateHealthStatus(HealthStatus healthStatus) {
        if (healthStatus == null) {
            throw new AnimalValidationException("Animal health status was null");
        }
    }

    private void validateImage(String image) {
        if (image == null) {
            throw new AnimalValidationException("Animal image was null");
        }
        if (image.isBlank()) {
            throw new AnimalValidationException("Animal image was empty");
        }
        if (image.length() > 255) {
            throw new AnimalValidationException("Animal image length must be less than or equal to 255");
        }
        if (!image.matches("[a-zA-Z0-9._()-]+\\.(jpg|jpeg|png|gif)$")) {
            throw new AnimalValidationException("Animal image must be a valid image file (jpg, jpeg, png, gif)");
        }
    }

    private void validateAnimalInteractions(LocalDateTime dateTime) {
        if(dateTime == null) {
            throw new AnimalValidationException("Animal last check or feeding time was null");
        }
        else if(dateTime.isAfter(LocalDateTime.now())) {
            throw new AnimalValidationException("Animal last check or feeding time can't be in the future");
        }
    }
}
