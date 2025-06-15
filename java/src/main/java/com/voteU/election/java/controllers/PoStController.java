package com.voteU.election.java.controllers;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.compactDTO.CompactPollingStation;
import com.voteU.election.java.services.MuniService;
import com.voteU.election.java.services.PoStService;
import com.voteU.election.java.models.Affiliation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/election/{electionIdListString}/constituencies/{constId}/municipalities/{munId}/pollingstations")
public class PoStController {
    private final MuniService muniService;
    private final PoStService poStService;

    public PoStController(MuniService muniService, PoStService poStService) {
        this.muniService = muniService;
        this.poStService = poStService;
    }

    @GetMapping
    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getMuniLevel_poStList_lhMap(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId) {
        return muniService.getMuniLevel_poStList_lhMap(electionIdListString, constId, munId);
    }

    @GetMapping("/compact")
    public LinkedHashMap<String, List<CompactPollingStation>> getMuniLevel_compactPoStList(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId) {
        return muniService.getMuniLevel_compactPoStList(electionIdListString, constId, munId);
    }

    @GetMapping("/{poStId}")
    public LinkedHashMap<String, PollingStation> getMuniLevel_poSt(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        return muniService.getMuniLevel_poSt(electionIdListString, constId, munId, poStId);
    }

    @GetMapping("/{poStId}/affiliations")
    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getPoStLevel_affiList_lhMap(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        return poStService.getPoStLevel_affiList_lhMap(electionIdListString, constId, munId, poStId);
    }
}
