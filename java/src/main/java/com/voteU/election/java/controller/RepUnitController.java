package com.voteU.election.java.controller;

import com.voteU.election.java.model.RepUnit;
import com.voteU.election.java.services.RepUnitService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/repunit")
public class RepUnitController {
    private final RepUnitService repUnitService;

    public RepUnitController(RepUnitService repUnitService) {
        this.repUnitService = repUnitService;
    }

    @GetMapping
    public Map<Integer, RepUnit> getRepUnits(String electionId) {
        return repUnitService.getRepUnits(electionId);
    }
}
