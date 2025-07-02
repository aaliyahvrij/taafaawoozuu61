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
    private final PollingStationPartyDataInserter pollingStationPartyDataInserter;
    private final PollingStationDataInserter  pollingStationDataInserter;

    @Autowired
    public ElectionDataController(
            ElectionDataInserter electionDataInserter,
            ElectionService electionService,
            PollingStationPartyDataInserter pollingStationPartyDataInserter,
            PollingStationCandidateDataInserter pollingStationCandidateDataInserter,
            PollingStationDataInserter pollingStationDataInserter
    ) {
        this.electionDataInserter = electionDataInserter;
        this.electionService = electionService;
        this.pollingStationPartyDataInserter = pollingStationPartyDataInserter;
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

    @PostMapping("/{electionId}/insertPCVotes")
    public String insertCandidateVotes(@PathVariable String electionId) {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            return "Election not found with ID: " + electionId;
        }

        int chunkSize = 19_000; // adjust as needed
        pollingStationCandidateDataInserter.insertPollingStationCandidateVotesStreamed(election, chunkSize);

        return "Election votes inserted successfully!";
    }

    @PostMapping("/{electionId}/insertPPVotes")
    public String insertPartyVotes(@PathVariable String electionId) {
        Election election = electionService.getElection(electionId);
        if (election == null) {
            return "Election not found with ID: " + electionId;
        }

        int chunkSize = 10_000; // adjust as needed
        pollingStationPartyDataInserter.insertPollingStationPartyVotesStreamed(election, chunkSize);

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
