package com.voteU.election.java.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.voteU.election.java.services.ElectionService;
import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/election")
public class ElectionController {
    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @PostMapping("/{electionIdListString}")
    public boolean readElection(@PathVariable String electionIdListString) {
        return this.electionService.getElection(electionIdListString);
    }

    @GetMapping("/{electionIdListString}/pollingstations")
    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getPoStListLhMap(@PathVariable String electionIdListString) {
        return this.electionService.getPoStListLhMap(electionIdListString);
    }

    @GetMapping("/{electionIdListString}/affiliations")
    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getAffiListLhMap(@PathVariable String electionIdListString) {
        return this.electionService.getAffiListLhMap(electionIdListString);
    }
}
