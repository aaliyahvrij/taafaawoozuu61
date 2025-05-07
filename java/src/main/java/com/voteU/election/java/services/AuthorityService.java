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
        return election.getAuthorities();
    }

    public Authority getAuthorityById(String year, String authorityId) {
        Map<String, Authority> authorities = getAuthoritiesByYear(year);
        if (authorities == null) {
            return null;
        }
        return authorities.get(authorityId);
    }
}
