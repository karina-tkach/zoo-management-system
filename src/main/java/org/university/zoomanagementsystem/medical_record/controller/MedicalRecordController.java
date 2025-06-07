package org.university.zoomanagementsystem.medical_record.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.medical_record.MedicalRecord;
import org.university.zoomanagementsystem.medical_record.service.MedicalRecordService;
import org.university.zoomanagementsystem.user.User;
import org.university.zoomanagementsystem.user.service.UserService;
import org.university.zoomanagementsystem.vet_examination_schedule.ExaminationSchedule;
import org.university.zoomanagementsystem.vet_examination_schedule.service.ExaminationScheduleService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    private final UserService userService;
    private final ExaminationScheduleService examinationScheduleService;
    
    public MedicalRecordController(MedicalRecordService medicalRecordService,
                                   UserService userService,
                                   ExaminationScheduleService examinationScheduleService) {
        this.medicalRecordService = medicalRecordService;
        this.userService = userService;
        this.examinationScheduleService = examinationScheduleService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('VETERINARIAN')")
    @GetMapping("/veterinarian")
    public ResponseEntity<Map<String, Object>> getAllMedicalRecordsByVeterinarian(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            Principal principal) {
        User vet = userService.getUserByEmail(principal.getName());

        List<MedicalRecord> medicalRecordList = medicalRecordService.getMedicalRecordsByVetWithPagination(vet.getId(), page, pageSize);
        int rows = medicalRecordService.getMedicalRecordsByVetRowsCount(vet.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("data", medicalRecordList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('VETERINARIAN')")
    @PostMapping
    public ResponseEntity<?> addMedicalRecord(@RequestBody MedicalRecord medicalRecord, Principal principal) {
        User vet = userService.getUserByEmail(principal.getName());
        ExaminationSchedule examinationSchedule = examinationScheduleService.getExaminationScheduleById(medicalRecord.getExaminationSchedule().getId());
        if (examinationSchedule.getVeterinarian().getId() != vet.getId()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Cannot set record to examination of different vet"));
        }

        MedicalRecord createdMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMedicalRecord);
    }
}
