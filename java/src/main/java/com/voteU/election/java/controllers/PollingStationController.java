package com.voteU.election.java.controllers;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.services.PollingStationService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/{electionId}/pollingstations")
public class PollingStationController {
    private final PollingStationService pollingStationService;

    public PollingStationController(PollingStationService pollingStationService) {
        this.pollingStationService = pollingStationService;
    }

    @GetMapping
    public LinkedHashMap<String, PollingStation> getElectoralLevel_pollingStationsOf(@PathVariable String electionId) {
        return this.pollingStationService.getElectoralLevel_pollingStationsOf(electionId);
    }
}
