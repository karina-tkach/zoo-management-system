package org.university.zoomanagementsystem.excursion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.exception.not_found.ExcursionNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.UserNotFoundException;
import org.university.zoomanagementsystem.exception.validation.ExcursionValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.excursion.Excursion;
import org.university.zoomanagementsystem.excursion.ExcursionValidator;
import org.university.zoomanagementsystem.excursion.repository.ExcursionRepository;
import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.service.UserService;

import java.util.List;

@Service
public class ExcursionService {
    private final ExcursionValidator excursionValidator;
    private final ExcursionRepository excursionRepository;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(ExcursionService.class);

    public ExcursionService(ExcursionValidator excursionValidator,
                            ExcursionRepository excursionRepository,
                            UserService userService) {
        this.excursionValidator = excursionValidator;
        this.excursionRepository = excursionRepository;
        this.userService = userService;
    }

    public Excursion addExcursion(Excursion excursion) {
        try {
            logger.info("Try to add excursion");

            excursionValidator.validate(excursion);

            if (excursionRepository.guideHasConflict(excursion)) {
                throw new ExcursionValidationException("This guide is already scheduled for an excursion during the specified time.");
            }

            User guide = userService.getUserById(excursion.getGuide().getId());
            if (!guide.getRole().equals(Role.GUIDE)) {
                throw new ExcursionValidationException("User is not having role 'GUIDE'");
            }

            int id = excursionRepository.addExcursion(excursion);
            if (id == -1) {
                throw new ExcursionValidationException("Unable to retrieve the generated key");
            }

            excursion.setId(id);
            logger.info("Excursion was added:\n{}", excursion);
            return getExcursionById(id);
        } catch (ExcursionValidationException | ExcursionNotFoundException |
                 UserNotFoundException | DataAccessException exception) {
            logger.warn("Excursion wasn't added: {}\n{}", excursion, exception.getMessage());
            throw exception;
        }
    }

    public Excursion getExcursionById(int id) {
        try {
            logger.info("Try to get excursion by id");
            Excursion excursion = excursionRepository.getExcursionById(id);
            if(excursion == null) {
                throw new ExcursionNotFoundException(String.format("Excursion with id %d was not found", id));
            }
            logger.info("Excursion was fetched successfully");
            return excursion;
        } catch (ExcursionNotFoundException | DataAccessException exception) {
            logger.warn("Excursion wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public Excursion updateExcursionById(Excursion excursion, int id) {
        try {
            Excursion excursionToUpdate = getExcursionById(id);
            logger.info("Try to update excursion");

            excursionValidator.validateExcursionForUpdate(excursionToUpdate, excursion);

            if (excursionRepository.guideHasConflict(excursion)) {
                throw new ExcursionValidationException("This guide is already scheduled for an excursion during the specified time.");
            }

            User guide = userService.getUserById(excursion.getGuide().getId());
            if (!guide.getRole().equals(Role.GUIDE)) {
                throw new ExcursionValidationException("User is not having role 'GUIDE'");
            }
            excursionRepository.updateExcursionById(excursion, id);

            logger.info("Excursion was updated:\n{}", excursion);
            return getExcursionById(id);
        } catch (ExcursionNotFoundException | ExcursionValidationException |
                 UserNotFoundException | DataAccessException exception) {
            logger.warn("Excursion wasn't updated: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public boolean deleteExcursionById(int id) {
        try {
            Excursion excursion = getExcursionById(id);
            logger.info("Try to delete excursion by id");
            excursionRepository.deleteExcursionById(id);
            logger.info("Excursion was deleted:\n{}", excursion);
            return true;
        } catch (ExcursionNotFoundException | DataAccessException exception) {
            logger.warn("Excursion wasn't deleted: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public List<Excursion> getExcursionsWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get excursions with pagination");
            List<Excursion> excursions = excursionRepository.getExcursionsWithPagination(pageNumber, limit);
            logger.info("Excursions were fetched with pagination successfully");
            return excursions;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Excursions weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getExcursionsRowsCount() {
        try {
            logger.info("Try to get excursions rows count");
            int count = excursionRepository.getExcursionsRowsCount();
            logger.info("Excursions rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Excursions rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<Excursion> getExcursionsByGuideWithPagination(int guideId, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get excursions by guide with pagination");
            List<Excursion> excursions = excursionRepository.getExcursionsByGuideWithPagination(guideId, pageNumber, limit);
            logger.info("Excursions by guide were fetched with pagination successfully");
            return excursions;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Excursions by guide weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getExcursionsByGuideRowsCount(int guideId) {
        try {
            logger.info("Try to get excursions by guide rows count");
            int count = excursionRepository.getExcursionsByGuideRowsCount(guideId);
            logger.info("Excursions by guide rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Excursions by guide rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<Excursion> getAvailableExcursions() {
        try {
            logger.info("Try to get available excursions");
            List<Excursion> excursions = excursionRepository.getAvailableExcursions();
            logger.info("Available excursions were fetched successfully");
            return excursions;
        } catch (DataAccessException exception) {
            logger.warn("Available excursions weren't fetched \n{}", exception.getMessage());
            throw exception;
        }
    }
}
