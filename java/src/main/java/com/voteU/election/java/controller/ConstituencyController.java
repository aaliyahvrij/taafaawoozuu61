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

    @GetMapping("/{year}/{contestId}")
    public Constituency getConstituencyById(@PathVariable String year, @PathVariable int contestId) {
        return consistuencyService.getConstituency(year, contestId);
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
