package com.voteU.election.java.services;

import com.voteU.election.java.entities.Reports;
import com.voteU.election.java.repositories.ReportsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsService {

    /**
     * Repository layer dependency for managing database operations related to the Reports entity.
     * This field is injected and used by the ReportsService to perform CRUD operations on reports.
     */
    private final ReportsRepository reportsRepository;

    /**
     * Constructs a new instance of the ReportsService class.
     *
     * @param reportsRepository the ReportsRepository instance used for accessing and managing
     *                           Reports data in the database
     */
    public ReportsService(ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }

    /**
     * Retrieves all the reports from the repository.
     *
     * @return a list of all reports available in the repository
     */
    public List<Reports> getAllReports() {
        return reportsRepository.findAll();
    }

    /**
     * Creates and saves a report to the repository.
     *
     * @param report the report object to be created and saved
     * @return the saved report object
     */
    public Reports createReport(Reports report) {
        return reportsRepository.save(report);
    }
}
