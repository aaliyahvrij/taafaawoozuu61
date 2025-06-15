package com.voteU.election.java.services;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.repositories.electiondata.ConstituencyPartyVotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConstituencyPartyVotesService {

    private final ConstituencyPartyVotesRepository repository;

    public ConstituencyPartyVotesService(ConstituencyPartyVotesRepository repository) {
        this.repository = repository;
    }

    public List<PartyVotesDTO> getPartyVotes(String electionId, Integer constituencyId) {
        List<PartyVotesDTO> votes = repository.findConstituencyPartyVotes(electionId, constituencyId);
        if (votes == null || votes.isEmpty()) {
            throw new ResourceNotFoundException("No votes found for electionId " + electionId + " and constituencyId " + constituencyId);
        }
        return votes;
    }

    public List<PartyVotesDTO> getPartyVotesSortedByVotes(String electionId, Integer constituencyId) {
        List<PartyVotesDTO> votes = repository.findConstituencyPartyVotesSortedByVotes(electionId, constituencyId);
        if (votes == null || votes.isEmpty()) {
            throw new ResourceNotFoundException("No votes found for electionId " + electionId + " and constituencyId " + constituencyId);
        }
        return votes;
    }

    public List<PartyVotesDTO> getPartyVotesSortedByName(String electionId, Integer constituencyId) {
        List<PartyVotesDTO> votes = repository.findConstituencyPartyVotesSortedByName(electionId, constituencyId);
        if (votes == null || votes.isEmpty()) {
            throw new ResourceNotFoundException("No votes found for electionId " + electionId + " and constituencyId " + constituencyId);
        }
        return votes;
    }
}
