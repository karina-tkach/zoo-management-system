package org.university.zoomanagementsystem.feeding_schedule.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.animal.service.AnimalService;
import org.university.zoomanagementsystem.exception.not_found.AnimalNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.FeedingScheduleNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.UserNotFoundException;
import org.university.zoomanagementsystem.exception.validation.FeedingScheduleValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.feeding_schedule.FeedingSchedule;
import org.university.zoomanagementsystem.feeding_schedule.FeedingScheduleValidator;
import org.university.zoomanagementsystem.feeding_schedule.repository.FeedingScheduleRepository;
import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.service.UserService;

import java.util.List;


@Service
public class FeedingScheduleService {
    private final FeedingScheduleValidator feedingScheduleValidator;
    private final FeedingScheduleRepository feedingScheduleRepository;
    private final AnimalService animalService;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(FeedingScheduleService.class);
    
    public FeedingScheduleService(FeedingScheduleValidator feedingScheduleValidator,
                                  FeedingScheduleRepository feedingScheduleRepository,
                                  AnimalService animalService,
                                  UserService userService) {
        this.feedingScheduleValidator = feedingScheduleValidator;
        this.feedingScheduleRepository = feedingScheduleRepository;
        this.animalService = animalService;
        this.userService = userService;
    }
    
    @Scheduled(cron = "0 * * * * *")
    public void resetFeedingFlagsByTime() {
        try {
            logger.info("Try to reset feeding flags");
            feedingScheduleRepository.resetFeedingFlagsByTime();
            logger.info("Feeding flags were reset successfully");
        } catch (DataAccessException exception) {
            logger.warn("Feeding flags were not reset\n{}", exception.getMessage());
            throw exception;
        }
    }

    public FeedingSchedule addFeedingSchedule(FeedingSchedule feedingSchedule) {
        try {
            logger.info("Try to add feeding schedule");
            feedingScheduleValidator.validate(feedingSchedule);

            animalService.getAnimalById(feedingSchedule.getAnimal().getId());

            FeedingSchedule feedingScheduleExists = feedingScheduleRepository.getFeedingScheduleByAnimalAndTime(feedingSchedule.getAnimal().getId(), feedingSchedule.getTime());

            if (feedingScheduleExists != null) {
                throw new FeedingScheduleValidationException("Cannot add feeding schedule as schedule for this animal and time already exists (change one of them)");
            }

            User caretaker = userService.getUserById(feedingSchedule.getCaretaker().getId());
            if (!caretaker.getRole().equals(Role.CARETAKER)) {
                throw new FeedingScheduleValidationException("User role must be 'CARETAKER'");
            }

            FeedingSchedule feedingScheduleExistsByCaretaker = feedingScheduleRepository.getFeedingScheduleByCaretakerAndTime(caretaker.getId(), feedingSchedule.getTime());

            if (feedingScheduleExistsByCaretaker != null) {
                throw new FeedingScheduleValidationException("Cannot add feeding schedule as schedule for this caretaker and time already exists (change one of them)");
            }

            int id = feedingScheduleRepository.addFeedingSchedule(feedingSchedule);
            if (id == -1) {
                throw new FeedingScheduleValidationException("Unable to retrieve the generated key");
            }

            feedingSchedule.setId(id);
            logger.info("Feeding schedule was added:\n{}", feedingSchedule);
            return getFeedingScheduleById(id);
        } catch (FeedingScheduleValidationException | FeedingScheduleNotFoundException |
                 AnimalNotFoundException | UserNotFoundException | DataAccessException exception) {
            logger.warn("Feeding schedule wasn't added: {}\n{}", feedingSchedule, exception.getMessage());
            throw exception;
        }
    }

    public FeedingSchedule getFeedingScheduleById(int id) {
        try {
            logger.info("Try to get feeding schedule by id");
            FeedingSchedule feedingSchedule = feedingScheduleRepository.getFeedingScheduleById(id);
            if (feedingSchedule == null) {
                throw new FeedingScheduleNotFoundException(String.format("Feeding schedule with id %d was not found", id));
            }
            logger.info("Feeding schedule was fetched successfully");
            return feedingSchedule;
        } catch (FeedingScheduleNotFoundException | DataAccessException exception) {
            logger.warn("Feeding schedule wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public FeedingSchedule updateFeedingScheduleById(FeedingSchedule feedingSchedule, int id) {
        try {
            FeedingSchedule feedingScheduleToUpdate = getFeedingScheduleById(id);
            logger.info("Try to update feeding schedule");

            feedingScheduleValidator.validateFeedingScheduleForUpdate(feedingScheduleToUpdate, feedingSchedule);

            animalService.getAnimalById(feedingSchedule.getAnimal().getId());

            FeedingSchedule feedingScheduleExists = feedingScheduleRepository.getFeedingScheduleByAnimalAndTime(feedingSchedule.getAnimal().getId(), feedingSchedule.getTime());

            if (feedingScheduleExists != null && feedingScheduleExists.getId() != id) {
                throw new FeedingScheduleValidationException("Cannot add feeding schedule as schedule for this animal and time already exists (change one of them)");
            }

            User caretaker = userService.getUserById(feedingSchedule.getCaretaker().getId());
            if (!caretaker.getRole().equals(Role.CARETAKER)) {
                throw new FeedingScheduleValidationException("User role must be 'CARETAKER'");
            }

            FeedingSchedule feedingScheduleExistsByCaretaker = feedingScheduleRepository.getFeedingScheduleByCaretakerAndTime(caretaker.getId(), feedingSchedule.getTime());

            if (feedingScheduleExistsByCaretaker != null && feedingScheduleExistsByCaretaker.getId() != id) {
                throw new FeedingScheduleValidationException("Cannot add feeding schedule as schedule for this caretaker and time already exists (change one of them)");
            }

            feedingScheduleRepository.updateFeedingScheduleById(feedingSchedule, id);

            logger.info("Feeding schedule was updated:\n{}", feedingSchedule);
            return getFeedingScheduleById(id);
        } catch (FeedingScheduleNotFoundException | FeedingScheduleValidationException |
                 AnimalNotFoundException | UserNotFoundException | DataAccessException exception) {
            logger.warn("Feeding schedule wasn't updated: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public FeedingSchedule updateFeedingScheduleStatusToTrueById(int id) {
        try {
            getFeedingScheduleById(id);
            logger.info("Try to update feeding schedule status to true");

            feedingScheduleRepository.updateFeedingScheduleStatusToTrueById(id);

            logger.info("Feeding schedule status was updated to true");
            return getFeedingScheduleById(id);
        } catch (FeedingScheduleNotFoundException | DataAccessException exception) {
            logger.warn("Feeding schedule status wasn't updated to true: \n{}", exception.getMessage());
            throw exception;
        }
    }

    public boolean deleteFeedingScheduleById(int id) {
        try {
            FeedingSchedule feedingSchedule = getFeedingScheduleById(id);
            logger.info("Try to delete feeding schedule by id");
            feedingScheduleRepository.deleteFeedingScheduleById(id);
            logger.info("Feeding schedule was deleted:\n{}", feedingSchedule);
            return true;
        } catch (FeedingScheduleNotFoundException | DataAccessException exception) {
            logger.warn("Feeding schedule wasn't deleted: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public List<FeedingSchedule> getFeedingSchedulesWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get feeding schedules with pagination");
            List<FeedingSchedule> feedingSchedules = feedingScheduleRepository.getFeedingSchedulesWithPagination(pageNumber, limit);
            logger.info("Feeding schedules were fetched with pagination successfully");
            return feedingSchedules;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Feeding schedules weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getFeedingSchedulesRowsCount() {
        try {
            logger.info("Try to get feeding schedules rows count");
            int count = feedingScheduleRepository.getFeedingSchedulesRowsCount();
            logger.info("Feeding schedules rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Feeding schedules rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<FeedingSchedule> getFeedingSchedulesByAnimalWithPagination(int animalId, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get feeding schedules by animal with pagination");
            List<FeedingSchedule> feedingSchedules = feedingScheduleRepository.getFeedingSchedulesByAnimalWithPagination(animalId, pageNumber, limit);
            logger.info("Feeding schedules by animal were fetched with pagination successfully");
            return feedingSchedules;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Feeding schedules by animal weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getFeedingSchedulesByAnimalRowsCount(int animalId) {
        try {
            logger.info("Try to get feeding schedules by animal rows count");
            int count = feedingScheduleRepository.getFeedingSchedulesByAnimalRowsCount(animalId);
            logger.info("Feeding schedules by animal rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Feeding schedules by animal rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<FeedingSchedule> getFeedingSchedulesByCaretakerWithPagination(int caretakerId, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }

            logger.info("Try to get feeding schedules by caretaker with pagination");
            List<FeedingSchedule> feedingSchedules = feedingScheduleRepository.getFeedingSchedulesByCaretakerWithPagination(caretakerId, pageNumber, limit);
            logger.info("Feeding schedules by caretaker were fetched with pagination successfully");
            return feedingSchedules;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Feeding schedules by caretaker weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getFeedingSchedulesByCaretakerRowsCount(int caretakerId) {
        try {
            logger.info("Try to get feeding schedules by caretaker rows count");
            int count = feedingScheduleRepository.getFeedingSchedulesByCaretakerRowsCount(caretakerId);
            logger.info("Feeding schedules by caretaker rows count were fetched successfully");
            return count;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Feeding schedules by caretaker rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<FeedingSchedule> getFeedingSchedulesByCompletionWithPagination(boolean isFeedingCompleted, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }

            logger.info("Try to get feeding schedules by completion with pagination");
            List<FeedingSchedule> feedingSchedules = feedingScheduleRepository.getFeedingSchedulesByCompletionWithPagination(isFeedingCompleted, pageNumber, limit);
            logger.info("Feeding schedules by completion were fetched with pagination successfully");
            return feedingSchedules;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Feeding schedules by completion weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getFeedingSchedulesByCompletionRowsCount(boolean isFeedingCompleted) {
        try {
            logger.info("Try to get feeding schedules by completion rows count");
            int count = feedingScheduleRepository.getFeedingSchedulesByCompletionRowsCount(isFeedingCompleted);
            logger.info("Feeding schedules by completion rows count were fetched successfully");
            return count;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Feeding schedules by completion rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
}
