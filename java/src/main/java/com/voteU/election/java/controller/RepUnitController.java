package com.voteU.election.java.controller;

import com.voteU.election.java.model.RepUnit;
import com.voteU.election.java.services.RepUnitService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/{electionId}/repunits")
public class RepUnitController {
    private final RepUnitService repUnitService;

    public RepUnitController(RepUnitService repUnitService) {
        this.repUnitService = repUnitService;
    }

    @GetMapping
    public Map<String, RepUnit> getAllRepUnitsOfElection(@PathVariable String electionId) {
        return repUnitService.getAllRepUnitsOfElection(electionId);
    }
}
