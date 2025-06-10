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
    public boolean readAllElectoralData() {
        return this.electionService.readAllElectoralData();
    }

    @PostMapping("/{electionId}")
    public boolean readElectoralDataOf(@PathVariable String electionId) {
        return this.electionService.readElectoralDataOf(electionId);
    }

    @GetMapping
    public LinkedHashMap<String, Election> getAllElectoralData() {
        return this.electionService.getAllElectoralData();
    }

    @GetMapping("/{electionId}")
    public Election getElectoralDataOf(@PathVariable String electionId) {
        return this.electionService.getElectoralDataOf(electionId);
    }

    @GetMapping("/{electionId}/affiliations")
    public LinkedHashMap<Integer, Affiliation> getNationalLevel_affiListMapOf(@PathVariable String electionId) {
        return this.electionService.getNationalLevel_affiListMapOf(electionId);
    }

    @GetMapping("/{electionId}/pollingstations")
    public LinkedHashMap<String, PollingStation> getNationalLevel_poStListMapOf(@PathVariable String electionId) {
        return this.electionService.getNationalLevel_poStListMapOf(electionId);
    }
}
