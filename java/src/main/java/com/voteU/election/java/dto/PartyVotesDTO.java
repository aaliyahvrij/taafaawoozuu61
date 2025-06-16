package com.voteU.election.java.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyVotesDTO {
    int id;
    private String partyName;
    private int votes;

    public PartyVotesDTO(int id, String partyName, int votes) {
        this.id = id;
        this.partyName = partyName;
        this.votes = votes;
    }

}
