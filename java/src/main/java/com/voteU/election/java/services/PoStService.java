package com.voteU.election.java.services;

import com.voteU.election.java.models.PollingStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Slf4j
@Service
public class PoStService {
    private final ElectionService electionService;

    public PoStService(ElectionService electionService) {
        this.electionService = electionService;
    }

    /**
     * Retrieves all the polling station data of a specific election.
     */
    public LinkedHashMap<String, PollingStation> getNationalLevel_pollingStationsOf(String electionId) {
        return this.electionService.getElectoralDataOf(electionId).getPollingStations();
    }
}
