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
     * Calls upon the reader to call upon the processor to retrieve all the electoral data from the XML files.
     *
     * @return true if the data has been loaded successfully, false otherwise
     */
    public boolean readAllElectoralData() {
        LinkedHashMap<String, Election> readerElectionListMap = this.electionReader.getAllElectoralData();
        if (readerElectionListMap == null || readerElectionListMap.isEmpty()) {
            log.warn("No electoral data found during readAllElectoralData().");
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
    public boolean readElectoralDataOf(String electionId) {
        return electionListMap.containsKey(electionId);
    }

    /**
     * Retrieves all the data of all the elections.
     */
    public LinkedHashMap<String, Election> getAllElectoralData() {
        return electionListMap;
    }

    /**
     * Retrieves all the data of a specific election.
     */
    public Election getElectoralDataOf(String electionId) {
        return electionListMap.get(electionId);
    }

    /**
     * Retrieves all the affiliation data of a specific election.
     */
    public LinkedHashMap<Integer, Affiliation> getNationalLevel_affiListMap(String electionId) {
        Election election = getElectoralDataOf(electionId);
        if (election == null) {
            return null;
        }
        return election.getAffiListMap();
    }

    /**
     * Retrieves all the polling station data of a specific election.
     */
    public LinkedHashMap<String, PollingStation> getNationalLevel_poStListMap(String electionId) {
        Election election = getElectoralDataOf(electionId);
        if (election == null) {
            return null;
        }
        return election.getPoStListMap();
    }
}
