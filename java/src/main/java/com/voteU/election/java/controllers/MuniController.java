package com.voteU.election.java.controllers;

import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.services.MuniService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/election/{electionIdListString}/municipalities")
public class MuniController {
    private final MuniService muniService;

    public MuniController(MuniService muniService) {
        this.muniService = muniService;
    }

    @GetMapping("/{munId}/pollingstations")
    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getPoStList_lhMap(@PathVariable String electionIdListString, @PathVariable String munId) {
        return muniService.getPoStList_lhMap(electionIdListString, munId);
    }

    @GetMapping("/{munId}/pollingstations/compact")
    public LinkedHashMap<String, List<PollingStation>> getCompactPoStList_lhMap(@PathVariable String electionIdListString, @PathVariable String munId) {
        return muniService.getCompactPoStList_lhMap(electionIdListString, munId);
    }

    @GetMapping("/{munId}/pollingstations/{poStId}")
    public LinkedHashMap<String, PollingStation> getPoSt(@PathVariable String electionIdListString, @PathVariable String munId, @PathVariable String poStId) {
        return muniService.getPoSt(electionIdListString, munId, poStId);
    }

    @GetMapping("/{munId}/affiliations")
    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getAffiList_lhMap(@PathVariable String electionIdListString, @PathVariable String munId) {
        return muniService.getAffiList_lhMap(electionIdListString, munId);
    }

    @GetMapping("/{munId}/affiliations/{affId}")
    public LinkedHashMap<String, Affiliation> getAffi(@PathVariable String electionIdListString, @PathVariable String munId, @PathVariable int affId) {
        return muniService.getAffi(electionIdListString, munId, affId);
    }
}
