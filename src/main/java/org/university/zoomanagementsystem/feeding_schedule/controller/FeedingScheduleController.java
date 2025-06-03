package org.university.zoomanagementsystem.feeding_schedule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.feeding_schedule.FeedingSchedule;
import org.university.zoomanagementsystem.feeding_schedule.service.FeedingScheduleService;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.service.UserService;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feeding-schedules")
public class FeedingScheduleController {
    private final FeedingScheduleService feedingScheduleService;
    private final UserService userService;

    public FeedingScheduleController(FeedingScheduleService feedingScheduleService,
                                     UserService userService) {
        this.feedingScheduleService = feedingScheduleService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFeedingSchedules(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<FeedingSchedule> feedingScheduleList = feedingScheduleService.getFeedingSchedulesWithPagination(page, pageSize);
        int rows = feedingScheduleService.getFeedingSchedulesRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", feedingScheduleList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CARETAKER')")
    @GetMapping("/caretaker")
    public ResponseEntity<Map<String, Object>> getAllFeedingSchedulesByCaretaker(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            Principal principal) {
        User caretaker = userService.getUserByEmail(principal.getName());

        List<FeedingSchedule> feedingScheduleList = feedingScheduleService.getFeedingSchedulesByCaretakerWithPagination(caretaker.getId(), page, pageSize);
        int rows = feedingScheduleService.getFeedingSchedulesByCaretakerRowsCount(caretaker.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("data", feedingScheduleList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/by-animal/{id}")
    public ResponseEntity<Map<String, Object>> getAllFeedingSchedulesByAnimal(
            @PathVariable int id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<FeedingSchedule> feedingScheduleList = feedingScheduleService.getFeedingSchedulesByAnimalWithPagination(id, page, pageSize);
        int rows = feedingScheduleService.getFeedingSchedulesByAnimalRowsCount(id);

        Map<String, Object> response = new HashMap<>();
        response.put("data", feedingScheduleList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/by-completion")
    public ResponseEntity<Map<String, Object>> getAllFeedingSchedulesByCompletion(
            @RequestParam("completion") boolean isFeedingCompleted,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<FeedingSchedule> feedingScheduleList = feedingScheduleService.getFeedingSchedulesByCompletionWithPagination(isFeedingCompleted, page, pageSize);
        int rows = feedingScheduleService.getFeedingSchedulesByCompletionRowsCount(isFeedingCompleted);

        Map<String, Object> response = new HashMap<>();
        response.put("data", feedingScheduleList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<FeedingSchedule> addFeedingSchedule(@RequestBody FeedingSchedule feedingSchedule) {
        FeedingSchedule createdFeedingSchedule = feedingScheduleService.addFeedingSchedule(feedingSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFeedingSchedule);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<FeedingSchedule> updateFeedingSchedule(
            @PathVariable int id,
            @RequestBody FeedingSchedule feedingSchedule) {
        FeedingSchedule updatedFeedingSchedule = feedingScheduleService.updateFeedingScheduleById(feedingSchedule, id);
        return ResponseEntity.ok(updatedFeedingSchedule);
    }

    @PreAuthorize("hasAuthority('CARETAKER')")
    @PatchMapping("/{id}/mark-done")
    public ResponseEntity<FeedingSchedule> updateFeedingScheduleStatus(
            @PathVariable int id) {
        FeedingSchedule updatedFeedingSchedule = feedingScheduleService.updateFeedingScheduleStatusToTrueById(id);
        return ResponseEntity.ok(updatedFeedingSchedule);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedingSchedule(@PathVariable int id) {
        feedingScheduleService.deleteFeedingScheduleById(id);
        return ResponseEntity.ok(Map.of("message", String.format("Feeding schedule with id %d was successfully deleted", id)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<FeedingSchedule> getFeedingScheduleById(@PathVariable int id) {
        FeedingSchedule feedingSchedule = feedingScheduleService.getFeedingScheduleById(id);
        return ResponseEntity.ok(feedingSchedule);
    }
}
