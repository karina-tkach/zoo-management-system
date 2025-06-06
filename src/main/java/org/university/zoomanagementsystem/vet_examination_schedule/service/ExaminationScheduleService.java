package org.university.zoomanagementsystem.vet_examination_schedule.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.animal.service.AnimalService;
import org.university.zoomanagementsystem.exception.not_found.AnimalNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.ExaminationScheduleNotFoundException;
import org.university.zoomanagementsystem.exception.not_found.UserNotFoundException;
import org.university.zoomanagementsystem.exception.validation.ExaminationScheduleValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationSchedule;
import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.service.UserService;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationScheduleValidator;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationStatus;
import org.university.zoomanagementsystem.vet_examination_schedule.repository.ExaminationScheduleRepository;

import java.util.List;

@Service
public class ExaminationScheduleService {
    private final ExaminationScheduleValidator examinationScheduleValidator;
    private final ExaminationScheduleRepository examinationScheduleRepository;
    private final AnimalService animalService;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(ExaminationScheduleService.class);

    public ExaminationScheduleService(ExaminationScheduleValidator examinationScheduleValidator,
                                      ExaminationScheduleRepository examinationScheduleRepository,
                                      AnimalService animalService, UserService userService) {
        this.examinationScheduleValidator = examinationScheduleValidator;
        this.examinationScheduleRepository = examinationScheduleRepository;
        this.animalService = animalService;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void markAnimalsNeedingCheckup() {
        try {
            logger.info("Try to mark animals needing checkup and set examination to them");
            examinationScheduleRepository.markAnimalsNeedingCheckup();
            logger.info("Animals needing checkup were marked successfully");
        } catch (DataAccessException exception) {
            logger.warn("Animals needing checkup were not marked\n{}", exception.getMessage());
            throw exception;
        }
    }

    public ExaminationSchedule addExaminationSchedule(ExaminationSchedule examinationSchedule) {
        try {
            logger.info("Try to add vet examination schedule");
            examinationScheduleValidator.validate(examinationSchedule);

            animalService.getAnimalById(examinationSchedule.getAnimal().getId());

            ExaminationSchedule examinationScheduleExists = examinationScheduleRepository.getExaminationScheduleByAnimalAndTime(examinationSchedule.getAnimal().getId(), examinationSchedule.getPlannedDateTime());

            if (examinationScheduleExists != null) {
                throw new ExaminationScheduleValidationException("Cannot add examination schedule as schedule for this animal and time already exists (change one of them)");
            }

            User vet = userService.getUserById(examinationSchedule.getVeterinarian().getId());
            if (!vet.getRole().equals(Role.VETERINARIAN)) {
                throw new ExaminationScheduleValidationException("User role must be 'VETERINARIAN'");
            }

            ExaminationSchedule examinationScheduleExistsByVet = examinationScheduleRepository.getExaminationScheduleByVeterinarianAndTime(vet.getId(), examinationSchedule.getPlannedDateTime());

            if (examinationScheduleExistsByVet != null) {
                throw new ExaminationScheduleValidationException("Cannot add examination schedule as schedule for this veterinarian and time already exists (change one of them)");
            }

            int id = examinationScheduleRepository.addExaminationSchedule(examinationSchedule);
            if (id == -1) {
                throw new ExaminationScheduleValidationException("Unable to retrieve the generated key");
            }

            examinationSchedule.setId(id);
            logger.info("Examination schedule was added:\n{}", examinationSchedule);
            return getExaminationScheduleById(id);
        } catch (ExaminationScheduleValidationException | ExaminationScheduleNotFoundException |
                 AnimalNotFoundException | UserNotFoundException | DataAccessException exception) {
            logger.warn("Examination schedule wasn't added: {}\n{}", examinationSchedule, exception.getMessage());
            throw exception;
        }
    }

    public ExaminationSchedule getExaminationScheduleById(int id) {
        try {
            logger.info("Try to get examination schedule by id");
            ExaminationSchedule examinationSchedule = examinationScheduleRepository.getExaminationScheduleById(id);
            if (examinationSchedule == null) {
                throw new ExaminationScheduleNotFoundException(String.format("Examination schedule with id %d was not found", id));
            }
            logger.info("Examination schedule was fetched successfully");
            return examinationSchedule;
        } catch (ExaminationScheduleNotFoundException | DataAccessException exception) {
            logger.warn("Examination schedule wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public ExaminationSchedule updateExaminationScheduleById(ExaminationSchedule examinationSchedule, int id) {
        try {
            ExaminationSchedule examinationScheduleToUpdate = getExaminationScheduleById(id);
            logger.info("Try to update examination schedule");

            examinationScheduleValidator.validateExaminationScheduleForUpdate(examinationScheduleToUpdate, examinationSchedule);

            animalService.getAnimalById(examinationSchedule.getAnimal().getId());

            ExaminationSchedule examinationScheduleExists = examinationScheduleRepository.getExaminationScheduleByAnimalAndTime(examinationSchedule.getAnimal().getId(), examinationSchedule.getPlannedDateTime());

            if (examinationScheduleExists != null && examinationScheduleExists.getId() != id) {
                throw new ExaminationScheduleValidationException("Cannot add examination schedule as schedule for this animal and time already exists (change one of them)");
            }

            User vet = userService.getUserById(examinationSchedule.getVeterinarian().getId());
            if (!vet.getRole().equals(Role.VETERINARIAN)) {
                throw new ExaminationScheduleValidationException("User role must be 'VETERINARIAN'");
            }

            ExaminationSchedule examinationScheduleExistsByVet = examinationScheduleRepository.getExaminationScheduleByVeterinarianAndTime(vet.getId(), examinationSchedule.getPlannedDateTime());

            if (examinationScheduleExistsByVet != null && examinationScheduleExistsByVet.getId() != id) {
                throw new ExaminationScheduleValidationException("Cannot add examination schedule as schedule for this veterinarian and time already exists (change one of them)");
            }

            examinationScheduleRepository.updateExaminationScheduleById(examinationSchedule, id);

            logger.info("Examination schedule was updated:\n{}", examinationSchedule);
            return getExaminationScheduleById(id);
        } catch (ExaminationScheduleNotFoundException | ExaminationScheduleValidationException |
                 AnimalNotFoundException | UserNotFoundException | DataAccessException exception) {
            logger.warn("Examination schedule wasn't updated: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public boolean deleteExaminationScheduleById(int id) {
        try {
            ExaminationSchedule examinationSchedule = getExaminationScheduleById(id);
            logger.info("Try to delete examination schedule by id");
            examinationScheduleRepository.deleteExaminationScheduleById(id);
            logger.info("Examination schedule was deleted:\n{}", examinationSchedule);
            return true;
        } catch (ExaminationScheduleNotFoundException | DataAccessException exception) {
            logger.warn("Examination schedule wasn't deleted: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public List<ExaminationSchedule> getExaminationSchedulesWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get examination schedules with pagination");
            List<ExaminationSchedule> examinationSchedules = examinationScheduleRepository.getExaminationSchedulesWithPagination(pageNumber, limit);
            logger.info("Examination schedules were fetched with pagination successfully");
            return examinationSchedules;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Examination schedules weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getExaminationSchedulesRowsCount() {
        try {
            logger.info("Try to get examination schedules rows count");
            int count = examinationScheduleRepository.getExaminationSchedulesRowsCount();
            logger.info("Examination schedules rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Examination schedules rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<ExaminationSchedule> getExaminationSchedulesByAnimalWithPagination(int animalId, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get examination schedules by animal with pagination");
            List<ExaminationSchedule> examinationSchedules = examinationScheduleRepository.getExaminationSchedulesByAnimalWithPagination(animalId, pageNumber, limit);
            logger.info("Examination schedules by animal were fetched with pagination successfully");
            return examinationSchedules;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Examination schedules by animal weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getExaminationSchedulesByAnimalRowsCount(int animalId) {
        try {
            logger.info("Try to get examination schedules by animal rows count");
            int count = examinationScheduleRepository.getExaminationSchedulesByAnimalRowsCount(animalId);
            logger.info("Examination schedules by animal rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Examination schedules by animal rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<ExaminationSchedule> getExaminationSchedulesByVeterinarianWithPagination(int veterinarianId, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }

            logger.info("Try to get examination schedules by veterinarian with pagination");
            List<ExaminationSchedule> examinationSchedules = examinationScheduleRepository.getExaminationSchedulesByVeterinarianWithPagination(veterinarianId, pageNumber, limit);
            logger.info("Examination schedules by veterinarian were fetched with pagination successfully");
            return examinationSchedules;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Examination schedules by veterinarian weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getExaminationSchedulesByVeterinarianRowsCount(int veterinarianId) {
        try {
            logger.info("Try to get examination schedules by veterinarian rows count");
            int count = examinationScheduleRepository.getExaminationSchedulesByVeterinarianRowsCount(veterinarianId);
            logger.info("Examination schedules by veterinarian rows count were fetched successfully");
            return count;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Examination schedules by veterinarian rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public List<ExaminationSchedule> getExaminationSchedulesByStatusWithPagination(ExaminationStatus status, int pageNumber, int limit) {
        try {
            if (pageNumber <= 0 || limit <= 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }

            logger.info("Try to get examination schedules by status with pagination");
            List<ExaminationSchedule> examinationSchedules = examinationScheduleRepository.getExaminationSchedulesByStatusWithPagination(status, pageNumber, limit);
            logger.info("Examination schedules by status were fetched with pagination successfully");
            return examinationSchedules;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Examination schedules by status weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getExaminationSchedulesByStatusRowsCount(ExaminationStatus status) {
        try {
            logger.info("Try to get examination schedules by status rows count");
            int count = examinationScheduleRepository.getExaminationSchedulesByStatusRowsCount(status);
            logger.info("Examination schedules by status rows count were fetched successfully");
            return count;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Examination schedules by status rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
}
