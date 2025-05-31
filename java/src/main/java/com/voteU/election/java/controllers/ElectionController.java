package com.voteU.election.java.controllers;

import com.voteU.election.java.models.*;
import com.voteU.election.java.services.ElectionService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/election")
public class ElectionController {
    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @PostMapping
    public boolean readResults() {
        return electionService.readElections();
    }

    @PostMapping("/{electionId}")
    public boolean readResultsYear(@PathVariable String electionId) {
        return electionService.readElection(electionId);
    }

    @GetMapping
    public Map<String, Election> getAllElectoralLevelData() {
        return electionService.getAllElectoralLevelData();
    }

    @GetMapping("/{electionId}")
    public Election getElectoralLevelDataOf(@PathVariable String electionId) {
        return electionService.getElectoralLevelDataOf(electionId);
    }

    @GetMapping("/{electionId}/affiliations")
    public Map<Integer, Affiliation> getElectoralLevel_affiliationsOf(@PathVariable String electionId) {
        return electionService.getElectoralLevel_affiliationsOf(electionId);
    }

    @GetMapping("/{electionId}/pollingstations")
    public Map<String, PollingStation> getElectoralLevel_pollingStationsOf(@PathVariable String electionId) {
        return electionService.getElectoralLevel_pollingStationsOf(electionId);
    }
}
