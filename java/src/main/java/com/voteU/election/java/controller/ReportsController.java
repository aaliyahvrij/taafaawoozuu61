package com.voteU.election.java.controller;

import com.voteU.election.java.entities.Reports;
import com.voteU.election.java.services.ReportsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles HTTP requests related to report operations.
 * Acts as the controller layer in an MVC architecture to manage reports.
 * Communicates with the {@code ReportsService} to perform business logic.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    /**
     * Service for handling business logic related to reports.
     * This object is used by the {@code ReportsController} to delegate
     * operations such as retrieving all reports and creating a new report.
     * It interacts with the repository layer to perform persistence-related tasks.
     */
    private final ReportsService reportsService;

    /**
     * Constructs an instance of {@code ReportsController} with the specified {@code ReportsService}.
     *
     * @param reportsService the service layer responsible for managing report operations
     */
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }


    /**
     * Retrieves a list of all reports.
     *
     * @return a ResponseEntity containing a List of Reports objects
     */
    @GetMapping
    public ResponseEntity<List<Reports>> getAllReports() {
        return ResponseEntity.ok(reportsService.getAllReports());
    }


    /**
     * Creates a new report in the system.
     * This method accepts a report object, processes it, and persists it using the service layer.
     *
     * @param report the report object to be created. This includes details such as the reporter,
     *               the reported user, the associated post, the reason, and timestamp.
     * @return a ResponseEntity containing the created report object. The response has an HTTP status of OK.
     */
    @PostMapping
    public ResponseEntity<Reports> createReport(@RequestBody Reports report) {
        return ResponseEntity.ok(reportsService.createReport(report));
    }
}
