package com.voteU.election.java.models;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
public class PollingStation {
    private final String id;
    private final String name;
    @Setter
    private String zipCode;
    private LinkedHashMap<Integer, Affiliation> affiList_lhMap;
    private int vvCount;

    public PollingStation(String id, String name, String zipCode) {     // For the unit test
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
    }

    public PollingStation(String id, String name, LinkedHashMap<Integer, Affiliation> affiList_lhMap, int vvCount) {
        this.id = id;
        this.name = name;
        this.affiList_lhMap = affiList_lhMap;
        this.vvCount = vvCount;
    }
}
