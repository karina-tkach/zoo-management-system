package org.university.zoomanagementsystem.vet_examination_schedule;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.exception.validation.ExaminationScheduleValidationException;
import org.university.zoomanagementsystem.user.User;

import java.time.LocalDateTime;

@Component
public class ExaminationScheduleValidator {
    public void validate(ExaminationSchedule examinationSchedule) {
        validateExaminationScheduleIsNotNull(examinationSchedule);
        validateAnimal(examinationSchedule.getAnimal());
        validateVeterinarian(examinationSchedule.getVeterinarian());
        validateDateTime(examinationSchedule.getPlannedDateTime());
        validateReason(examinationSchedule.getReason());

    }

    public void validateExaminationScheduleForUpdate(ExaminationSchedule examinationScheduleToUpdate, ExaminationSchedule examinationSchedule) {
        if(examinationSchedule.getAnimal() == null) {
            examinationSchedule.setAnimal(examinationScheduleToUpdate.getAnimal());
        }
        if(examinationSchedule.getVeterinarian() == null) {
            examinationSchedule.setVeterinarian(examinationScheduleToUpdate.getVeterinarian());
        }
        if (examinationSchedule.getPlannedDateTime() == null) {
            examinationSchedule.setPlannedDateTime(examinationScheduleToUpdate.getPlannedDateTime());
        }
        if (examinationSchedule.getReason() == null) {
            examinationSchedule.setReason(examinationScheduleToUpdate.getReason());
        }

        validate(examinationSchedule);
    }

    private void validateExaminationScheduleIsNotNull(ExaminationSchedule examinationSchedule) {
        if (examinationSchedule == null) {
            throw new ExaminationScheduleValidationException("Feeding schedule was null");
        }
    }

    private void validateAnimal(Animal animal) {
        if (animal == null) {
            throw new ExaminationScheduleValidationException("Feeding schedule animal was null");
        }
        else if (animal.getId() < 0) {
            throw new ExaminationScheduleValidationException("Feeding schedule animal id was negative");
        }
    }

    private void validateVeterinarian(User veterinarian) {
        if (veterinarian == null) {
            throw new ExaminationScheduleValidationException("Feeding schedule veterinarian was null");
        }
        else if (veterinarian.getId() < 0) {
            throw new ExaminationScheduleValidationException("Feeding schedule veterinarian id was negative");
        }
    }

    private void validateDateTime(LocalDateTime time) {
        if (time == null) {
            throw new ExaminationScheduleValidationException("Feeding schedule dateTime was null");
        }
        else if (time.isBefore(LocalDateTime.now())) {
            throw new ExaminationScheduleValidationException("Feeding schedule dateTime was before current date");
        }
    }

    private void validateReason(String reason) {
        if (reason == null) {
            throw new ExaminationScheduleValidationException("Feeding schedule reason was null");
        }
        if (reason.isBlank()) {
            throw new ExaminationScheduleValidationException("Feeding schedule reason was blank");
        }
        if (reason.length() < 2) {
            throw new ExaminationScheduleValidationException("Feeding schedule reason had length less than 2");
        }
    }
}
