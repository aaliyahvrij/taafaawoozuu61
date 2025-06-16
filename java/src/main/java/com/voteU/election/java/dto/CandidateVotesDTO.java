package com.voteU.election.java.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateVotesDTO {
    private String electionId;
    private int partyId;
    private int id;
    private String name;
    private int votes;
    private String gender;
    private String locality;

    public CandidateVotesDTO(String electionId, int partyId, int id, String name, int votes, String gender, String locality) {
        this.electionId = electionId;
        this.partyId = partyId;
        this.id = id;
        this.name = name;
        this.votes = votes;
        this.gender = gender;
        this.locality = locality;
    }

}
