package com.voteU.election.java.model;

import lombok.Getter;

import java.util.*;

@Getter
public class RepUnit {
    String id;
    String name;
    Map<Integer, Party> affiliations;
    int votes;

    public RepUnit(String id, String name, Map<Integer, Party> affiliations, int votes) {
        this.id = id;
        this.name = name;
        this.affiliations = affiliations;
        this.votes = votes;
    }
}
