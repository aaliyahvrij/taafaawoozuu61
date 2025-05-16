package com.voteU.election.java.controller;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
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
@RequestMapping("/api/election/{year}/authorities/pollingStations")
public class PollingStationController {

    private final PollingStationService pollingStationService;

    public PollingStationController(PollingStationService pollingStationService) {
        this.pollingStationService = pollingStationService;
    }

    @GetMapping
    public Map<String, PollingStation> getPollingStationByYear(@PathVariable String year) {
        return pollingStationService.getPollingStationsByYear(year);
    }

    @GetMapping("/{pollingId}")
    public PollingStation getPollingStationById(@PathVariable String year, @PathVariable String pollingId){
        return pollingStationService.getPollingStationsById(year, pollingId);
    }

    @GetMapping("/{pollingId}/parties")
    public Map<Integer, Party> getPartiesByPollingId(@PathVariable String year, @PathVariable String pollingId){
        return pollingStationService.getPartiesByPollingStationsId(year, pollingId);
    }
}
