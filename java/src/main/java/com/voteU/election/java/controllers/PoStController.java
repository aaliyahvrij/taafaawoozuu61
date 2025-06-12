package com.voteU.election.java.controllers;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.compactDTO.CompactPollingStation;
import com.voteU.election.java.services.PoStService;
import com.voteU.election.java.models.Affiliation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@RestController
//@RequestMapping("/api/{electionId}/pollingstations")
@RequestMapping("/api/election/{electionId}/constituencies/{constId}/municipalities/{munId}/pollingstations")
public class PoStController {
    private final PoStService poStService;

    public PoStController(PoStService poStService) {
        this.poStService = poStService;
    }

    /*@GetMapping
    public LinkedHashMap<String, PollingStation> getNationalLevel_poStListMapOf(@PathVariable String electionId) {
        return this.poStService.getNationalLevel_poStListMapOf(electionId);
    }*/

    @GetMapping
    public LinkedHashMap<String, PollingStation> getMuniLevel_poStListMapOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        return poStService.getMuniLevel_poStListMapOf(electionId, constId, munId);
    }

    @GetMapping("/compact")
    public List<CompactPollingStation> getMuniLevel_compactPoStListOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        return poStService.getMuniLevel_compactPoStListOf(electionId, constId, munId);
    }

    @GetMapping("/{poStId}")
    public PollingStation getMuniLevel_poStOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        return poStService.getMuniLevel_poStOf(electionId, constId, munId, poStId);
    }

    @GetMapping("/{poStId}/affiliations")
    public LinkedHashMap<Integer, Affiliation> getPoStLevel_affiListMapOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        return poStService.getPoStLevel_affiListMapOf(electionId, constId, munId, poStId);
    }
}
