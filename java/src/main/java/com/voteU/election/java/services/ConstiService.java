package com.voteU.election.java.services;

import com.voteU.election.java.models.Constituency;
import com.voteU.election.java.utils.xml.ElectionReader;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConstiService {
    private ElectionReader electionReader;
    private final Map<String, Map<Integer, Constituency>> storedElections = new HashMap<>();

    public void ElectionService(ElectionReader electionReader) {
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
    public Map<String, Map<Integer, Constituency>> getElections() {
        return storedElections;
    }

    // TODO: getConsistuency method
}
