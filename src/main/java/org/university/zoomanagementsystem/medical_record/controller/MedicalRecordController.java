package org.university.zoomanagementsystem.medical_record.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.university.zoomanagementsystem.medical_record.MedicalRecord;
import org.university.zoomanagementsystem.medical_record.service.MedicalRecordService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMedicalRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<MedicalRecord> medicalRecordList = medicalRecordService.getMedicalRecordsWithPagination(page, pageSize);
        int rows = medicalRecordService.getMedicalRecordsRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", medicalRecordList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-date")
    public ResponseEntity<Map<String, Object>> getAllMedicalRecordsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<MedicalRecord> medicalRecordList = medicalRecordService.getMedicalRecordsByDateWithPagination(date, page, pageSize);
        int rows = medicalRecordService.getMedicalRecordsByDateRowsCount(date);

        Map<String, Object> response = new HashMap<>();
        response.put("data", medicalRecordList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }
}
