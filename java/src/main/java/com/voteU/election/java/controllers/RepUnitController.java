package com.voteU.election.java.controllers;

import com.voteU.election.java.models.RepUnit;
import com.voteU.election.java.services.RepUnitService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
