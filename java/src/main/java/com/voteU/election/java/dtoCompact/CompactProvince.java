package com.voteU.election.java.dtoCompact;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompactProvince {
    int id;
    String name;


    public CompactProvince(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
