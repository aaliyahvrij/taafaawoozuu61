package com.voteU.election.java.services;

import com.voteU.election.java.database.responseDTO.PartyVotesDTO;
import com.voteU.election.java.repositories.NationalPartyVotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationalPartyVotesService {

    private final NationalPartyVotesRepository nationalPartyVotesRepository;

    public NationalPartyVotesService(NationalPartyVotesRepository nationalPartyVotesRepository) {
        this.nationalPartyVotesRepository = nationalPartyVotesRepository;
    }

    public List<PartyVotesDTO> getNationalPartyVotesByElectionId(String electionId) {
        return nationalPartyVotesRepository.findNationalPartyVotesByElectionId(electionId);
    }
}
