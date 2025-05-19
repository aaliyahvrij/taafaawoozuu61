package com.voteU.election.java.controller;
import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.Candidate;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.services.AuthorityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/election/{year}/constituencies/{constituencyId}/authorities")
public class AuthorityController {
    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping
    public Map<String, Authority> getAuthoritiesByConstituencyId(@PathVariable String year, @PathVariable Integer constituencyId) {
        return authorityService.getAuthoritiesByConstituencyId(year, constituencyId);
    }

    @GetMapping("/{authorityId}")
    public Authority getAuthorityById(@PathVariable String year,@PathVariable Integer constituencyId, @PathVariable String authorityId) {
        return authorityService.getAuthorityById(year, constituencyId, authorityId);
    }

    @GetMapping("/{authorityId}/parties")
    public Map<Integer, Party> getPartiesByAuthorityId(@PathVariable String year, @PathVariable Integer constituencyId, @PathVariable String authorityId) {
        return authorityService.getPartiesByAuthorityId(year, constituencyId, authorityId);
    }

    @GetMapping("/{authorityId}/parties/{partyId}")
    public Party getPartyById(@PathVariable String year, @PathVariable Integer constituencyId, @PathVariable String authorityId, @PathVariable Integer partyId){
        return authorityService.getPartyById(year, constituencyId, authorityId, partyId);
    }

    @GetMapping("/{authorityId}/parties/{partyId}/candidates")
    public List<Candidate> getCandidatesByPartyId(@PathVariable String year, @PathVariable Integer constituencyId, @PathVariable String authorityId, @PathVariable Integer partyId){
        return authorityService.getCandidatesByPartyId(year, constituencyId, authorityId, partyId);
    }
}