package com.voteU.election.java.services;


import ch.qos.logback.core.util.DatePatternToRegexUtil;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.reader.DutchElectionReader;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ElectionService {
    private final DutchElectionReader electionReader;


    public ElectionService(DutchElectionReader electionReader) {
        this.electionReader = electionReader;
    }

    public boolean readResults() {
        return electionReader.readElections();
    }

    public Map<Integer, Party> getParties() {
        return electionReader.getParties();
    }

}
