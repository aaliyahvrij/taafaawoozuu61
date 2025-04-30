package com.voteU.election.java.services;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.reader.DutchElectionReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
public class ElectionService {

    private final DutchElectionReader electionReader;
    private final Map<String, Election> electionsByYear = new HashMap<>();

    public ElectionService(DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }


    public boolean readElections() {
        Map<String, Election> elections = electionReader.getAll();
        if (elections == null || elections.isEmpty()) {
            log.warn("No election data found during readElections().");
            return false;
        }
        electionsByYear.putAll(elections);
        return true;
    }


    public boolean readElectionYear(String electionYear) {
        return electionsByYear.containsKey(electionYear);
    }


    public Map<String, Election> getAll() {
        return electionsByYear;
    }

    public Election getElectionByYear(String electionYear) {
        return electionsByYear.get(electionYear);
    }


}
