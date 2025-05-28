package com.voteU.election.java.models;

import lombok.*;

import java.util.*;

@Getter
public class Municipality {
    @Setter
    private String id;
    @Setter
    private String name;
    @Setter
    private int constId;
    private final Map<String, PollingStation> pollingStations;
    private final Map<Integer, Affiliation> affiliations;

    public Municipality(String id) {
        this.id = id;
        this.name = "";
        this.constId = 0;
        this.pollingStations = new HashMap<>();
        this.affiliations = new HashMap<>();
    }
}
