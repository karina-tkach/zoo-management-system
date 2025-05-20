package org.university.zoomanagementsystem.events.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.university.zoomanagementsystem.events.Event;
import org.university.zoomanagementsystem.events.service.EventService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Event> eventList = eventService.getEventsWithPagination(page, pageSize);
        int rows = eventService.getEventsRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", eventList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> addEvent(
            @RequestPart("event") Event event,
            @RequestParam(value = "image", required = true) MultipartFile image
    ) throws IOException {
        event.setImage(eventService.saveEventImage(image));
        Event createdEvent = eventService.addEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Event> updateEvent(
            @PathVariable int id,
            @RequestPart("event") Event event,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        eventService.setEventForUpdate(event, image);
        Event updatedEvent = eventService.updateEventById(event, id);
        return ResponseEntity.ok(updatedEvent);
    }

    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable int id) {
        eventService.deleteEventById(id);
        return ResponseEntity.ok(Map.of("message", String.format("Event with id %d was successfully deleted", id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }
}
