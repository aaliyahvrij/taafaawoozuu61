package com.voteU.election.java.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDetailsDTO {
    private String partyName;
    private String firstName;
    private String lastName;
    private int votes;
    private String gender;
    private String locality;

    public CandidateDetailsDTO(String partyName, String firstName, String lastName, int votes, String gender, String locality) {
        this.partyName = partyName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.votes = votes;
        this.gender = gender;
        this.locality = locality;
    }

}
