package com.voteU.election.java.controller;
import com.voteU.election.java.model.Constituency;
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

    @GetMapping("/{year}")
    public Map<Integer,Constituency> getConstituencyById(@PathVariable String year) {
        return constituencyService.getConstituencies(year);
    }


}
