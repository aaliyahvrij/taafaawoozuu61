package com.voteU.election.java.models;

import lombok.*;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Constituency {
    private int id;
    private String name;
    private LinkedHashMap<String, Municipality> muniListMap;
    private LinkedHashMap<Integer, Affiliation> affiListMap;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.muniListMap = new LinkedHashMap<>();
        this.affiListMap = new LinkedHashMap<>();
    }
}
