package org.university.zoomanagementsystem.animal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.university.zoomanagementsystem.animal.Animal;
import org.university.zoomanagementsystem.animal.enums.AnimalGroup;
import org.university.zoomanagementsystem.animal.enums.HealthStatus;
import org.university.zoomanagementsystem.animal.service.AnimalService;
import org.university.zoomanagementsystem.enclosure.HabitatType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAnimals(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Animal> animalList = animalService.getAnimalsWithPagination(page, pageSize);
        int rows = animalService.getAnimalsRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", animalList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("by-enclosure/{id}")
    public ResponseEntity<Map<String, Object>> getAllAnimalsByEnclosure(
            @PathVariable int id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Animal> animalList = animalService.getAnimalsByEnclosureWithPagination(id, page, pageSize);
        int rows = animalService.getAnimalsByEnclosureRowsCount(id);

        Map<String, Object> response = new HashMap<>();
        response.put("data", animalList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-group")
    public ResponseEntity<Map<String, Object>> getAllAnimalsByGroup(
            @RequestParam("group") AnimalGroup group,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Animal> animalList = animalService.getAnimalsByGroupWithPagination(group, page, pageSize);
        int rows = animalService.getAnimalsByGroupRowsCount(group);

        Map<String, Object> response = new HashMap<>();
        response.put("data", animalList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-habitat")
    public ResponseEntity<Map<String, Object>> getAllAnimalsByHabitat(
            @RequestParam("habitat") HabitatType habitatType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Animal> animalList = animalService.getAnimalsByHabitatWithPagination(habitatType, page, pageSize);
        int rows = animalService.getAnimalsByHabitatRowsCount(habitatType);

        Map<String, Object> response = new HashMap<>();
        response.put("data", animalList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all/by-habitat")
    public ResponseEntity<List<Animal>> getAnimalsByHabitat(
            @RequestParam("habitat") HabitatType habitatType) {

        List<Animal> animalList = animalService.getAllAnimalsByHabitat(habitatType);

        return ResponseEntity.ok(animalList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/by-health")
    public ResponseEntity<Map<String, Object>> getAllAnimalsByHealthStatus(
            @RequestParam("healthStatus") HealthStatus healthStatus,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Animal> animalList = animalService.getAnimalsByHealthStatusWithPagination(healthStatus, page, pageSize);
        int rows = animalService.getAnimalsByByHealthStatusRowsCount(healthStatus);

        Map<String, Object> response = new HashMap<>();
        response.put("data", animalList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Animal> addAnimal(
            @RequestPart("animal") Animal animal,
            @RequestParam(value = "image", required = true) MultipartFile image
    ) throws IOException {
        animal.setImage(animalService.saveAnimalImage(image));
        Animal createdAnimal = animalService.addAnimal(animal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnimal);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Animal> updateAnimal(
            @PathVariable int id,
            @RequestPart("animal") Animal animal,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        animalService.setAnimalForUpdate(animal, image);
        Animal updatedAnimal = animalService.updateAnimalById(animal, id);
        return ResponseEntity.ok(updatedAnimal);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable int id) {
        animalService.deleteAnimalById(id);
        return ResponseEntity.ok(Map.of("message", String.format("Animal with id %d was successfully deleted", id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable int id) {
        Animal animal = animalService.getAnimalById(id);
        return ResponseEntity.ok(animal);
    }
}
