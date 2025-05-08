package com.voteU.election.java.services;

import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthorityService {
    private final ElectionService electionService;

    public AuthorityService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public Map<String, Authority> getAllAuthoritiesByElection(String electionId) {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            return null;
        }
        return election.getAuthorities();
    }

    public Map<String, Authority> getAuthoritiesByConstituencyId(String electionId, int constituencyId) {
        Election election = electionService.getElection(electionId);
        Constituency constituency = election.getConstituencies().get(constituencyId);
        if (constituency == null) {
            return null;
        }
        return constituency.getAuthorities();
    }

    public Authority getAuthorityById(String electionId, Integer constituencyId, String authorityId) {
        Map<String, Authority> authorities = getAuthoritiesByConstituencyId(electionId, constituencyId);
        if (authorities == null) {
            return null;
        }
        return authorities.get(authorityId);
    }

    public Map<Integer, Party> getPartiesByAuthorityId(String electionId, Integer constituencyId, String authorityId) {
        Authority authority = getAuthorityById(electionId, constituencyId, authorityId);
        if (authority == null) {
            return null;
        }
        return authority.getAuthorityParties();
    }
}
