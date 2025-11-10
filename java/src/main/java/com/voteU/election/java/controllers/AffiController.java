package com.voteU.election.java.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.voteU.election.java.services.AffiService;
import com.voteU.election.java.models.Candidate;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/election/{electionIdListString}/affiliations")
public class AffiController {
    private final AffiService affiService;

    public AffiController(AffiService affiService) {
        this.affiService = affiService;
    }

    @GetMapping("/{affId}/candidates")
    public LinkedHashMap<String, List<Candidate>> getCandiListLhMap(@PathVariable String electionIdListString, @PathVariable int affId) {
        return this.affiService.getCandiListLhMap(electionIdListString, affId);
    }
}
