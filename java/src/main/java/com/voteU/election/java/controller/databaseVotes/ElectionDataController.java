package com.voteU.election.java.controller.databaseVotes;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.services.ElectionDataCSV;
import com.voteU.election.java.services.ElectionDataInserter;
import com.voteU.election.java.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/elections")
public class ElectionDataController {

    private final ElectionDataInserter electionDataInserter;
    private final ElectionDataCSV electionDataCSV;
    private final ElectionService electionService;

    @Autowired
    public ElectionDataController(ElectionDataInserter electionDataInserter, ElectionService electionService,  ElectionDataCSV electionDataCSV) {
        this.electionDataInserter = electionDataInserter;
        this.electionService = electionService;
        this.electionDataCSV = electionDataCSV;
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

    @PostMapping("/{electionId}/csv")
    public String csvElectionById(@PathVariable String electionId) throws IOException {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            return "Election not found with ID: " + electionId;
        }
        String filePath = "C:\\Users\\temp\\authority_candidate_votes_" + electionId + ".csv";
        electionDataCSV.exportAuthorityCandidateVotesToCSV(election, filePath);
        return "Election csv'd  successfully!";
    }
}
