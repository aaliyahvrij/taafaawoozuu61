package com.voteU.election.java.controller.databaseVotes;


import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.services.NationalPartyVotesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/partyvotes/national")
public class NationalPartyVotesController {

    private final NationalPartyVotesService nationalPartyVotesService;

    public NationalPartyVotesController(NationalPartyVotesService nationalPartyVotesService){
        this.nationalPartyVotesService = nationalPartyVotesService;
    }

    @GetMapping
    public List<PartyVotesDTO> getNationalPartyVotesByElectionId(@RequestParam String electionId){
        return nationalPartyVotesService.getNationalPartyVotes(electionId);
    }
}
