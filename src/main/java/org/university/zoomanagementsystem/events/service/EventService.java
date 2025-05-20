package org.university.zoomanagementsystem.events.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.university.zoomanagementsystem.exception.not_found.EventNotFoundException;
import org.university.zoomanagementsystem.exception.validation.EventValidationException;
import org.university.zoomanagementsystem.exception.validation.ValidationException;
import org.university.zoomanagementsystem.events.Event;
import org.university.zoomanagementsystem.events.EventValidator;
import org.university.zoomanagementsystem.events.repository.EventRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private final EventValidator eventValidator;
    private final EventRepository eventRepository;

    private final Logger logger = LoggerFactory.getLogger(EventService.class);

    public EventService(EventValidator eventValidator, EventRepository eventRepository) {
        this.eventValidator = eventValidator;
        this.eventRepository = eventRepository;
    }

    public Event addEvent(Event event) {
        try {
            logger.info("Try to add event");

            eventValidator.validate(event);

            int id = eventRepository.addEvent(event);
            if (id == -1) {
                throw new EventValidationException("Unable to retrieve the generated key");
            }

            event.setId(id);
            logger.info("Event was added:\n{}", event);
            return getEventById(id);
        } catch (EventValidationException | EventNotFoundException | DataAccessException exception) {
            logger.warn("Event wasn't added: {}\n{}", event, exception.getMessage());
            throw exception;
        }
    }

    public Event getEventById(int id) {
        try {
            logger.info("Try to get event by id");
            Event event = eventRepository.getEventById(id);
            if(event == null) {
                throw new EventNotFoundException(String.format("Event with id %d was not found", id));
            }
            logger.info("Event was fetched successfully");
            return event;
        } catch (EventNotFoundException | DataAccessException exception) {
            logger.warn("Event wasn't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }

    public Event updateEventById(Event event, int id) {
        try {
            Event eventToUpdate = getEventById(id);
            logger.info("Try to update event");

            eventValidator.validateEventForUpdate(eventToUpdate, event);
            eventRepository.updateEventById(event, id);

            logger.info("Event was updated:\n{}", event);
            return getEventById(id);
        } catch (EventNotFoundException | EventValidationException | DataAccessException exception) {
            logger.warn("Event wasn't updated: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public boolean deleteEventById(int id) {
        try {
            Event event = eventRepository.getEventById(id);
            logger.info("Try to delete event by id");
            eventRepository.deleteEventById(id);
            logger.info("Event was deleted:\n{}", event);
            return true;
        } catch (EventNotFoundException | DataAccessException exception) {
            logger.warn("Event wasn't deleted: {}\n{}", id, exception.getMessage());
            throw exception;
        }
    }

    public List<Event> getEventsWithPagination(int pageNumber, int limit) {
        try {
            if (pageNumber < 0 || limit < 0) {
                throw new ValidationException("Page number and limit must be greater than 0");
            }
            logger.info("Try to get events with pagination");
            List<Event> events = eventRepository.getEventsWithPagination(pageNumber, limit);
            logger.info("Events were fetched with pagination successfully");
            return events;
        } catch (ValidationException | DataAccessException exception) {
            logger.warn("Events weren't fetched with pagination\n{}", exception.getMessage());
            throw exception;
        }
    }

    public int getEventsRowsCount() {
        try {
            logger.info("Try to get events rows count");
            int count = eventRepository.getEventsRowsCount();
            logger.info("Events rows count were fetched successfully");
            return count;
        } catch (DataAccessException exception) {
            logger.warn("Events rows count weren't fetched\n{}", exception.getMessage());
            throw exception;
        }
    }
    public boolean setEventForUpdate(Event event, MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                event.setImage(null);
            } else {
                String filename = saveEventImage(file);
                event.setImage(filename);
            }
        } catch (IOException e) {
            throw new EventValidationException("Can't add image to event");
        }
        return true;
    }

    public String saveEventImage(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();

        Path uploadPath = Paths.get(Paths.get("").toAbsolutePath() + "\\app\\public\\" + filename);

        Files.createDirectories(uploadPath.getParent());
        Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
