package com.voteU.election.java.controller.databaseVotes.partyVotes;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.services.ConstituencyPartyVotesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partyvotes/constituency")
public class ConstituencyPartyVotesController {

    private final ConstituencyPartyVotesService constituencyPartyVotesService;

    public ConstituencyPartyVotesController(ConstituencyPartyVotesService service) {
        this.constituencyPartyVotesService = service;
    }

    @GetMapping
    public List<PartyVotesDTO> getVotesByElectionAndProvince(
            @RequestParam String electionId,
            @RequestParam Integer constituencyId,
            @RequestParam(required = false, defaultValue = "false") boolean byvotes,
            @RequestParam(required = false, defaultValue = "false") boolean byname
    ) {
        if (byvotes) {
            return constituencyPartyVotesService.getPartyVotesSortedByVotes(electionId, constituencyId);
        }
        if (byname) {
            return constituencyPartyVotesService.getPartyVotesSortedByName(electionId, constituencyId);
        }

        return constituencyPartyVotesService.getPartyVotes(electionId, constituencyId);
    }
}
