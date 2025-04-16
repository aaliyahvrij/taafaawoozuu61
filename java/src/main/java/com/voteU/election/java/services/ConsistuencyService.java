package com.voteU.election.java.services;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.reader.DutchElectionReader;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConsistuencyService {

    private final DutchElectionReader electionReader;
    private final Map<String, Map<Integer, Constituency>> storedElections = new HashMap<>();

    public ConsistuencyService (DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    /**
     * Reads and stores election results (POST equivalent).
     * This method processes the election data and stores it in memory.
     *
     * @return true if the elections were successfully processed and stored, false otherwise.
     */
    public boolean readElections() {
        //electionReader.getAll();//
        Map<String, Map<Integer, Constituency>> elections = electionReader.getConstituencies();
        if (elections == null || elections.isEmpty())  {
            System.out.println("Geen verkiezingsdata gevonden!");
            return false;
        }
        storedElections.putAll(elections);
        System.out.println("Elections opgeslagen: " + storedElections.keySet());
        return true;
    }


    /**
     * Retrieves the stored election data (GET equivalent).
     *
     * @return A map containing election results grouped by year.
     */
    public Map<String, Map<Integer, Constituency>> getElections() {
        return storedElections;
    }

    //Todo: getConsistuency function
    public Constituency getConstituency(String year, int constituencyId) {
        Map<Integer, Constituency> constituencys = storedElections.get(year);
        return (constituencys != null) ? constituencys.get(constituencyId) : null;
    }
}
