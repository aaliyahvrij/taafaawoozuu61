package com.voteU.election.java.models;

import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

@Getter
public class Affiliation {
    private final int id;
    private final String name;
    private final List<Candidate> candiList;
    private final int vvCount;

    public Affiliation(int id, String name, int vvCount) {
        this.id = id;
        this.name = name;
        this.candiList = new ArrayList<>();
        this.vvCount = vvCount;
    }
}
