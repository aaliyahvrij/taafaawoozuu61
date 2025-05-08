package com.voteU.election.java.controller;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.services.ConsistuencyService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/constituency")
public class ConstituencyController {
    private final ConsistuencyService consistuencyService;

    public ConstituencyController(ConsistuencyService consistuencyService) {
        this.consistuencyService = consistuencyService;
    }

    @GetMapping
    public Map<String, Map<Integer, Constituency>> getConstituency() {
        return consistuencyService.getElections();
    }

    @PostMapping
    public boolean readResults() {
        return consistuencyService.readElections();
    }
}
