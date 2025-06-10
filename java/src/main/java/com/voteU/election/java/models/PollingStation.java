package com.voteU.election.java.models;

import lombok.Getter;

import java.util.LinkedHashMap;

@Getter
public class PollingStation {
    private final String id;
    private final String name;
    private final String zipCode;
    private final LinkedHashMap<Integer, Affiliation> affiliations;
    private final int vvCount;

    public PollingStation(String id, String name, String zipCode, LinkedHashMap<Integer, Affiliation> affiliations, int vvCount) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
        this.affiliations = affiliations;
        this.vvCount = vvCount;
    }
}
