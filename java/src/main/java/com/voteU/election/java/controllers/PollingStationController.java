package com.voteU.election.java.controllers;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.services.PollingStationService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/{electionId}/pollingstations")
public class PollingStationController {
    private final PollingStationService pollingStationService;

    public PollingStationController(PollingStationService pollingStationService) {
        this.pollingStationService = pollingStationService;
    }

    @GetMapping
    public Map<String, PollingStation> getELectionLevel_pollingStations(@PathVariable String electionId) {
        return pollingStationService.getElectionLevel_pollingStations(electionId);
    }
}
