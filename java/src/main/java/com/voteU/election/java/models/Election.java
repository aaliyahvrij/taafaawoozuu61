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
    private LinkedHashMap<Integer, Province> proviListLhMap;
    private LinkedHashMap<Integer, Constituency> constiListLhMap;
    private LinkedHashMap<String, Municipality> muniListLhMap;
    private LinkedHashMap<String, PollingStation> poStListLhMap;
    private LinkedHashMap<Integer, Affiliation> affiListLhMap;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.proviListLhMap = new LinkedHashMap<>();
        this.constiListLhMap = new LinkedHashMap<>();
        this.muniListLhMap = new LinkedHashMap<>();
        this.poStListLhMap = new LinkedHashMap<>();
        this.affiListLhMap = new LinkedHashMap<>();
    }
}
