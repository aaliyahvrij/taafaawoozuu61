package com.voteU.election.java.services;

import com.voteU.election.java.model.PollingStation;
import com.voteU.election.java.reader.DutchElectionReader;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PollingStationService {

    private final DutchElectionReader electionReader;
    private final Map<String, Map<String, PollingStation>> storedPollingStations = new HashMap<>();

    public PollingStationService(DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    /**
     * Reads and stores polling station results (POST equivalent).
     * This method processes the election data and stores polling stations in memory.
     *
     * @return true if polling station data was successfully processed and stored, false otherwise.
     */
    public boolean readPollingStations() {
        electionReader.getAll();

        Map<String, Map<String, PollingStation>> pollingStations = electionReader.getPollingStations();
        System.out.println("Polling station map size: " + pollingStations.size());
        System.out.println("Years (keys): " + pollingStations.keySet());

        if (pollingStations == null || pollingStations.isEmpty()) {
            System.out.println("No polling stations found!");
            return false;
        }

        storedPollingStations.putAll(pollingStations);
        System.out.println("Polling stations saved: " + storedPollingStations.keySet());
        return true;
    }

    /**
     * Retrieves the stored polling station data (GET equivalent).
     *
     * @return A map containing polling station results grouped by year.
     */
    public Map<String, Map<String, PollingStation>> getStoredPollingStations() {
        return storedPollingStations;
    }

    /**
     * Retrieves a specific polling station based on election year and polling station ID.
     *
     * @param year            The election year.
     * @param pollingStationId The unique ID of the polling station.
     * @return The {@link PollingStation} if found, or null otherwise.
     */
    public PollingStation getPollingStation(String year, String pollingStationId) {
        Map<String, PollingStation> stationsForYear = storedPollingStations.get(year);
        return (stationsForYear != null) ? stationsForYear.get(pollingStationId) : null;
    }
}

