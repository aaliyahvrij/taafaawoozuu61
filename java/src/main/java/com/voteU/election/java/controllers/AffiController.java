package com.voteU.election.java.controllers;

import com.voteU.election.java.models.Candidate;
import com.voteU.election.java.services.AffiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/election/{electionIdListString}/affiliations")
public class AffiController {
    private final AffiService affiService;

    public AffiController(AffiService affiService) {
        this.affiService = affiService;
    }

    @GetMapping("/{affId}/candidates")
    public LinkedHashMap<String, List<Candidate>> getCandiList(@PathVariable String electionIdListString, @PathVariable int affId) {
        return affiService.getCandiList(electionIdListString, affId);
    }
}
