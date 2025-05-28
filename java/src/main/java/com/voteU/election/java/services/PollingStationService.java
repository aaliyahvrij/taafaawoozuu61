package com.voteU.election.java.services;

import com.voteU.election.java.models.PollingStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class PollingStationService {
    private final ElectionService electionService;

    public PollingStationService(ElectionService electionService) {
        this.electionService = electionService;
    }

    /**
     * Retrieves all stored polling stations (GET).
     */
    public Map<String, PollingStation> getElectionLevel_pollingStations(String electionId) {
        System.out.println("The amount of polling stations: " + electionService.getElection(electionId).getPollingStations().size());
        return electionService.getElection(electionId).getPollingStations();
    }
}
