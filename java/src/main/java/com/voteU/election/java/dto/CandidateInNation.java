package com.voteU.election.java.dto;

import com.voteU.election.java.model.Party;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateInNation {
    private String shorCode = "";
    private int votes = 0;
    private Party party;

    public CandidateInNation(){

    }


}