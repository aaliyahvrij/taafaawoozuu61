package com.voteU.election.java.controllers;

import com.voteU.election.java.models.*;
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

    @PostMapping
    public boolean readAllElectoralLevelData() {
        return this.electionService.readAllElectoralLevelData();
    }

    @PostMapping("/{electionId}")
    public boolean readElectoralLevelDataOf(@PathVariable String electionId) {
        return this.electionService.readElectoralLevelDataOf(electionId);
    }

    @GetMapping
    public LinkedHashMap<String, Election> getAllElectoralLevelData() {
        return this.electionService.getAllElectoralLevelData();
    }

    @GetMapping("/{electionId}")
    public Election getElectoralLevelDataOf(@PathVariable String electionId) {
        return this.electionService.getElectoralLevelDataOf(electionId);
    }

    @GetMapping("/{electionId}/affiliations")
    public LinkedHashMap<Integer, Affiliation> getElectoralLevel_affiliationsOf(@PathVariable String electionId) {
        return this.electionService.getElectoralLevel_affiliationsOf(electionId);
    }

    @GetMapping("/{electionId}/pollingstations")
    public LinkedHashMap<String, PollingStation> getElectoralLevel_pollingStationsOf(@PathVariable String electionId) {
        return this.electionService.getElectoralLevel_pollingStationsOf(electionId);
    }
}
