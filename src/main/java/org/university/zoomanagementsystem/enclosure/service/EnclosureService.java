package org.university.zoomanagementsystem.enclosure.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.enclosure.EnclosureValidator;
import org.university.zoomanagementsystem.enclosure.HabitatType;
import org.university.zoomanagementsystem.enclosure.repository.EnclosureRepository;
import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.exception.not_found.EnclosureNotFoundException;
import org.university.zoomanagementsystem.exception.validation.EnclosureValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;

import java.util.List;

@Service
public class EnclosureService {
    private final EnclosureValidator enclosureValidator;
    private final EnclosureRepository enclosureRepository;

    private final Logger logger = LoggerFactory.getLogger(EnclosureService.class);

    public EnclosureService(EnclosureValidator enclosureValidator, EnclosureRepository enclosureRepository) {
        this.enclosureValidator = enclosureValidator;
        this.enclosureRepository = enclosureRepository;
    }

    public Enclosure addEnclosure(Enclosure enclosure) {
        try {
            logger.info("Try to add enclosure");

            enclosureValidator.validate(enclosure);

            int id = enclosureRepository.addEnclosure(enclosure);
            if (id == -1) {
                throw new EnclosureValidationException("Unable to retrieve the generated key");
            }

            enclosure.setId(id);
            logger.info("Enclosure was added:\n{}", enclosure);
            return getEnclosureById(id);
        } catch (EnclosureValidationException | EnclosureNotFoundException | DataAccessException exception) {
            logger.warn("Enclosure wasn't added: {}\n{}", enclosure, exception.getMessage());
            throw exception;
        }
    }

    public Enclosure getEnclosureById(int id) {
        try {
            logger.info("Try to get enclosure by id");
            Enclosure enclosure = enclosureRepository.getEnclosureById(id);
            if(enclosure == null) {
                throw new EnclosureNotFoundException(String.format("Enclosure with id %d was not found", id));
            }
            logger.info("Enclosure was fetched successfully");
            return enclosure;
        } catch (EnclosureNotFoundException | DataAccessException exception) {
            logger.warn("Enclosure wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public Enclosure updateEnclosureById(Enclosure enclosure, int id) {
        try {
            Enclosure enclosureToUpdate = getEnclosureById(id);
            logger.info("Try to update enclosure");

            enclosureValidator.validateEnclosureForUpdate(enclosureToUpdate, enclosure);
            enclosureRepository.updateEnclosureById(enclosure, id);

            logger.info("Enclosure was updated:\n{}", enclosure);
            return getEnclosureById(id);
        } catch (EnclosureNotFoundException | EnclosureValidationException | DataAccessException exception) {
            logger.warn("Enclosure wasn't updated: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public boolean deleteEnclosureById(int id) {
        try {
            Enclosure enclosure = getEnclosureById(id);
            logger.info("Try to delete enclosure by id");
            enclosureRepository.deleteEnclosureById(id);
            logger.info("Enclosure was deleted:\n{}", enclosure);
            return true;
        } catch (EnclosureNotFoundException | DataAccessException exception) {
            logger.warn("Enclosure wasn't deleted: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public List<Enclosure> getEnclosuresWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get enclosures with pagination");
            List<Enclosure> enclosures = enclosureRepository.getEnclosuresWithPagination(pageNumber, limit);
            logger.info("Enclosures were fetched with pagination successfully");
            return enclosures;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Enclosures weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getEnclosuresRowsCount() {
        try {
            logger.info("Try to get enclosures rows count");
            int count = enclosureRepository.getEnclosuresRowsCount();
            logger.info("Enclosures rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Enclosures rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<Enclosure> getEnclosuresByEnvironmentType(HabitatType environmentType) {
        try {
            logger.info("Try to get enclosures by environment type");
            List<Enclosure> enclosures = enclosureRepository.getEnclosuresByEnvironmentType(environmentType);
            logger.info("Enclosures were fetched by environment type successfully");
            return enclosures;
        } catch (DataAccessException exception) {
            logger.warn("Enclosures weren't fetched by environment type\n{}", exception.getMessage());
            throw exception;
        }
    }
}
