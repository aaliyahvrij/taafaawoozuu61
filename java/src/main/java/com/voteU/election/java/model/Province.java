package com.voteU.election.java.model;

import lombok.*;

import java.util.*;

public class Province {
    @Getter
    @Setter
    int id;
    @Getter
    @Setter
    String name;
    @Getter
    List<Constituency> contests;

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.contests = new ArrayList<>();
    }
}
