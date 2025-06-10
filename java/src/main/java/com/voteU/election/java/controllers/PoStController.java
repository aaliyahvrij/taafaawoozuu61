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
@RequestMapping("/api/{electionId}/pollingstations")
public class PoStController {
    private final PoStService poStService;

    public PoStController(PoStService poStService) {
        this.poStService = poStService;
    }

    /*@GetMapping
    public LinkedHashMap<String, PollingStation> getNationalLevel_pollingStationsOf(@PathVariable String electionId) {
        return this.poStService.getNationalLevel_pollingStationsOf(electionId);
    }*/

    @GetMapping
    public Map<String, PollingStation> getMuniLevel_pollingStationsOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        return poStService.getMuniLevel_pollingStationsOf(electionId, constId, munId);
    }

    @GetMapping("/compact")
    public List<CompactPollingStation> getMuniLevel_pollingStationsOf_compact(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        return poStService.getMuniLevel_pollingStationsOf_compact(electionId, constId, munId);
    }

    @GetMapping("/{poStId}")
    public PollingStation getPollingStationById(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        return poStService.getPollingStationById(electionId, constId, munId, poStId);
    }

    @GetMapping("/{poStId}/affiliations")
    public Map<Integer, Affiliation> getPoStLevel_affiliationsOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        return poStService.getPoStLevel_affiliationsOf(electionId, constId, munId, poStId);
    }
}
