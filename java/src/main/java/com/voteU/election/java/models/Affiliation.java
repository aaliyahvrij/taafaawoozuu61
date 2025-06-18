package com.voteU.election.java.models;

import lombok.Getter;

import java.util.LinkedHashMap;

@Getter
public class Affiliation {
    private final int id;
    private final String name;
    private final LinkedHashMap<Integer, Candidate> candiListLhMap;
    private final int vvCount;

    public Affiliation(int id, String name, int vvCount) {
        this.id = id;
        this.name = name;
        this.candiListLhMap = new LinkedHashMap<>();
        this.vvCount = vvCount;
    }
}
