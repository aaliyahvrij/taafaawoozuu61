package com.voteU.election.java.controller;

import com.voteU.election.java.model.Contest;
import com.voteU.election.java.services.ConsistuencyService;
import com.voteU.election.java.services.ElectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/constituency")
public class ConstituencyController {
    private final ConsistuencyService consistuencyService;

    public ConstituencyController(ConsistuencyService consistuencyService) {
        this.consistuencyService = consistuencyService;
    }

    @GetMapping
    public Map<String, Map<Integer, Contest>> getConstituency() {
        return consistuencyService.getElections();
    }

    @PostMapping
    public boolean readResults() {
        return consistuencyService.readElections();
    }
}
