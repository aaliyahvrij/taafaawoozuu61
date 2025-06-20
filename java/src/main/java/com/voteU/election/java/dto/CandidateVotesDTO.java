package com.voteU.election.java.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateVotesDTO {
    String firstName;
    String lastName;
    private Integer votes;

    public CandidateVotesDTO(String firstName, String lastName, Integer votes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.votes = votes;
    }
}
