package org.university.zoomanagementsystem.staff.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.staff.StaffDTO;
import org.university.zoomanagementsystem.staff.service.StaffService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStaff(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<StaffDTO> staffList = staffService.getStaffWithPagination(page, pageSize);
        int rows = staffService.getStaffRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", staffList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<StaffDTO> addStaff(@RequestBody StaffDTO dto) {
        StaffDTO createdStaff = staffService.addStaff(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStaff);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StaffDTO> updateStaff(@PathVariable int id, @RequestBody StaffDTO dto) {
        StaffDTO updatedStaff = staffService.updateStaffById(dto, id);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable int id) {
        staffService.deleteStaffById(id);
        return ResponseEntity.ok(Map.of("message", String.format("Staff with id %d was successfully deleted", id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable int id) {
        StaffDTO staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }
}
