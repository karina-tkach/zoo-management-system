package org.university.zoomanagementsystem.gate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.gate.Gate;
import org.university.zoomanagementsystem.gate.service.GateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('TICKET_AGENT')")
@RequestMapping("/api/gates")
public class GateController {
    private final GateService gateService;

    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllGates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int pageSize) {

        List<Gate> gatesList = gateService.getGatesWithPagination(page, pageSize);
        int rows = gateService.getGatesRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", gatesList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Gate> addGate(@RequestBody Gate gate) {
        Gate createdGate = gateService.addGate(gate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGate);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Gate> updateGate(@PathVariable int id, @RequestBody Gate gate) {
        Gate updatedGate = gateService.updateGateById(gate, id);
        return ResponseEntity.ok(updatedGate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGate(@PathVariable int id) {
        gateService.deleteGateById(id);
        return ResponseEntity.ok(Map.of("message", String.format("Gate with id %d was successfully deleted", id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gate> getGateById(@PathVariable int id) {
        Gate gate = gateService.getGateById(id);
        return ResponseEntity.ok(gate);
    }
}
