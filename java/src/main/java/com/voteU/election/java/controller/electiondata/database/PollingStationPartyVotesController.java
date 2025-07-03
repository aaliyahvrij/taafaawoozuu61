package com.voteU.election.java.controller.electiondata.database;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.services.electiondata.database.PollingStationPartyVotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pollingstation-party-votes")
public class PollingStationPartyVotesController {

    private static final Logger logger = LoggerFactory.getLogger(PollingStationPartyVotesController.class);
    private final PollingStationPartyVotesService pollingStationPartyVotesService;

    public PollingStationPartyVotesController(PollingStationPartyVotesService pollingStationPartyVotesService) {
        this.pollingStationPartyVotesService = pollingStationPartyVotesService;
    }

    @GetMapping("/{electionId}/{pollingStationId}")
    public ResponseEntity<List<PartyVotesDTO>> getPartyVotes(
            @PathVariable String electionId,
            @PathVariable String pollingStationId) {

        logger.info("Fetching party votes for electionId={} and pollingStationId={}", electionId, pollingStationId);

        List<PartyVotesDTO> votes = pollingStationPartyVotesService
                .getPollingStationPartyVotesByElectionAndPollingStationId(electionId, pollingStationId);

        return ResponseEntity.ok(votes);
    }
}
