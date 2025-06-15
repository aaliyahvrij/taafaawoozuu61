package com.voteU.election.java.controllers;

import com.voteU.election.java.services.PoStService;
import com.voteU.election.java.models.Affiliation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/election/{electionIdListString}/pollingstations")
public class PoStController {
    private final PoStService poStService;

    public PoStController(PoStService poStService) {
        this.poStService = poStService;
    }

    @GetMapping("/{poStId}/affiliations")
    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getPoStLevel_affiList_lhMap(@PathVariable String electionIdListString, @PathVariable String poStId) {
        return poStService.getPoStLevel_affiList_lhMap(electionIdListString, poStId);
    }
}
