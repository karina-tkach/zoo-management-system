package org.university.zoomanagementsystem.vet_examination_schedule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.user.Role;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationSchedule;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.service.UserService;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationStatus;
import org.university.zoomanagementsystem.vet_examination_schedule.service.ExaminationScheduleService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/examination-schedules")
public class ExaminationScheduleController {
    private final ExaminationScheduleService examinationScheduleService;
    private final UserService userService;
    
    public ExaminationScheduleController(ExaminationScheduleService examinationScheduleService, UserService userService) {
        this.examinationScheduleService = examinationScheduleService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllExaminationSchedules(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<ExaminationSchedule> examinationScheduleList = examinationScheduleService.getExaminationSchedulesWithPagination(page, pageSize);
        int rows = examinationScheduleService.getExaminationSchedulesRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", examinationScheduleList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VETERINARIAN')")
    @GetMapping("/veterinarian")
    public ResponseEntity<Map<String, Object>> getAllExaminationSchedulesByVeterinarian(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            Principal principal) {
        User vet = userService.getUserByEmail(principal.getName());

        List<ExaminationSchedule> examinationScheduleList = examinationScheduleService.getExaminationSchedulesByVeterinarianWithPagination(vet.getId(), page, pageSize);
        int rows = examinationScheduleService.getExaminationSchedulesByVeterinarianRowsCount(vet.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("data", examinationScheduleList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/by-animal/{id}")
    public ResponseEntity<Map<String, Object>> getAllExaminationSchedulesByAnimal(
            @PathVariable int id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<ExaminationSchedule> examinationScheduleList = examinationScheduleService.getExaminationSchedulesByAnimalWithPagination(id, page, pageSize);
        int rows = examinationScheduleService.getExaminationSchedulesByAnimalRowsCount(id);

        Map<String, Object> response = new HashMap<>();
        response.put("data", examinationScheduleList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/by-status")
    public ResponseEntity<Map<String, Object>> getAllExaminationSchedulesByStatus(
            @RequestParam("status")ExaminationStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<ExaminationSchedule> examinationScheduleList = examinationScheduleService.getExaminationSchedulesByStatusWithPagination(status, page, pageSize);
        int rows = examinationScheduleService.getExaminationSchedulesByStatusRowsCount(status);

        Map<String, Object> response = new HashMap<>();
        response.put("data", examinationScheduleList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VETERINARIAN')")
    @PostMapping
    public ResponseEntity<?> addExaminationSchedule(@RequestBody ExaminationSchedule examinationSchedule,
                                                                      Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        if (user.getRole().equals(Role.VETERINARIAN)) {
            examinationSchedule.setVeterinarian(user);
        }
        ExaminationSchedule createdExaminationSchedule = examinationScheduleService.addExaminationSchedule(examinationSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExaminationSchedule);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VETERINARIAN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateExaminationSchedule(
            @PathVariable int id,
            @RequestBody ExaminationSchedule examinationSchedule,
            Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        ExaminationSchedule examinationScheduleExists = examinationScheduleService.getExaminationScheduleById(id);
        if (user.getRole().equals(Role.VETERINARIAN)) {
            examinationSchedule.setVeterinarian(user);
        }
        if (user.getRole().equals(Role.VETERINARIAN)
                && examinationScheduleExists.getVeterinarian().getId() != user.getId()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Cannot update examination schedule of different vet"));
        }
        ExaminationSchedule updatedExaminationSchedule = examinationScheduleService.updateExaminationScheduleById(examinationSchedule, id);
        return ResponseEntity.ok(updatedExaminationSchedule);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VETERINARIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExaminationSchedule(@PathVariable int id, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        ExaminationSchedule examinationSchedule = examinationScheduleService.getExaminationScheduleById(id);
        if (user.getRole().equals(Role.VETERINARIAN) && examinationSchedule.getVeterinarian().getId() != user.getId()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Cannot delete examination schedule of different vet"));
        }

        examinationScheduleService.deleteExaminationScheduleById(id);
        return ResponseEntity.ok(Map.of("message", String.format("Examination schedule with id %d was successfully deleted", id)));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VETERINARIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<ExaminationSchedule> getExaminationScheduleById(@PathVariable int id) {
        ExaminationSchedule examinationSchedule = examinationScheduleService.getExaminationScheduleById(id);
        return ResponseEntity.ok(examinationSchedule);
    }
}
