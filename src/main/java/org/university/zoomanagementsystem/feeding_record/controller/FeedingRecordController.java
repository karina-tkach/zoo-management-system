package org.university.zoomanagementsystem.feeding_record.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.university.zoomanagementsystem.feeding_record.FeedingRecord;
import org.university.zoomanagementsystem.feeding_record.service.FeedingRecordService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/feeding-records")
public class FeedingRecordController {
    private final FeedingRecordService feedingRecordService;

    public FeedingRecordController(FeedingRecordService feedingRecordService) {
        this.feedingRecordService = feedingRecordService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFeedingRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<FeedingRecord> feedingRecordList = feedingRecordService.getFeedingRecordsWithPagination(page, pageSize);
        int rows = feedingRecordService.getFeedingRecordsRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", feedingRecordList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-date")
    public ResponseEntity<Map<String, Object>> getAllFeedingRecordsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<FeedingRecord> feedingRecordList = feedingRecordService.getFeedingRecordsByDateWithPagination(date, page, pageSize);
        int rows = feedingRecordService.getFeedingRecordsByDateRowsCount(date);

        Map<String, Object> response = new HashMap<>();
        response.put("data", feedingRecordList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }
}
