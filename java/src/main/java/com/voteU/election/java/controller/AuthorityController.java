package com.voteU.election.java.controller;

import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.Candidate;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.services.AuthorityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * AuthorityController handles HTTP requests related to election authority operations
 * within a specific constituency and year. This includes fetching authorities, parties,
 * and candidates for the associated authority.
 */
@RestController
@RequestMapping("/api/election/{year}/constituencies/{constituencyId}/authorities")
public class AuthorityController {
    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * Retrieves a map of election authorities for a given constituency ID and year.
     *
     * @param year the election year for which authorities are being retrieved
     * @param constituencyId the ID of the constituency for which authorities are being retrieved
     * @return a map where the key is the authority identifier and the value is an Authority object
     * @throws ResponseStatusException if an internal server error occurs during the retrieval process
     */
    @GetMapping
    public Map<String, Authority> getAuthoritiesByConstituencyId(@PathVariable String year, @PathVariable Integer constituencyId) {
        try {
            return authorityService.getAuthoritiesByConstituencyId(year, constituencyId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch authorities", e);
        }
    }

    /**
     * Retrieves an authority by its unique identifier within the context of a specific election year
     * and constituency. If the authority is not found, a 404 NOT FOUND response is returned.
     *
     * @param year the election year for which the authority data is being requested
     * @param constituencyId the identifier for the specific constituency
     * @param authorityId the unique identifier of the authority to fetch
     * @return the Authority object corresponding to the provided authorityId
     * @throws ResponseStatusException if the authority is not found
     */
    @GetMapping("/{authorityId}")
    public Authority getAuthorityById(@PathVariable String year, @PathVariable Integer constituencyId, @PathVariable String authorityId) {
        Authority authority = authorityService.getAuthorityById(year, constituencyId, authorityId);
        if (authority == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Authority not found");
        }
        return authority;
    }

    /**
     * Retrieves a map of parties associated with a specific authority ID within a given year
     * and constituency ID.
     *
     * @param year The election year for which the parties are to be fetched.
     * @param constituencyId The identifier of the constituency related to the authority.
     * @param authorityId The identifier of the authority for which parties are to be fetched.
     * @return A map where the key is the party ID (Integer), and the value is the Party object.
     * @throws ResponseStatusException If an error occurs during the processing of the request.
     */
    @GetMapping("/{authorityId}/parties")
    public Map<Integer, Party> getPartiesByAuthorityId(@PathVariable String year, @PathVariable Integer constituencyId, @PathVariable String authorityId) {
        try {
            return authorityService.getPartiesByAuthorityId(year, constituencyId, authorityId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch parties", e);
        }
    }

    /**
     * Retrieves a party by its unique identifier for a specific authority within a given year and constituency.
     *
     * @param year the election year
     * @param constituencyId the unique identifier for the constituency
     * @param authorityId the unique identifier for the authority
     * @param partyId the unique identifier for the party
     * @return the Party object associated with the given partyId
     * @throws ResponseStatusException if the party is not found
     */
    @GetMapping("/{authorityId}/parties/{partyId}")
    public Party getPartyById(@PathVariable String year, @PathVariable Integer constituencyId, @PathVariable String authorityId, @PathVariable Integer partyId) {
        Party party = authorityService.getPartyById(year, constituencyId, authorityId, partyId);
        if (party == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party not found");
        }
        return party;
    }

    /**
     * Retrieves a list of candidates associated with a specific party within an authority.
     *
     * @param year The election year.
     * @param constituencyId The ID of the constituency.
     * @param authorityId The ID of the election authority.
     * @param partyId The ID of the party.
     * @return A list of candidates belonging to the specified party.
     * @throws ResponseStatusException if an internal server error occurs while retrieving candidates.
     */
    @GetMapping("/{authorityId}/parties/{partyId}/candidates")
    public List<Candidate> getCandidatesByPartyId(@PathVariable String year, @PathVariable Integer constituencyId, @PathVariable String authorityId, @PathVariable Integer partyId) {
        try {
            return authorityService.getCandidatesByPartyId(year, constituencyId, authorityId, partyId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch candidates", e);
        }
    }
}
