package com.voteU.election.java.services;

import com.voteU.election.java.model.Contest;
import com.voteU.election.java.reader.DutchElectionReader;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class ConsistuencyService {

    private DutchElectionReader electionReader;
    private final Map<String, Map<Integer, Contest>> storedElections = new HashMap<>();

    public void ElectionService(DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    /**
     * Reads and stores election results (POST equivalent).
     * This method processes the election data and stores it in memory.
     *
     * @return true if the elections were successfully processed and stored, false otherwise.
     */
    public boolean readElections() {
        return false;
    }

    /**
     * Retrieves the stored election data (GET equivalent).
     *
     * @return A map containing election results grouped by year.
     */
    public Map<String, Map<Integer, Contest>> getElections() {
        return storedElections;
    }

    //Todo: getConsistuency function
}
