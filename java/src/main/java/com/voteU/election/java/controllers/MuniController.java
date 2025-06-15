package com.voteU.election.java.controllers;

import com.voteU.election.java.compactDTO.CompactPollingStation;
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
@RequestMapping("/api/election/{electionIdListString}/constituencies/{constId}/municipalities")
public class MuniController {
    private final MuniService muniService;

    public MuniController(MuniService muniService) {
        this.muniService = muniService;
    }

    @GetMapping("/{munId}/pollingstations")
    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getMuniLevel_poStList_lhMap(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId) {
        return muniService.getMuniLevel_poStList_lhMap(electionIdListString, constId, munId);
    }

    @GetMapping("/{munId}/pollingstations/compact")
    public LinkedHashMap<String, List<CompactPollingStation>> getMuniLevel_compactPoStList(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId) {
        return muniService.getMuniLevel_compactPoStList(electionIdListString, constId, munId);
    }

    @GetMapping("/{munId}/pollingstations/{poStId}")
    public LinkedHashMap<String, PollingStation> getMuniLevel_poSt(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        return muniService.getMuniLevel_poSt(electionIdListString, constId, munId, poStId);
    }

    @GetMapping("/{munId}/affiliations")
    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getMuniLevel_affiList_lhMap(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId) {
        return muniService.getMuniLevel_affiList_lhMap(electionIdListString, constId, munId);
    }

    @GetMapping("/{munId}/affiliations/{affId}")
    public LinkedHashMap<String, Affiliation> getMuniLevel_affi(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId, @PathVariable int affId) {
        return muniService.getMuniLevel_affi(electionIdListString, constId, munId, affId);
    }
}
