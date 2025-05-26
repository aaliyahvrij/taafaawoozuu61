package com.voteU.election.java.services;

import com.voteU.election.java.database.responseDTO.PartyVotesDTO;
import com.voteU.election.java.repositories.NationalPartyVotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationalVotesService {

    private final NationalPartyVotesRepository nationalPartyVotesRepository;

    public NationalVotesService(NationalPartyVotesRepository nationalPartyVotesRepository) {
        this.nationalPartyVotesRepository = nationalPartyVotesRepository;
    }

    public List<PartyVotesDTO> getVotesByElection(String electionId) {
        return  nationalPartyVotesRepository.findPartyVotesByElection(electionId);
    }
}
