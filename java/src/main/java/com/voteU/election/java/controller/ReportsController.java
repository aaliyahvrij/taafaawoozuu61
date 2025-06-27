package com.voteU.election.java.controller;

import com.voteU.election.java.entities.Reports;
import com.voteU.election.java.services.ReportsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    /**
     * Haal alle rapporten op (alleen voor admins).
     */
    @GetMapping
    public ResponseEntity<List<Reports>> getAllReports() {
        return ResponseEntity.ok(reportsService.getAllReports());
    }

    /**
     * Maak een nieuw rapport aan.
     */
    @PostMapping
    public ResponseEntity<Reports> createReport(@RequestBody Reports report) {
        return ResponseEntity.ok(reportsService.createReport(report));
    }
}
