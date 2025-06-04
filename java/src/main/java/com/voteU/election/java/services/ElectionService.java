package com.voteU.election.java.services;

import com.voteU.election.java.models.*;
import com.voteU.election.java.utils.xml.ElectionReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Slf4j
@Service
public class ElectionService {
    private final ElectionReader electionReader;
    private static final LinkedHashMap<String, Election> electionListMap = new LinkedHashMap<>();

    public ElectionService(ElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    /**
     * Loads all elections from the reader and stores them internally.
     * This is intended to be called via a POST-like operation.
     *
     * @return true if elections were loaded successfully, false otherwise
     */
    public boolean readAllElectoralLevelData() {
        LinkedHashMap<String, Election> readerElectionListMap = this.electionReader.getAllElectoralLevelData();
        if (readerElectionListMap == null || readerElectionListMap.isEmpty()) {
            log.warn("No election data found during readAllElectoralLevelData().");
            return false;
        }
        electionListMap.putAll(readerElectionListMap);
        return true;
    }

    /**
     * Checks if a specific election year is available in memory.
     *
     * @param electionId the ID of the election (e.g. "TK2021")
     * @return true if found, false otherwise
     */
    public boolean readElectoralLevelDataOf(String electionId) {
        return electionListMap.containsKey(electionId);
    }

    /**
     * Retrieves all the data of all the elections.
     */
    public LinkedHashMap<String, Election> getAllElectoralLevelData() {
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
    public LinkedHashMap<Integer, Affiliation> getElectoralLevel_affiliationsOf(String electionId) {
        Election election = getElectoralLevelDataOf(electionId);
        if (election == null) {
            return null;
        }
        return election.getAffiliations();
    }

    /**
     * Retrieves all the polling station data of a specific election.
     */
    public LinkedHashMap<String, PollingStation> getElectoralLevel_pollingStationsOf(String electionId) {
        Election election = getElectoralLevelDataOf(electionId);
        if (election == null) {
            return null;
        }
        return election.getPollingStations();
    }
}
