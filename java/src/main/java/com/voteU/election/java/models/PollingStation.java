package com.voteU.election.java.models;

import lombok.Getter;

import java.util.*;

@Getter
public class PollingStation {
    String id;
    String name;
    Map<Integer, Affiliation> affiliations;
    int votes;

    public PollingStation(String id, String name, Map<Integer, Affiliation> affiliations, int votes) {
        this.id = id;
        this.name = name;
        this.affiliations = affiliations;
        this.votes = votes;
    }
}
