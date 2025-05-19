package com.voteU.election.java.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Compact {
    int intId;
    String stringId;
    String name;
    String postCode;
    int votes;
    int partiesSize;

    public Compact(int intId, String name, int votes, int partiesSize) {
        this.intId = intId;
        this.name = name;
        this.votes = votes;
        this.partiesSize = partiesSize;
    }

    public Compact(String stringId, String name, int votes, int partiesSize){
        this.stringId = stringId;
        this.name = name;
        this.votes = votes;
        this.postCode = "";
        this.partiesSize = partiesSize;
    }
}
