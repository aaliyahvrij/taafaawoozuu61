package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.entities.Reports;
import com.voteU.election.java.repositories.ReportsRepository;
import com.voteU.election.java.services.ReportsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportsServiceTest {

    private ReportsRepository reportsRepository;
    private ReportsService reportsService;

    @BeforeEach
    void setUp() {
        reportsRepository = mock(ReportsRepository.class);
        reportsService = new ReportsService(reportsRepository);
    }

    @Test // Happy flow
    void testGetAllReports_returnsReportsList() {
        Reports report1 = new Reports();
        Reports report2 = new Reports();

        when(reportsRepository.findAll()).thenReturn(Arrays.asList(report1, report2));

        List<Reports> result = reportsService.getAllReports();

        assertEquals(2, result.size());
        verify(reportsRepository, times(1)).findAll();
    }

    @Test // Error flow
    void testCreateReport_whenRepositoryThrowsException() {
        Reports report = new Reports();
        when(reportsRepository.save(report)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> reportsService.createReport(report));
        verify(reportsRepository).save(report);
    }

    @Test //  Normale test
    void testGetAllReports_returnsEmptyList() {
        when(reportsRepository.findAll()).thenReturn(Collections.emptyList());

        List<Reports> result = reportsService.getAllReports();

        assertTrue(result.isEmpty());
        verify(reportsRepository).findAll();
    }
}
