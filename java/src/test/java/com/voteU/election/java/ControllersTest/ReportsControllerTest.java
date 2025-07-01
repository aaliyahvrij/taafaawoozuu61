package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.controller.ReportsController;
import com.voteU.election.java.entities.Reports;
import com.voteU.election.java.services.ReportsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportsControllerTest {

    private ReportsService reportsService;
    private ReportsController reportsController;

    @BeforeEach
    void setUp() {
        reportsService = mock(ReportsService.class);
        reportsController = new ReportsController(reportsService);
    }

    @Test // Happy flow
    void testGetAllReports_returnsReportList() {
        Reports report1 = new Reports();
        Reports report2 = new Reports();

        when(reportsService.getAllReports()).thenReturn(Arrays.asList(report1, report2));

        ResponseEntity<List<Reports>> response = reportsController.getAllReports();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test //  Normale test
    void testCreateReport_returnsCreatedReport() {
        Reports report = new Reports();
        when(reportsService.createReport(report)).thenReturn(report);

        ResponseEntity<Reports> response = reportsController.createReport(report);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(report, response.getBody());
    }

    @Test //  Error
    void testCreateReport_withNull_shouldStillReturnResponse() {
        when(reportsService.createReport(null)).thenReturn(null);

        ResponseEntity<Reports> response = reportsController.createReport(null);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
