package com.voteU.election.java.models;

import lombok.*;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Election {
    private String id;
    private final String name;
    private final String date;
    private LinkedHashMap<Integer, Province> provinces;
    private LinkedHashMap<Integer, Constituency> constituencies;
    private final LinkedHashMap<String, Municipality> municipalities;
    private final LinkedHashMap<String, PollingStation> pollingStations;
    private final LinkedHashMap<Integer, Affiliation> affiliations;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.provinces = new LinkedHashMap<>();
        this.constituencies = new LinkedHashMap<>();
        this.municipalities = new LinkedHashMap<>();
        this.pollingStations = new LinkedHashMap<>();
        this.affiliations = new LinkedHashMap<>();
    }
}
