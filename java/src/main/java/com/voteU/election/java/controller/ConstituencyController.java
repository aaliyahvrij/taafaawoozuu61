package com.voteU.election.java.controller;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.services.ConstituencyService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/election/{year}/constituencies")
public class ConstituencyController {

    private final ConstituencyService constituencyService;

    public ConstituencyController(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }

    @GetMapping
    public Map<Integer, Constituency> getConstituenciesByYear(@PathVariable String year){
        return constituencyService.getConstituenciesByYear(year);
    }

    @GetMapping("/{constituencyId}")
    public Constituency getConstituencyById(@PathVariable String year, @PathVariable Integer constituencyId){
        return constituencyService.getConstituencyById(year, constituencyId);
    }

    @GetMapping("/{constituencyId}/parties")
    public Map<Integer, Party> getPartiesByConstituencyId(@PathVariable String year, @PathVariable Integer constituencyId){
        return constituencyService.getPartiesByConstituencyId(year, constituencyId);
    }

}

