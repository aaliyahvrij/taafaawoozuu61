package com.voteU.election.java.controller;

import com.voteU.election.java.database.responseDTO.PartyVotesDTO;
import com.voteU.election.java.services.AuthorityPartyVotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authority-votes")
public class AuthorityPartyVotesController {

    private final AuthorityPartyVotesService service;

    @Autowired
    public AuthorityPartyVotesController(AuthorityPartyVotesService service) {
        this.service = service;
    }


    // 4. Get summarized party votes in a specific authority (DTO)
    @GetMapping("/summary/authority/{authorityId}/election/{electionId}")
    public List<PartyVotesDTO> getPartyVotesByAuthority(@PathVariable String authorityId,
                                                    @PathVariable String electionId) {
        return service.getPartyVotesByAuthority(authorityId, electionId);
    }
}
