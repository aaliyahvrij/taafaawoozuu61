package com.voteU.election.java.controller.database;

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
    private final PollingStationCandidateDataInserter pollingStationCandidateDataInserter;
    private final PollingStationDataInserter  pollingStationDataInserter;

    @Autowired
    public ElectionDataController(
            ElectionDataInserter electionDataInserter,
            ElectionService electionService,
            PollingStationCandidateDataInserter pollingStationCandidateDataInserter,
            PollingStationDataInserter pollingStationDataInserter
    ) {
        this.electionDataInserter = electionDataInserter;
        this.electionService = electionService;
        this.pollingStationCandidateDataInserter = pollingStationCandidateDataInserter;
        this.pollingStationDataInserter = pollingStationDataInserter;
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

    @PostMapping("/{electionId}/insertVotes")
    public String insertFlattenedVotes(@PathVariable String electionId) {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            return "Election not found with ID: " + electionId;
        }

        int chunkSize = 50_000; // adjust as needed
        pollingStationCandidateDataInserter.insertPollingStationCandidateVotesStreamed(election, chunkSize);

        return "Election votes inserted successfully!";
    }

    @PostMapping("/{electionId}/insertStations")
    public String insertStations(@PathVariable String electionId) {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            return "Election not found with ID: " + electionId;
        }

        int chunkSize = 500; // adjust as needed
        pollingStationDataInserter.insertPollingStations(election, chunkSize);

        return "Election votes inserted successfully!";
    }

}
