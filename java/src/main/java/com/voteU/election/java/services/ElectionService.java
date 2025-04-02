package com.voteU.election.java.services;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Contest;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.reader.DutchElectionReader;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ElectionService implements IElectionService {
    private final DutchElectionReader electionReader;
    private final Map<String, Map<Integer, Contest>> storedElections = new HashMap<>();

    public ElectionService(DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    /**
     * Reads and stores election results (POST equivalent).
     * This method processes the election data and stores it in memory.
     *
     * @return true if the elections were successfully processed and stored, false otherwise.
     */
    public boolean readElections() {
        Map<String, Map<Integer, Contest>> elections = electionReader.getElections();
        if (elections.isEmpty()) {
            return false;
        }
        storedElections.putAll(elections);
        return true;
    }

    /**
     * Retrieves the stored election data (GET equivalent).
     *
     * @return A map containing election results grouped by year.
     */
    public Map<String, Map<Integer, Contest>> getElections() {
        return storedElections;
    }

    @Override
    public Party[] getAll() {
        return new Party[0];
    }
}
