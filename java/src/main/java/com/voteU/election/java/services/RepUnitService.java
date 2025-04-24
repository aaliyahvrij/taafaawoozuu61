package com.voteU.election.java.services;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.ReportingUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class RepUnitService {
    private final Election storedElection;

    public RepUnitService() {
        this.storedElection = ElectionService.storedElection;
    }

    /**
     * Retrieves all stored reporting units (GET).
     */
    public Map<Integer, ReportingUnit> getRepUnits() {
        return storedElection.getRepUnits();
    }
}
