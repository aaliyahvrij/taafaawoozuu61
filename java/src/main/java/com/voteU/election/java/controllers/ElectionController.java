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
    public Map<String, Election> getAllElections() {
        return electionService.getAll();
    }

    @GetMapping("/{electionId}")
    public Election getElection(@PathVariable String electionId) {
        return electionService.getElection(electionId);
    }

    @GetMapping("/{electionId}/affiliations")
    public Map<Integer, Affiliation> getElectionLevel_affiliations(@PathVariable String electionId) {
        return electionService.getElectionLevel_affiliations(electionId);
    }

    @GetMapping("/{electionId}/pollingstations")
    public Map<String, PollingStation> getElectionLevel_pollingStations(@PathVariable String electionId) {
        return electionService.getElectionLevel_pollingStations(electionId);
    }
}
