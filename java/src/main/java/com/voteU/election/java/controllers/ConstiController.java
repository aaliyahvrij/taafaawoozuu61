package com.voteU.election.java.controllers;

import com.voteU.election.java.models.Municipality;
import com.voteU.election.java.services.ConstiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/election/{electionIdListString}/constituencies")
public class ConstiController {
    private final ConstiService constiService;

    public ConstiController(ConstiService constiService) {
        this.constiService = constiService;
    }

    @GetMapping("/{constId}/municipalities")
    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getConstiLevel_muniList_lhMap(@PathVariable String electionIdListString, @PathVariable int constId) {
        return constiService.getConstiLevel_muniList_lhMap(electionIdListString, constId);
    }

    @GetMapping("/{constId}/municipalities/compact")
    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getConstiLevel_compactMuniList_lhMap(@PathVariable String electionIdListString, @PathVariable int constId) {
        return constiService.getConstiLevel_compactMuniList_lhMap(electionIdListString, constId);
    }

    @GetMapping("/{constId}/municipalities/{munId}")
    public LinkedHashMap<String, Municipality> getConstiLevel_muni(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId) {
        return constiService.getConstiLevel_muni(electionIdListString, constId, munId);
    }
}
