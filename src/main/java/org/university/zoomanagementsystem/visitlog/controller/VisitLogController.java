package org.university.zoomanagementsystem.visitlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.university.zoomanagementsystem.visitlog.VisitLog;
import org.university.zoomanagementsystem.visitlog.service.VisitLogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('TICKET_AGENT')")
@RequestMapping("/api/visits")
public class VisitLogController {
    private final VisitLogService visitLogService;

    public VisitLogController(VisitLogService visitLogService) {
    this.visitLogService = visitLogService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVisits(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<VisitLog> visitLogList = visitLogService.getVisitLogsWithPagination(page, pageSize);
        int rows = visitLogService.getVisitLogsRowsCount();

        Map<String, Object> response = new HashMap<>();
        response.put("data", visitLogList);
        response.put("currentPage", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", (int) Math.ceil(rows / (float) pageSize));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<VisitLog> addVisit(@RequestBody VisitLog visitLog) {
        VisitLog createdVisitLog = visitLogService.addVisitLog(visitLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVisitLog);
    }
}
