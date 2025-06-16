package com.voteU.election.java.services;

import com.voteU.election.java.exceptions.AccessDeniedException;
import com.voteU.election.java.exceptions.ResourceAlreadyExistsException;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.model.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorityService {
    private final ElectionService electionService;

    public AuthorityService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public Map<String, Authority> getAuthoritiesByConstituencyId(String electionId, int constituencyId) {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            throw new ResourceNotFoundException("Election with ID " + electionId + " not found");
        }
        Constituency constituency = election.getConstituencies().get(constituencyId);
        if (constituency == null) {
            throw new ResourceNotFoundException("Constituency with ID " + constituencyId + " not found in election " + electionId);
        }
        return constituency.getAuthorities();
    }

    public Map<String, Authority> getAuthoritiesByConstituencyIdCompact(String electionId, int constituencyId) {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            throw new ResourceNotFoundException("Election with ID " + electionId + " not found");
        }
        Constituency constituency = election.getConstituencies().get(constituencyId);
        if (constituency == null) {
            throw new ResourceNotFoundException("Constituency with ID " + constituencyId + " not found in election " + electionId);
        }

        Map<String, Authority> authorities = constituency.getAuthorities();
        Map<String, Authority> compactAuthorities = new HashMap<>();

        for (Authority authority : authorities.values()) {
            compactAuthorities.put(authority.getId(), new Authority(authority.getId(), authority.getName()));
        }

        return compactAuthorities;
    }

    public Authority getAuthorityById(String electionId, Integer constituencyId, String authorityId) {
        Map<String, Authority> authorities = getAuthoritiesByConstituencyId(electionId, constituencyId);
        Authority authority = authorities.get(authorityId);
        if (authority == null) {
            throw new ResourceNotFoundException("Authority with ID " + authorityId + " not found in constituency " + constituencyId);
        }
        return authority;
    }

    public Map<Integer, Party> getPartiesByAuthorityId(String electionId, Integer constituencyId, String authorityId) {
        Authority authority = getAuthorityById(electionId, constituencyId, authorityId);
        if (authority == null) {
            throw new ResourceNotFoundException("Authority with ID " + authorityId + " not found");
        }
        return authority.getParties();
    }

    public Party getPartyById(String electionId, Integer constituencyId, String authorityId, Integer partyId) {
        Map<Integer, Party> parties = getPartiesByAuthorityId(electionId, constituencyId, authorityId);
        Party party = parties.get(partyId);
        if (party == null) {
            throw new ResourceNotFoundException("Party with ID " + partyId + " not found");
        }
        return party;
    }

    public List<Candidate> getCandidatesByPartyId(String electionId, Integer constituencyId, String authorityId, Integer partyId) {
        Party party = getPartyById(electionId, constituencyId, authorityId, partyId);
        if (party == null) {
            throw new ResourceNotFoundException("Party with ID " + partyId + " not found");
        }
        return party.getCandidates();
    }
}
