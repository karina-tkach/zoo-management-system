package org.university.zoomanagementsystem.animal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.animal.AnimalValidator;
import org.university.zoomanagementsystem.animal.enums.AnimalGroup;
import org.university.zoomanagementsystem.animal.enums.HealthStatus;
import org.university.zoomanagementsystem.animal.repository.AnimalRepository;
import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.enclosure.HabitatType;
import org.university.zoomanagementsystem.enclosure.service.EnclosureService;
import org.university.zoomanagementsystem.exception.not_found.AnimalNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.EnclosureNotFoundException;
import org.university.zoomanagementsystem.exception.validation.AnimalValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnimalService {
    private final AnimalValidator animalValidator;
    private final AnimalRepository animalRepository;
    private final EnclosureService enclosureService;

    private final Logger logger = LoggerFactory.getLogger(AnimalService.class);

    public AnimalService(AnimalValidator animalValidator, AnimalRepository animalRepository, EnclosureService enclosureService) {
        this.animalValidator = animalValidator;
        this.animalRepository = animalRepository;
        this.enclosureService = enclosureService;
    }

    public Animal addAnimal(Animal animal) {
        try {
            logger.info("Try to add animal");
            animal.setHealthStatus(HealthStatus.HEALTHY);
            animal.setLastCheckedUpAt(LocalDateTime.now());
            animal.setLastFedUpAt(LocalDateTime.now());

            animalValidator.validate(animal);

            Enclosure enclosure = enclosureService.getEnclosureById(animal.getEnclosure().getId());
            if (!enclosure.getEnvironmentType().equals(animal.getHabitatType())) {
                throw new AnimalValidationException("Cannot add animal to enclosure with different environment type");
            }

            int id = animalRepository.addAnimal(animal);
            if (id == -1) {
                throw new AnimalValidationException("Unable to retrieve the generated key");
            }

            animal.setId(id);
            logger.info("Animal was added:\n{}", animal);
            return getAnimalById(id);
        } catch (AnimalValidationException | AnimalNotFoundException |
                 EnclosureNotFoundException | DataAccessException exception) {
            logger.warn("Animal wasn't added: {}\n{}", animal, exception.getMessage());
            throw exception;
        }
    }

    public Animal getAnimalById(int id) {
        try {
            logger.info("Try to get animal by id");
            Animal animal = animalRepository.getAnimalById(id);
            if (animal == null) {
                throw new AnimalNotFoundException(String.format("Animal with id %d was not found", id));
            }
            logger.info("Animal was fetched successfully");
            return animal;
        } catch (AnimalNotFoundException | DataAccessException exception) {
            logger.warn("Animal wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public Animal updateAnimalById(Animal animal, int id) {
        try {
            Animal animalToUpdate = getAnimalById(id);
            logger.info("Try to update animal");

            animalValidator.validateAnimalForUpdate(animalToUpdate, animal);
            Enclosure enclosure = enclosureService.getEnclosureById(animal.getEnclosure().getId());
            if (!enclosure.getEnvironmentType().equals(animal.getHabitatType())) {
                throw new AnimalValidationException("Cannot add animal to enclosure with different environment type");
            }

            animalRepository.updateAnimalById(animal, id);

            logger.info("Animal was updated:\n{}", animal);
            return getAnimalById(id);
        } catch (AnimalNotFoundException | AnimalValidationException |
                 EnclosureNotFoundException | DataAccessException exception) {
            logger.warn("Animal wasn't updated: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public boolean deleteAnimalById(int id) {
        try {
            Animal animal = getAnimalById(id);
            logger.info("Try to delete animal by id");
            animalRepository.deleteAnimalById(id);
            logger.info("Animal was deleted:\n{}", animal);
            return true;
        } catch (AnimalNotFoundException | DataAccessException exception) {
            logger.warn("Animal wasn't deleted: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public List<Animal> getAnimalsWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get animals with pagination");
            List<Animal> animals = animalRepository.getAnimalsWithPagination(pageNumber, limit);
            logger.info("Animals were fetched with pagination successfully");
            return animals;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Animals weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getAnimalsRowsCount() {
        try {
            logger.info("Try to get animals rows count");
            int count = animalRepository.getAnimalsRowsCount();
            logger.info("Animals rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Animals rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<Animal> getAnimalsByEnclosureWithPagination(int enclosureId, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get animals by enclosure with pagination");
            List<Animal> animals = animalRepository.getAnimalsByEnclosureWithPagination(enclosureId, pageNumber, limit);
            logger.info("Animals by enclosure were fetched with pagination successfully");
            return animals;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Animals by enclosure weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getAnimalsByEnclosureRowsCount(int enclosureId) {
        try {
            logger.info("Try to get animals by enclosure rows count");
            int count = animalRepository.getAnimalsByEnclosureRowsCount(enclosureId);
            logger.info("Animals by enclosure rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Animals by enclosure rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<Animal> getAnimalsByGroupWithPagination(AnimalGroup animalGroup, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            if (animalGroup == null) {
                throw new ValidationException("Animal group is null");
            }

            logger.info("Try to get animals by group with pagination");
            List<Animal> animals = animalRepository.getAnimalsByGroupWithPagination(animalGroup, pageNumber, limit);
            logger.info("Animals by group were fetched with pagination successfully");
            return animals;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Animals by group weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getAnimalsByGroupRowsCount(AnimalGroup animalGroup) {
        try {
            logger.info("Try to get animals by group rows count");
            if (animalGroup == null) {
                throw new ValidationException("Animal group is null");
            }
            int count = animalRepository.getAnimalsByGroupRowsCount(animalGroup);
            logger.info("Animals by group rows count were fetched successfully");
            return count;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Animals by group rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<Animal> getAnimalsByHabitatWithPagination(HabitatType habitatType, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            if (habitatType == null) {
                throw new ValidationException("Habitat type is null");
            }

            logger.info("Try to get animals by habitat with pagination");
            List<Animal> animals = animalRepository.getAnimalsByHabitatWithPagination(habitatType, pageNumber, limit);
            logger.info("Animals by habitat were fetched with pagination successfully");
            return animals;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Animals by habitat weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getAnimalsByHabitatRowsCount(HabitatType habitatType) {
        try {
            if (habitatType == null) {
                throw new ValidationException("Habitat type is null");
            }
            logger.info("Try to get animals by habitat type rows count");
            int count = animalRepository.getAnimalsByHabitatRowsCount(habitatType);
            logger.info("Animals by habitat type rows count were fetched successfully");
            return count;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Animals by habitat type rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<Animal> getAnimalsByHealthStatusWithPagination(HealthStatus healthStatus, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            if (healthStatus == null) {
                throw new ValidationException("Health status is null");
            }
            logger.info("Try to get animals by health status with pagination");
            List<Animal> animals = animalRepository.getAnimalsByHealthStatusWithPagination(healthStatus, pageNumber, limit);
            logger.info("Animals by health status were fetched with pagination successfully");
            return animals;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Animals by health status weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getAnimalsByByHealthStatusRowsCount(HealthStatus healthStatus) {
        try {
            if (healthStatus == null) {
                throw new ValidationException("Health status is null");
            }
            logger.info("Try to get animals by health status rows count");
            int count = animalRepository.getAnimalsByByHealthStatusRowsCount(healthStatus);
            logger.info("Animals by health status rows count were fetched successfully");
            return count;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Animals by health status rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public boolean setAnimalForUpdate(Animal animal, MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                animal.setImage(null);
            } else {
                String filename = saveAnimalImage(file);
                animal.setImage(filename);
            }
        } catch (IOException e) {
            throw new AnimalValidationException("Can't add image to animal");
        }
        return true;
    }

    public String saveAnimalImage(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();

        Path uploadPath = Paths.get(Paths.get("").toAbsolutePath() + "\\app\\public\\" + filename);

        Files.createDirectories(uploadPath.getParent());
        Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
