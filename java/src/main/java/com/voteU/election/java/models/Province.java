package com.voteU.election.java.models;

import lombok.*;

import java.util.*;

@Getter
@Setter
public class Province {
    int id;
    String name;
    Map<Integer, Constituency> constituencies;

    public Province() {
        this.constituencies = new HashMap<>();
    }

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.constituencies = new HashMap<>();
    }
}
