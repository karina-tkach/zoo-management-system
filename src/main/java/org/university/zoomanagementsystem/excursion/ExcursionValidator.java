package org.university.zoomanagementsystem.excursion;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.exception.validation.ExcursionValidationException;
import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.user.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ExcursionValidator {
    public void validate(Excursion excursion) {
        validateExcursionNotNull(excursion);
        validateGuide(excursion.getGuide());
        validateTopicAndDescription(excursion.getTopic(), excursion.getDescription());
        validateDate(excursion.getDate());
        validateStartTime(excursion.getStartTime());
        validateDurationMinutes(excursion.getDurationMinutes());
        validateMaxParticipantsAndBookedCount(excursion.getMaxParticipants(), excursion.getBookedCount());
    }

    public void validateExcursionForUpdate(Excursion excursionToUpdate, Excursion updatedExcursion) {
        if (updatedExcursion.getGuide() == null) {
            updatedExcursion.setGuide(excursionToUpdate.getGuide());
        }
        if (updatedExcursion.getTopic() == null) {
            updatedExcursion.setTopic(excursionToUpdate.getTopic());
        }
        if (updatedExcursion.getDescription() == null) {
            updatedExcursion.setDescription(excursionToUpdate.getDescription());
        }
        if (updatedExcursion.getDate() == null) {
            updatedExcursion.setDate(excursionToUpdate.getDate());
        }
        if (updatedExcursion.getStartTime() == null) {
            updatedExcursion.setStartTime(excursionToUpdate.getStartTime());
        }
        if (updatedExcursion.getDurationMinutes() == 0) {
            updatedExcursion.setDurationMinutes(excursionToUpdate.getDurationMinutes());
        }
        if (updatedExcursion.getMaxParticipants() == 0) {
            updatedExcursion.setMaxParticipants(excursionToUpdate.getMaxParticipants());
        }
        if (updatedExcursion.getBookedCount() == 0) {
            updatedExcursion.setBookedCount(excursionToUpdate.getBookedCount());
        }

        validate(updatedExcursion);
    }

    private void validateExcursionNotNull(Excursion excursion) {
        if (excursion == null) {
            throw new ExcursionValidationException("Excursion was null");
        }
    }

    private void validateGuide(User user) {
        if (user == null) {
            throw new ExcursionValidationException("Excursion guide was null");
        }
        else if (!user.getRole().equals(Role.GUIDE)) {
            throw new ExcursionValidationException("Excursion guide role must be 'GUIDE'");
        }
    }

    private void validateTopicAndDescription(String topic, String description) {
        if (topic == null || description == null) {
            throw new ExcursionValidationException("Excursion topic or description was null");
        }
        else if(topic.isBlank() || description.isBlank()) {
            throw new ExcursionValidationException("Excursion topic or description was empty");
        }
        else if (topic.length() > 150 || description.length() > 250) {
            throw new ExcursionValidationException("Excursion topic length must be less than 150 and description length must be less than 250");
        }
    }

    private void validateStartTime(LocalTime startTime) {
        if (startTime == null) {
            throw new ExcursionValidationException("Excursion start time was null");
        }
    }

    private void validateDate(LocalDate date) {
        if(date == null) {
            throw new ExcursionValidationException("Excursion date was null");
        }
        else if(date.isBefore(LocalDate.now())) {
            throw new ExcursionValidationException("Excursion date is before current date");
        }
    }

    private void validateDurationMinutes(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new ExcursionValidationException("Excursion duration minutes must be positive");
        }
        if (durationMinutes > 480) {
            throw new ExcursionValidationException("Excursion duration exceeds 8 hours");
        }
    }

    private void validateMaxParticipantsAndBookedCount(int maxParticipants, int bookedCount) {
        if(maxParticipants < 0 || bookedCount < 0) {
            throw new ExcursionValidationException("Excursion maxParticipants or bookedCount must be non-negative");
        }
        else if(maxParticipants < bookedCount) {
            throw new ExcursionValidationException("Excursion maxParticipants is lower than bookedCount");
        }
        else if(maxParticipants > 100) {
            throw new ExcursionValidationException("Excursion maxParticipants is higher than 100");
        }
    }
}
