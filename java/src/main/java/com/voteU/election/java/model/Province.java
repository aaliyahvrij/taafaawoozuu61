package com.voteU.election.java.model;

import lombok.*;

import java.util.*;

@Getter
public class Province {
    @Setter
    int id;
    @Setter
    String name;
    List<Constituency> constituencies;

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.constituencies = new ArrayList<>();
    }
}
