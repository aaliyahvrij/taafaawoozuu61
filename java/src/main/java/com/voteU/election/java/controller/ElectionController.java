package com.voteU.election.java.controller;

import com.voteU.election.java.model.*;
import com.voteU.election.java.services.ElectionService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
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
    public Map<Integer, Affiliation> getAllAffiliationsOfElection(@PathVariable String electionId) {
        return electionService.getAllAffiliationsOfElection(electionId);
    }

    @GetMapping("/{electionId}/repunits")
    public Map<String, RepUnit> getAllRepUnitsOfElection(@PathVariable String electionId) {
        return electionService.getAllRepUnitsOfElection(electionId);
    }
}
