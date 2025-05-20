package org.university.zoomanagementsystem.excursion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.excursion.Excursion;
import org.university.zoomanagementsystem.excursion.service.ExcursionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excursions")
public class ExcursionController {
    private final ExcursionService excursionService;

    public ExcursionController(ExcursionService excursionService) {
        this.excursionService = excursionService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllExcursions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Excursion> excursionList = excursionService.getExcursionsWithPagination(page, pageSize);
        int rows = excursionService.getExcursionsRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", excursionList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    @PostMapping
    public ResponseEntity<Excursion> addExcursion(@RequestBody Excursion excursion) {
        Excursion createdExcursion = excursionService.addExcursion(excursion);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExcursion);
    }

    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Excursion> updateExcursion(@PathVariable int id, @RequestBody Excursion excursion) {
        Excursion updatedExcursion = excursionService.updateExcursionById(excursion, id);
        return ResponseEntity.ok(updatedExcursion);
    }

    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable int id) {
        excursionService.deleteExcursionById(id);
        return ResponseEntity.ok(Map.of("message", String.format("Excursion with id %d was successfully deleted", id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Excursion> getExcursionById(@PathVariable int id) {
        Excursion excursion = excursionService.getExcursionById(id);
        return ResponseEntity.ok(excursion);
    }
}
