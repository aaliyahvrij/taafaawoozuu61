package com.voteU.election.java.controller;
import com.voteU.election.java.model.Contest;
import com.voteU.election.java.services.ConstituencyService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/constituency")
public class ConstituencyController {
    private final ConstituencyService constituencyService;

    public ConstituencyController(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }

//    @GetMapping("/{year}/{contestId}")
//    public Contest getConstituencyById(@PathVariable String year, @PathVariable int contestId) {
//        return constituencyService.getConstituency(year, contestId);
//    }

    @GetMapping
    public Map<String, Map<Integer, Contest>> getConstituency() {
        return constituencyService.getElections();
    }

    @PostMapping
    public boolean readResults() {
        return constituencyService.readElections();
    }
}
