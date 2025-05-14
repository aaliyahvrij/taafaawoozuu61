package com.voteU.election.java.services;

import com.voteU.election.java.model.*;
import com.voteU.election.java.reader.DutchElectionReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class ElectionService {
    private final DutchElectionReader electionReader;
    private final Map<String, Election> electionsByElectionId = new HashMap<>();

    public ElectionService(DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    public boolean readElections() {
        Map<String, Election> elections = electionReader.getAll();
        if (elections == null || elections.isEmpty()) {
            log.warn("No election data found during readElections().");
            return false;
        }
        electionsByElectionId.putAll(elections);
        return true;
    }

    public boolean readElection(String electionId) {
        Election election = electionReader.getElection(electionId);
        if (election == null) {
            log.warn("No election data found for year {} during readElectionYear().", electionId);
            return false;
        }
        electionsByElectionId.put(electionId, election);
        return true;
    }

    public Map<String, Election> getAll() {
        return electionsByElectionId;
    }

    public Election getElection(String electionId) {
        return electionsByElectionId.get(electionId);
    }

    public Map<Integer, Party> getAllPartiesByElection(String electionId) {
        Election election = getElection(electionId);
        if (election == null) {
            return null;
        }
        return election.getNationalParties();
    }
}
