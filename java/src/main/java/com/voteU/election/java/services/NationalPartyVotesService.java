package com.voteU.election.java.services;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.repositories.electiondata.NationalPartyVotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationalPartyVotesService {

    private final NationalPartyVotesRepository nationalPartyVotesRepository;

    public NationalPartyVotesService(NationalPartyVotesRepository nationalPartyVotesRepository) {
        this.nationalPartyVotesRepository = nationalPartyVotesRepository;
    }

    public List<PartyVotesDTO> getNationalPartyVotes(String electionId) {
        return nationalPartyVotesRepository.findNationalPartyVotesByElectionId(electionId);
    }
}
