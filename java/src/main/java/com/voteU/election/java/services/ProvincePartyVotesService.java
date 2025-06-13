package com.voteU.election.java.services;

import com.voteU.election.java.dto.PartyVotesDTO;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.repositories.electiondata.ProvincePartyVotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvincePartyVotesService {

    private final ProvincePartyVotesRepository repository;

    public ProvincePartyVotesService(ProvincePartyVotesRepository repository) {
        this.repository = repository;
    }

    public List<PartyVotesDTO> getPartyVotes(String electionId, Integer provinceId) {
        List<PartyVotesDTO> votes = repository.findProvincePartyVotes(electionId, provinceId);
        if (votes == null || votes.isEmpty()) {
            throw new ResourceNotFoundException("No votes found for electionId " + electionId + " and provinceId " + provinceId);
        }
        return votes;
    }

    public List<PartyVotesDTO> getPartyVotesSortedByVotes(String electionId, Integer provinceId) {
        List<PartyVotesDTO> votes = repository.findProvincePartyVotesSortedByVotes(electionId, provinceId);
        if (votes == null || votes.isEmpty()) {
            throw new ResourceNotFoundException("No votes found for electionId " + electionId + " and provinceId " + provinceId);
        }
        return votes;
    }

    public List<PartyVotesDTO> getPartyVotesSortedByName(String electionId, Integer provinceId) {
        List<PartyVotesDTO> votes = repository.findProvincePartyVotesSortedByName(electionId, provinceId);
        if (votes == null || votes.isEmpty()) {
            throw new ResourceNotFoundException("No votes found for electionId " + electionId + " and provinceId " + provinceId);
        }
        return votes;
    }
}
