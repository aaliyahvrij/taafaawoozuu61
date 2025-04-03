package com.voteU.election.java.controller;

import com.voteU.election.java.model.Contest;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.services.ElectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("election")
public class ElectionController {
    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @GetMapping
    public Map<String, Map<Integer, Contest>> getElections() {
        return electionService.getElections();
    }

    @PostMapping
    public boolean readResults() {
        return electionService.readElections();
    }

}

