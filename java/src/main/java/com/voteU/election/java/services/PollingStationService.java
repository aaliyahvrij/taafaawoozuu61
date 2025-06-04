package com.voteU.election.java.services;

import com.voteU.election.java.models.PollingStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

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
    public LinkedHashMap<String, PollingStation> getElectoralLevel_pollingStationsOf(String electionId) {
        return this.electionService.getElectoralLevelDataOf(electionId).getPollingStations();
    }
}
