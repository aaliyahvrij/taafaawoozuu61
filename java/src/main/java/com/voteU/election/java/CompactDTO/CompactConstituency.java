package com.voteU.election.java.CompactDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompactConstituency {
    int id;
    String name;
    int votes;
    String electionId;
    int provinceId;

    public CompactConstituency(int id, String name){
        this.id = id;
        this.name = name;
        this.votes = votes;
        this.electionId = "";
        this.provinceId = 0;
    }
}
