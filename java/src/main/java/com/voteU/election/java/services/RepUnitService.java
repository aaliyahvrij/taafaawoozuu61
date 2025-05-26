package com.voteU.election.java.services;

import com.voteU.election.java.models.RepUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class RepUnitService {
    private final ElectionService electionService;

    public RepUnitService(ElectionService electionService) {
        this.electionService = electionService;
    }

    /**
     * Retrieves all stored reporting units (GET).
     */
    public Map<String, RepUnit> getAllRepUnitsOfElection(String electionId) {
        System.out.println("The amount of repUnits: " + electionService.getElection(electionId).getRepUnits().size());
        return electionService.getElection(electionId).getRepUnits();
    }
}
