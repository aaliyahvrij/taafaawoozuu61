package com.voteU.election.java.controllers;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.services.AffiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/election/{electionIdListString}/constituencies/{constId}/municipalities/{munId}/pollingstations/{poStId}/affiliations")
public class AffiController {
    private final AffiService affiService;

    public AffiController(AffiService affiService) {
        this.affiService = affiService;
    }

    @GetMapping
    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getAffiLevel_candiList(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        return affiService.getAffiLevel_candiList(electionIdListString, constId, munId, poStId);
    }
}
