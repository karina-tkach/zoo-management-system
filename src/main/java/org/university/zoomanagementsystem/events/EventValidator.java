package org.university.zoomanagementsystem.events;

import org.springframework.stereotype.Component;
import org.university.zoomanagementsystem.exception.validation.EventValidationException;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class EventValidator {

    public void validate(Event event) {
        validateEventNotNull(event);
        validateTitleAndDescription(event.getTitle(), event.getDescription());
        validateDate(event.getDate());
        validateStartTime(event.getStartTime());
        validateDuration(event.getDurationMinutes());
        validateLocation(event.getLocation());
        validateImage(event.getImage());
    }

    public void validateEventForUpdate(Event eventToUpdate, Event updatedEvent) {
        if (updatedEvent.getTitle() == null) {
            updatedEvent.setTitle(eventToUpdate.getTitle());
        }
        if (updatedEvent.getDescription() == null) {
            updatedEvent.setDescription(eventToUpdate.getDescription());
        }
        if (updatedEvent.getDate() == null) {
            updatedEvent.setDate(eventToUpdate.getDate());
        }
        if (updatedEvent.getStartTime() == null) {
            updatedEvent.setStartTime(eventToUpdate.getStartTime());
        }
        if (updatedEvent.getDurationMinutes() == 0) {
            updatedEvent.setDurationMinutes(eventToUpdate.getDurationMinutes());
        }
        if (updatedEvent.getLocation() == null) {
            updatedEvent.setLocation(eventToUpdate.getLocation());
        }
        if (updatedEvent.getImage() == null) {
            updatedEvent.setImage(eventToUpdate.getImage());
        }

        validate(updatedEvent);
    }

    private void validateEventNotNull(Event event) {
        if (event == null) {
            throw new EventValidationException("Event was null");
        }
    }

    private void validateTitleAndDescription(String title, String description) {
        if (title == null || description == null) {
            throw new EventValidationException("Event title or description was null");
        }
        if (title.isBlank() || description.isBlank()) {
            throw new EventValidationException("Event title or description was empty");
        }
        if (title.length() > 150) {
            throw new EventValidationException("Event title length must be less than or equal to 150");
        }
        if (description.length() > 300) {
            throw new EventValidationException("Event description length must be less than or equal to 300");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new EventValidationException("Event date was null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new EventValidationException("Event date is in the past");
        }
    }

    private void validateStartTime(LocalTime startTime) {
        if (startTime == null) {
            throw new EventValidationException("Event start time was null");
        }
    }

    private void validateDuration(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new EventValidationException("Event duration must be positive");
        }
        if (durationMinutes > 480) {
            throw new EventValidationException("Event duration exceeds 8 hours");
        }
    }

    private void validateLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new EventValidationException("Event location was null or empty");
        }
        if (location.length() > 150) {
            throw new EventValidationException("Event location length must be less than or equal to 150");
        }
    }

    private void validateImage(String image) {
        if (image == null) {
            throw new EventValidationException("Event image was null");
        }
        if (image.isBlank()) {
            throw new EventValidationException("Event image was empty");
        }
        if (image.length() > 255) {
            throw new EventValidationException("Event image length must be less than or equal to 255");
        }
        if (!image.matches("[a-zA-Z0-9._-]+\\.(jpg|jpeg|png|gif)$")) {
            throw new EventValidationException("Event image must be a valid image file (jpg, jpeg, png, gif)");
        }
    }
}
