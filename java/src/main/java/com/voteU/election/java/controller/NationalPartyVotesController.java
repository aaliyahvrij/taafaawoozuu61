package com.voteU.election.java.controller;

import com.voteU.election.java.database.responseDTO.PartyVotesDTO;
import com.voteU.election.java.services.NationalPartyVotesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partyvotes/national")
public class NationalPartyVotesController {

    private final NationalPartyVotesService nationalPartyVotesService;

    public NationalPartyVotesController(NationalPartyVotesService nationalPartyVotesService) {
        this.nationalPartyVotesService = nationalPartyVotesService;
    }

    @GetMapping("/{electionId}")
    public List<PartyVotesDTO> getNationalVotes(@PathVariable String electionId) {
        return nationalPartyVotesService.getNationalPartyVotesByElectionId(electionId);
    }
}
