package com.voteU.election.java.controller.databaseVotes.partyVotes;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.services.ProvincePartyVotesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partyvotes/province")
public class ProvincePartyVotesController {

    private final ProvincePartyVotesService provincePartyVotesService;

    public ProvincePartyVotesController(ProvincePartyVotesService service) {
        this.provincePartyVotesService = service;
    }

    @GetMapping
    public List<PartyVotesDTO> getVotesByElectionAndProvince(
            @RequestParam String electionId,
            @RequestParam Integer provinceId,
            @RequestParam(required = false, defaultValue = "false") boolean byvotes,
            @RequestParam(required = false, defaultValue = "false") boolean byname
            ){
        if(byvotes){
            return provincePartyVotesService.getPartyVotesSortedByVotes(electionId,provinceId);
        }
        if(byname){
            return provincePartyVotesService.getPartyVotesSortedByName(electionId,provinceId);
        }

        return provincePartyVotesService.getPartyVotes(electionId, provinceId);
    }
}
