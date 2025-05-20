package com.voteU.election.java.controller;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.services.ElectionDataPersister;
import com.voteU.election.java.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/elections")
public class ElectionDataController {

    private final ElectionDataPersister electionDataPersister;
    private final ElectionService electionService;

    @Autowired
    public ElectionDataController(ElectionDataPersister electionDataPersister, ElectionService electionService) {
        this.electionDataPersister = electionDataPersister;
        this.electionService = electionService;
    }

    @PostMapping("/{electionId}/persist")
    public String saveElectionById(@PathVariable String electionId) {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            return "Election not found with ID: " + electionId;
        }
        electionDataPersister.persistElection(election);
        return "Election saved successfully!";
    }
}
