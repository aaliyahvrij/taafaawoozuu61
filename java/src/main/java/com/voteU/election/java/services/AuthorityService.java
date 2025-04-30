package com.voteU.election.java.services;

import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.Election;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthorityService {
    private final ElectionService electionService;

    public AuthorityService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public Map<String, Authority> getAuthoritiesByYear(String year) {
        Election election = electionService.getElectionByYear(year);
        if (election == null) {
            return null;
        }
        // Assuming Election has a Map<Integer, Constituency> field (like getConstituencies())
        Map<String, Authority> authorities= election.getAuthorities();
        if (authorities == null) {
            return null;
        }

        return authorities;
    }
}
