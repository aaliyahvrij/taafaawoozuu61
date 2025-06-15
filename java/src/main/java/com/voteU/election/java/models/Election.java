package com.voteU.election.java.models;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Election {
    private String id;
    private String name;
    private String date;
    private LinkedHashMap<Integer, Province> proviList_lhMap;
    private LinkedHashMap<Integer, Constituency> constiList_lhMap;
    private LinkedHashMap<String, Municipality> muniList_lhMap;
    private LinkedHashMap<String, PollingStation> poStList_lhMap;
    private LinkedHashMap<Integer, Affiliation> affiList_lhMap;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.proviList_lhMap = new LinkedHashMap<>();
        this.constiList_lhMap = new LinkedHashMap<>();
        this.muniList_lhMap = new LinkedHashMap<>();
        this.poStList_lhMap = new LinkedHashMap<>();
        this.affiList_lhMap = new LinkedHashMap<>();
    }
}
