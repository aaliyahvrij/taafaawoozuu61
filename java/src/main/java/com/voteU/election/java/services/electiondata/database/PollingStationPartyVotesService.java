package com.voteU.election.java.services.electiondata.database;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.repositories.electiondata.PollingStationPartyVotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PollingStationPartyVotesService {


    private final PollingStationPartyVotesRepository pollingStationPartyVotesRepository;

    public PollingStationPartyVotesService(PollingStationPartyVotesRepository pollingStationPartyVotesRepository) {
        this.pollingStationPartyVotesRepository = pollingStationPartyVotesRepository;
    }

    public List<PartyVotesDTO> getPollingStationPartyVotesByElectionAndPollingStationId(String electionId, String pollingStationId) {
        List<PartyVotesDTO> votes = pollingStationPartyVotesRepository.findPollingStationPartyVotesByElectionAndPollingStationId(electionId, pollingStationId);

        if (votes == null || votes.isEmpty()) {
            throw new ResourceNotFoundException("No party votes found for polling station ID " + pollingStationId + " and election ID " + electionId);
        }

        log.info("Found {} party votes for pollingStationId={} and electionId={}", votes.size(), pollingStationId, electionId);
        return votes;
    }
}
