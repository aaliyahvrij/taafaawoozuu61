package com.voteU.election.java.controllers;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.services.ElectionService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/election")
public class ElectionController {
    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @PostMapping("/{electionIdListString}")
    public boolean readElectionData(@PathVariable String electionIdListString) {
        return this.electionService.getElectionData(electionIdListString);
    }

    @GetMapping("/{electionIdListString}/affiliations")
    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getAffiList_lhMap(@PathVariable String electionIdListString) {
        return this.electionService.getAffiList_lhMap(electionIdListString);
    }

    @GetMapping("/{electionIdListString}/pollingstations")
    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getPoStList_lhMap(@PathVariable String electionIdListString) {
        return this.electionService.getPoStList_lhMap(electionIdListString);
    }
}
