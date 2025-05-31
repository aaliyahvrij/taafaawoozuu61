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
     * Retrieves all the polling station data of a specific election.
     */
    public Map<String, PollingStation> getElectoralLevel_pollingStationsOf(String electionId) {
        System.out.println("The amount of polling stations: " + electionService.getElectoralLevelDataOf(electionId).getPollingStations().size());
        return electionService.getElectoralLevelDataOf(electionId).getPollingStations();
    }
}
