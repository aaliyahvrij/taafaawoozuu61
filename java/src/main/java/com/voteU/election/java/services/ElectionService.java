package com.voteU.election.java.services;

import com.voteU.election.java.CompactDTO.CompactElection;
import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.model.*;
import com.voteU.election.java.reader.DutchElectionReader;
import com.voteU.election.java.repositories.electiondata.ElectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class ElectionService {
    private final DutchElectionReader electionReader;
    private final Map<String, Election> electionsByElectionId = new HashMap<>();
    private final ElectionRepository electionRepository;

    public ElectionService(DutchElectionReader electionReader, ElectionRepository electionRepository) {
        this.electionReader = electionReader;
        this.electionRepository = electionRepository;
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

    public CompactElection getCompactElection(String electionId){
        Election election = getElection(electionId);
        String id = election.getId();
        String name = election.getName();
        int votes = election.getVotes();
        Map<Integer, Party> parties = election.getParties();
        int size = parties.size();

        return new CompactElection(id, name, votes, size);
    }

    public Map<Integer, Party> getAllPartiesByElection(String electionId) {
        Election election = getElection(electionId);
        if (election == null) {
            return null;
        }
        return election.getParties();
    }

    public List<DropdownOptionDTO<String>> getElectionNames() {
        return electionRepository.getElectionNames();
    }
}
