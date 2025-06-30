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
    /**
     * Represents the service component used for managing and performing operations
     * related to elections, such as candidate management, vote counting, election
     * scheduling, and related functionalities.
     *
     * This variable provides access to business logic and methods required for
     * election process handling, ensuring proper encapsulation and separation
     * of concerns within the application.
     */
    private final ElectionService electionService;

    /**
     * Constructs a new instance of the AuthorityService class.
     *
     * @param electionService the ElectionService used for managing and interacting
     *                        with election-related operations
     */
    public AuthorityService(ElectionService electionService) {
        this.electionService = electionService;
    }

    /**
     * Retrieves the authorities associated with a specific constituency within a given election.
     *
     * @param electionId      the unique identifier of the election
     * @param constituencyId  the unique identifier of the constituency within the election
     * @return a map containing authorities where the key is the authority name and the value is the corresponding Authority object
     * @throws ResourceNotFoundException if the specified election or constituency is not found
     */
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

    /**
     * Retrieves a map of compacted authorities for a specified constituency within an election.
     *
     * @param electionId the unique identifier of the election
     * @param constituencyId the unique identifier of the constituency
     * @return a map where the key is the authority ID and the value is a compact representation of the authority
     * @throws ResourceNotFoundException if the election or constituency with the specified IDs does not exist
     */
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

    /**
     * Retrieves an authority based on the provided election ID, constituency ID, and authority ID.
     *
     * @param electionId The identifier of the election to which the authority belongs.
     * @param constituencyId The identifier of the constituency in which the authority exists.
     * @param authorityId The unique identifier of the authority to be retrieved.
     * @return The Authority object corresponding to the specified ID.
     * @throws ResourceNotFoundException if no authority is found with the given ID in the specified constituency.
     */
    public Authority getAuthorityById(String electionId, Integer constituencyId, String authorityId) {
        Map<String, Authority> authorities = getAuthoritiesByConstituencyId(electionId, constituencyId);
        Authority authority = authorities.get(authorityId);
        if (authority == null) {
            throw new ResourceNotFoundException("Authority with ID " + authorityId + " not found in constituency " + constituencyId);
        }
        return authority;
    }

    /**
     * Retrieves a map of parties associated with a specific authority.
     *
     * @param electionId the ID of the election
     * @param constituencyId the ID of the constituency
     * @param authorityId the ID of the authority
     * @return a map where the key is the party ID (integer) and the value is the corresponding Party object
     * @throws ResourceNotFoundException if the authority with the given ID is not found
     */
    public Map<Integer, Party> getPartiesByAuthorityId(String electionId, Integer constituencyId, String authorityId) {
        Authority authority = getAuthorityById(electionId, constituencyId, authorityId);
        if (authority == null) {
            throw new ResourceNotFoundException("Authority with ID " + authorityId + " not found");
        }
        return authority.getParties();
    }

    /**
     * Retrieves a Party object based on the provided election ID, constituency ID, authority ID, and party ID.
     *
     * @param electionId the unique identifier for the election
     * @param constituencyId the unique identifier for the constituency
     * @param authorityId the unique identifier for the authority
     * @param partyId the unique identifier for the party to retrieve
     * @return the Party object corresponding to the specified IDs
     * @throws ResourceNotFoundException if no party with the specified party ID is found
     */
    public Party getPartyById(String electionId, Integer constituencyId, String authorityId, Integer partyId) {
        Map<Integer, Party> parties = getPartiesByAuthorityId(electionId, constituencyId, authorityId);
        Party party = parties.get(partyId);
        if (party == null) {
            throw new ResourceNotFoundException("Party with ID " + partyId + " not found");
        }
        return party;
    }

    /**
     * Retrieves a list of candidates associated with a specific party based on the given identifiers.
     *
     * @param electionId the ID of the election to which the party belongs
     * @param constituencyId the ID of the constituency associated with the party
     * @param authorityId the ID of the authority managing the election
     * @param partyId the ID of the party whose candidates are being retrieved
     * @return a list of candidates associated with the specified party
     * @throws ResourceNotFoundException if the party with the specified ID is not found
     */
    public List<Candidate> getCandidatesByPartyId(String electionId, Integer constituencyId, String authorityId, Integer partyId) {
        Party party = getPartyById(electionId, constituencyId, authorityId, partyId);
        if (party == null) {
            throw new ResourceNotFoundException("Party with ID " + partyId + " not found");
        }
        return party.getCandidates();
    }
}
