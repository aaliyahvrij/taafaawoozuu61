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
        Map<String, Election> elections = electionReader.getAll();
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
     * Retrieves all the data of all the elections (GET).
     */
    public Map<String, Election> getAll() {
        return electionListMap;
    }

    /**
     * Retrieves all the data of a specific election (GET).
     */
    public Election getElection(String electionId) {
        return electionListMap.get(electionId);
    }

    /**
     * Retrieves all affiliations by electionId (GET).
     */
    public Map<Integer, Affiliation> getElectionLevel_affiliations(String electionId) {
        Election election = getElection(electionId);
        if (election == null) {
            return null;
        }
        return election.getAffiliations();
    }

    /**
     * Retrieves all polling stations by electionId (GET).
     */
    public Map<String, PollingStation> getElectionLevel_pollingStations(String electionId) {
        Election election = getElection(electionId);
        if (election == null) {
            return null;
        }
        return election.getPollingStations();
    }
}
