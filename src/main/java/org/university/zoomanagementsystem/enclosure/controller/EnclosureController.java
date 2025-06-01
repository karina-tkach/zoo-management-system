package org.university.zoomanagementsystem.enclosure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.animal.service.AnimalService;
import org.university.zoomanagementsystem.enclosure.Enclosure;
import org.university.zoomanagementsystem.enclosure.HabitatType;
import org.university.zoomanagementsystem.enclosure.service.EnclosureService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/enclosures")
public class EnclosureController {
    private final EnclosureService enclosureService;
    private final AnimalService animalService;

    public EnclosureController(EnclosureService enclosureService, AnimalService animalService) {
        this.enclosureService = enclosureService;
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEnclosures(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Enclosure> enclosureList = enclosureService.getEnclosuresWithPagination(page, pageSize);
        int rows = enclosureService.getEnclosuresRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", enclosureList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Enclosure> addEnclosure(@RequestBody Enclosure enclosure) {
        Enclosure createdEnclosure = enclosureService.addEnclosure(enclosure);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEnclosure);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Enclosure> updateEnclosure(@PathVariable int id,
                                                     @RequestBody Enclosure enclosure) {
        Enclosure updatedEnclosure = enclosureService.updateEnclosureById(enclosure, id);
        return ResponseEntity.ok(updatedEnclosure);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnclosure(@PathVariable int id) {
        int count = animalService.getAnimalsByEnclosureRowsCount(id);
        if (count > 0) {
            return ResponseEntity.badRequest().body(Map.of("message", String.format("Enclosure with id %d has animals, delete them firstly", id)));
        }
        enclosureService.deleteEnclosureById(id);
        return ResponseEntity.ok(Map.of("message", String.format("Enclosure with id %d was successfully deleted", id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enclosure> getEnclosureById(@PathVariable int id) {
        Enclosure enclosure = enclosureService.getEnclosureById(id);
        return ResponseEntity.ok(enclosure);
    }

    @GetMapping("/by-environment")
    public ResponseEntity<List<Enclosure>> getEnclosuresByEnvironmentType(@RequestParam("type") HabitatType type) {
        List<Enclosure> enclosures = enclosureService.getEnclosuresByEnvironmentType(type);
        return ResponseEntity.ok(enclosures);
    }
}
