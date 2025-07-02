package com.voteU.election.java.dtoCompact;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompactElection {
    String stringId;
    String name;
    int votes;
    int partiesSize;

    public CompactElection(String stringId, String name, int votes, int partiesSize){
        this.stringId = stringId;
        this.name = name;
        this.votes = votes;
        this.partiesSize = partiesSize;
    }
}
