package com.voteU.election.java.controller;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.services.ConstiService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/constituency")
public class ConstiController {
    private final ConstiService constiService;

    public ConstiController(ConstiService constiService) {
        this.constiService = constiService;
    }

    @GetMapping
    public Map<String, Map<Integer, Constituency>> getConstituency() {
        return constiService.getElections();
    }

    @PostMapping
    public boolean readResults() {
        return constiService.readElections();
    }
}
