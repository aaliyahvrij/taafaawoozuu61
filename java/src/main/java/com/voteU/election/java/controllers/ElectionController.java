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

    @PostMapping("/{electionIdListString}")
    public boolean readElectoralData(@PathVariable String electionIdListString) {
        return this.electionService.readElectoralData(electionIdListString);
    }

    @GetMapping("/{electionIdListString}")
    public LinkedHashMap<String, Election> getElectoralData(@PathVariable String electionIdListString) {
        return this.electionService.getElectoralData(electionIdListString);
    }

    @GetMapping("/{electionId}/affiliations")
    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getNationalLevel_affiList_lhMap(@PathVariable String electionId) {
        return this.electionService.getNationalLevel_affiList_lhMap(electionId);
    }

    @GetMapping("/{electionId}/pollingstations")
    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getNationalLevel_poStList_lhMap(@PathVariable String electionId) {
        return this.electionService.getNationalLevel_poStList_lhMap(electionId);
    }
}
