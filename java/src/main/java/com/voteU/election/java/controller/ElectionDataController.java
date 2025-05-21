package com.voteU.election.java.controller;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.services.ElectionDataInserter;
import com.voteU.election.java.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/elections")
public class ElectionDataController {

    private final ElectionDataInserter electionDataInserter;
    private final ElectionService electionService;

    @Autowired
    public ElectionDataController(ElectionDataInserter electionDataInserter, ElectionService electionService) {
        this.electionDataInserter = electionDataInserter;
        this.electionService = electionService;
    }

    @PostMapping("/{electionId}/persist")
    public String saveElectionById(@PathVariable String electionId) {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            return "Election not found with ID: " + electionId;
        }
        electionDataInserter.insertElection(election);
        return "Election saved successfully!";
    }
}
