package com.voteU.election.java.services;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.reader.DutchElectionReader;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConsistuencyService {

    private final DutchElectionReader electionReader;
    private final Map<String, Map<Integer, Constituency>> storedConstituencies = new HashMap<>();

    public ConsistuencyService (DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    /**
     * Reads and stores election results (POST equivalent).
     * This method processes the election data and stores it in memory.
     *
     * @return true if the elections were successfully processed and stored, false otherwise.
     */
    public boolean readConstituencies() {
        electionReader.getAll();

        Map<String, Map<Integer, Constituency>> constituencies = electionReader.getConstituencies();
        System.out.println("Constituency map size: " + constituencies.size());
        System.out.println("Keys: " + constituencies.keySet());

        if (constituencies == null || constituencies.isEmpty())  {
            System.out.println("no constituencies found!");
            return false;
        }
        storedConstituencies.putAll(constituencies);
        System.out.println("Constituencies saved: " + storedConstituencies.keySet());
        return true;
    }



    /**
     * Retrieves the stored constituency data (GET equivalent).
     *
     * @return A map containing constituency results grouped by year.
     */
    public Map<String, Map<Integer, Constituency>> getStoredConstituencies() {
        return storedConstituencies;
    }

    public Constituency getConstituency(String year, int constituencyId) {
        Map<Integer, Constituency> constituencys = storedConstituencies.get(year);
        return (constituencys != null) ? constituencys.get(constituencyId) : null;
    }
}
