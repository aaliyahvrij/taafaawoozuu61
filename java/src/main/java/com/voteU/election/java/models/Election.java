package com.voteU.election.java.models;

import lombok.*;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Election {
    private String id;
    private String name;
    private String date;
    private LinkedHashMap<Integer, Province> proviListMap;
    private LinkedHashMap<Integer, Constituency> constiListMap;
    private LinkedHashMap<String, Municipality> muniListMap;
    private LinkedHashMap<String, PollingStation> poStListMap;
    private LinkedHashMap<Integer, Affiliation> affiListMap;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.proviListMap = new LinkedHashMap<>();
        this.constiListMap = new LinkedHashMap<>();
        this.muniListMap = new LinkedHashMap<>();
        this.poStListMap = new LinkedHashMap<>();
        this.affiListMap = new LinkedHashMap<>();
    }
}
