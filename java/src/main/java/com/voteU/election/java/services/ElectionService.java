package com.voteU.election.java.services;

import com.voteU.election.java.models.*;
import com.voteU.election.java.utils.xml.ElectionReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ElectionService {
    private final ElectionReader electionReader;
    private static final Map<String, Election> electionListMap = new HashMap<>();

    public ElectionService(ElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    /**
     * Loads all elections from the reader and stores them internally.
     * This is intended to be called via a POST-like operation.
     *
     * @return true if elections were loaded successfully, false otherwise
     */
    public boolean readElections() {
        Map<String, Election> elections = electionReader.getAllElectoralLevelData();
        if (elections == null || elections.isEmpty()) {
            log.warn("No election data found during readElections().");
            return false;
        }
        electionListMap.putAll(elections);
        return true;
    }

    /**
     * Checks if a specific election year is available in memory.
     *
     * @param electionId the ID of the election (e.g. "TK2021")
     * @return true if found, false otherwise
     */
    public boolean readElection(String electionId) {
        return electionListMap.containsKey(electionId);
    }

    /**
     * Retrieves all the data of all the elections.
     */
    public Map<String, Election> getAll() {
        return electionListMap;
    }

    /**
     * Retrieves all the data of a specific election.
     */
    public Election getElectoralLevelDataOf(String electionId) {
        return electionListMap.get(electionId);
    }

    /**
     * Retrieves all the affiliation data of a specific election.
     */
    public Map<Integer, Affiliation> getElectoralLevel_affiliationsOf(String electionId) {
        Election election = getElectoralLevelDataOf(electionId);
        if (election == null) {
            return null;
        }
        return election.getAffiliations();
    }

    /**
     * Retrieves all the polling station data of a specific election.
     */
    public Map<String, PollingStation> getElectoralLevel_pollingStationsOf(String electionId) {
        Election election = getElectoralLevelDataOf(electionId);
        if (election == null) {
            return null;
        }
        return election.getPollingStations();
    }
}
