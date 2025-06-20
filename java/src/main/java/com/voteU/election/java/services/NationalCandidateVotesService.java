package com.voteU.election.java.services;

import com.voteU.election.java.dto.CandidateVotesDTO;
import com.voteU.election.java.repositories.electiondata.NationalCandidateVotesRepository;

import java.util.List;

public class NationalCandidateVotesService {
    private final NationalCandidateVotesRepository nationalCandidateVotesRepository;

    public NationalCandidateVotesService(NationalCandidateVotesRepository nationalCandidateVotesRepository) {
        this.nationalCandidateVotesRepository = nationalCandidateVotesRepository;
    }

    public List<CandidateVotesDTO> getCandidateVotesByParty(String electionId, Integer partyId) {
        return nationalCandidateVotesRepository.findAllCandidateVotesByParty(electionId, partyId);
    }
}
