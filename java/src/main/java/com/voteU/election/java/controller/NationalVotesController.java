package com.voteU.election.java.controller;

import com.voteU.election.java.database.responseDTO.PartyVotesDTO;
import com.voteU.election.java.services.NationalVotesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nationalpartyvotes")
public class NationalVotesController {

    private final NationalVotesService nationalVotesService;

    public NationalVotesController(NationalVotesService nationalVotesService) {
        this.nationalVotesService = nationalVotesService;
    }

    @GetMapping("/{electionId}")
    public List<PartyVotesDTO> getVotesByElection(@PathVariable String electionId) {
        return nationalVotesService.getVotesByElection(electionId);
    }
}
