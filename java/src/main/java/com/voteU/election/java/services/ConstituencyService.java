package com.voteU.election.java.services;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConstituencyService {
    private final ElectionService electionService;

    public ConstituencyService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public Map<Integer,Constituency> getConstituencies(String year) {
        Election election = electionService.getElection(year);
        if (election == null) {
            return null;
        }

        // Assuming Election has a Map<Integer, Constituency> field (like getConstituencies())
        Map<Integer, Constituency> constituencies = election.getConstituencies();
        if (constituencies == null) {
            return null;
        }

        return constituencies;
    }
}
