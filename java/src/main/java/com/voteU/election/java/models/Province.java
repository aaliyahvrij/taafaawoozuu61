package com.voteU.election.java.models;

import lombok.*;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Province {
    int id;
    String name;
    LinkedHashMap<Integer, Constituency> constituencies;

    public Province() {
        this.constituencies = new LinkedHashMap<>();
    }

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.constituencies = new LinkedHashMap<>();
    }
}
