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
        return this.electionService.readElections();
    }

    @PostMapping("/{electionId}")
    public boolean readResultsYear(@PathVariable String electionId) {
        return this.electionService.readElection(electionId);
    }

    @GetMapping
    public Map<String, Election> getAllElectoralLevelData() {
        return this.electionService.getAllElectoralLevelData();
    }

    @GetMapping("/{electionId}")
    public Election getElectoralLevelDataOf(@PathVariable String electionId) {
        return this.electionService.getElectoralLevelDataOf(electionId);
    }

    @GetMapping("/{electionId}/affiliations")
    public Map<Integer, Affiliation> getElectoralLevel_affiliationsOf(@PathVariable String electionId) {
        return this.electionService.getElectoralLevel_affiliationsOf(electionId);
    }

    @GetMapping("/{electionId}/pollingstations")
    public Map<String, PollingStation> getElectoralLevel_pollingStationsOf(@PathVariable String electionId) {
        return this.electionService.getElectoralLevel_pollingStationsOf(electionId);
    }
}
