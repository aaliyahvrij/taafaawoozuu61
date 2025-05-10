package com.voteU.election.java.controller;

import com.voteU.election.java.model.PollingStation;
import com.voteU.election.java.services.PollingStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for managing and accessing polling station election results.
 *
 * Provides endpoints to retrieve all polling stations, retrieve a specific polling station by year and ID,
 * and trigger reading/parsing of polling station election data into memory.
 */
@RestController
@RequestMapping("/api/pollingStation")
public class PollingStationController {

    private final PollingStationService pollingStationService;

    public PollingStationController(PollingStationService pollingStationService) {
        this.pollingStationService = pollingStationService;
    }

    /**
     * Retrieves a specific polling station by year and polling station ID.
     *
     * @param year        The election year (e.g., "2024").
     * @param pollingId   The unique identifier of the polling station.
     * @return The {@link PollingStation} object if found, or null if not found.
     */
    @GetMapping("/{year}/{pollingId}")
    public PollingStation getPollingStationById(@PathVariable String year, @PathVariable String pollingId) {
        return pollingStationService.getPollingStation(year, pollingId);
    }

    /**
     * Retrieves all stored polling station results grouped by election year.
     *
     * @return A {@link ResponseEntity} containing a map of all polling station data.
     */
    @GetMapping
    public ResponseEntity<Map<String, Map<String, PollingStation>>> getPollingStations() {
        return ResponseEntity.ok(pollingStationService.getStoredPollingStations());
    }

    /**
     * Reads and processes polling station election results from the source.
     * This is a trigger endpoint typically called once to load data into memory.
     *
     * @return true if data was successfully read and stored, false otherwise.
     */
    @PostMapping
    public boolean readResults() {
        return pollingStationService.readPollingStations();
    }
}
