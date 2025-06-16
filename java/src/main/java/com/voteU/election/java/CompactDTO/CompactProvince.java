package com.voteU.election.java.CompactDTO;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

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
