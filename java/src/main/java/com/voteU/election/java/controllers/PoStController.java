package com.voteU.election.java.controllers;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.services.PoStService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/{electionId}/pollingstations")
public class PoStController {
    private final PoStService poStService;

    public PoStController(PoStService poStService) {
        this.poStService = poStService;
    }

    @GetMapping
    public LinkedHashMap<String, PollingStation> getElectoralLevel_poStOf(@PathVariable String electionId) {
        return this.poStService.getElectoralLevel_pollingStationsOf(electionId);
    }
}
