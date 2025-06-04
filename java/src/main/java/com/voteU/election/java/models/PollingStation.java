package com.voteU.election.java.models;

import lombok.Getter;

import java.util.LinkedHashMap;

@Getter
public class PollingStation {
    private final String id;
    private final String name;
    private final LinkedHashMap<Integer, Affiliation> affiliations;
    private final int vvCount;

    public PollingStation(String id, String name, LinkedHashMap<Integer, Affiliation> affiliations, int vvCount) {
        this.id = id;
        this.name = name;
        this.affiliations = affiliations;
        this.vvCount = vvCount;
    }
}
