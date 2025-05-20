package com.voteU.election.java.services;

import com.voteU.election.java.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service class responsible for managing and retrieving data related to authorities, parties,
 * and candidates within electoral constituencies.
 *
 * This class provides methods to retrieve authorities by constituency ID, authority by ID, parties
 * associated with a given authority, as well as candidates of a specific party.
 */
@Service
public class AuthorityService {
    private final ElectionService electionService;

    /**
     * Constructor for the AuthorityService class.
     *
     * @param electionService the ElectionService instance used to retrieve election-related data
     */
    public AuthorityService(ElectionService electionService) {
        this.electionService = electionService;
    }


    /**
     * Retrieves a map of authorities associated with a specific constituency in an election.
     * The authorities are identified by their respective IDs.
     *
     * @param electionId      The unique identifier of the election.
     * @param constituencyId  The unique identifier of the constituency within the election.
     * @return A map where the keys are authority IDs and the values are Authority objects
     *         corresponding to the identified constituency, or null if the constituency is not found.
     */
    public Map<String, Authority> getAuthoritiesByConstituencyId(String electionId, int constituencyId) {
        Election election = electionService.getElection(electionId);
        Constituency constituency = election.getConstituencies().get(constituencyId);
        if (constituency == null) {
            return null;
        }
        return constituency.getAuthorities();
    }

    /**
     * Retrieves the authority with the specified ID from the given election ID and constituency ID.
     *
     * @param electionId the unique identifier of the election
     * @param constituencyId the unique identifier of the constituency
     * @param authorityId the unique identifier of the authority to retrieve
     * @return the {@code Authority} object matching the specified ID, or {@code null} if no such authority exists
     */
    public Authority getAuthorityById(String electionId, Integer constituencyId, String authorityId) {
        Map<String, Authority> authorities = getAuthoritiesByConstituencyId(electionId, constituencyId);
        if (authorities == null) {
            return null;
        }
        return authorities.get(authorityId);
    }

    /**
     * Retrieves a map of parties associated with a specific authority identified by its ID.
     *
     * @param electionId the ID of the election to which the authority belongs
     * @param constituencyId the ID of the constituency to which the authority belongs
     * @param authorityId the ID of the authority whose parties are to be retrieved
     * @return a map where the keys are party IDs and the values are Party objects associated with the specified authority,
     *         or null if the authority is not found
     */
    public Map<Integer, Party> getPartiesByAuthorityId(String electionId, Integer constituencyId, String authorityId) {
        Authority authority = getAuthorityById(electionId, constituencyId, authorityId);
        if (authority == null) {
            return null;
        }
        return authority.getAuthorityParties();
    }

    /**
     * Retrieves a Party object by its ID for the specified election, constituency, and authority.
     *
     * This method fetches the parties associated with a specific authority,
     * then returns the party that matches the given party ID.
     *
     * @param electionId the ID of the election
     * @param constituencyId the ID of the constituency
     * @param authorityId the ID of the authority
     * @param partyId the ID of the party to be retrieved
     * @return the Party object corresponding to the given party ID, or null if not found
     */
    public Party getPartyById(String electionId, Integer constituencyId, String authorityId, Integer partyId) {
        Map<Integer, Party> parties = getPartiesByAuthorityId(electionId, constituencyId, authorityId);
        if (parties == null) {
            return null;
        }
        return parties.get(partyId);
    }
    /**
     * Retrieves the list of candidates associated with a specific party within an election,
     * constituency, and authority.
     *
     * @param electionId The unique identifier of the election.
     * @param constituencyId The unique identifier of the constituency.
     * @param authorityId The unique identifier of the authority.
     * @param partyId The unique identifier of the party.
     * @return A list of candidates belonging to the specified party, or null if the party is not found.
     */
    public List<Candidate> getCandidatesByPartyId(String electionId, Integer constituencyId, String authorityId, Integer partyId) {
        Party party = getPartyById(electionId, constituencyId, authorityId, partyId);
        if (party == null) {
            return null;
        }
        return party.getCandidates();
    }
}
