package com.voteU.election.java.services;

import com.voteU.election.java.database.responseDTO.PartyVotesDTO;
import com.voteU.election.java.repositories.AuthorityPartyVotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityPartyVotesService {

    private final AuthorityPartyVotesRepository repository;

    @Autowired
    public AuthorityPartyVotesService(AuthorityPartyVotesRepository repository) {
        this.repository = repository;
    }


    public List<PartyVotesDTO> getPartyVotesByAuthority(String authorityId, String electionId) {
        return repository.findPartyVotesByAuthority(authorityId, electionId);
    }
}
