package com.voteU.election.java.services;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.PollingStation;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class PollingStationService {

    private final ElectionService electionService;

    public PollingStationService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public Map<String, PollingStation> getPollingStationsByYear(String year) {
        Election election = electionService.getElection(year);
        if (election == null){
            return null;
        }
        Map<String, PollingStation> pollingStations = election.getPollingStations();
        if (pollingStations == null || pollingStations.isEmpty()) {
            System.out.println("No polling stations found for election year: " + year);
            return null;
        }
        return pollingStations;
    }

    public PollingStation getPollingStationsById(String year, String pollingId) {
        Map<String, PollingStation> pollingStations = getPollingStationsByYear(year);
        if (pollingStations == null){
            return null;
        }
        return pollingStations.get(pollingId);
    }

    public Map<Integer, Party> getPartiesByPollingStationsId(String year, String pollingId) {
        PollingStation pollingStation = getPollingStationsById(year, pollingId);
        if (pollingStation == null){
            return null;
        }
        return pollingStation.getParties();
    }
}

