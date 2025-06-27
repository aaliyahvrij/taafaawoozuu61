package com.voteU.election.java.services;

import com.voteU.election.java.entities.Reports;
import com.voteU.election.java.repositories.ReportsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsService {

    private final ReportsRepository reportsRepository;

    public ReportsService(ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }

    public List<Reports> getAllReports() {
        return reportsRepository.findAll();
    }

    public Reports createReport(Reports report) {
        return reportsRepository.save(report);
    }
}
