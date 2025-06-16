package com.voteU.election.java.models;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Constituency {
    private int id;
    private String name;
    private LinkedHashMap<String, Municipality> muniListLhMap;
    private LinkedHashMap<Integer, Affiliation> affiListLhMap;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.muniListLhMap = new LinkedHashMap<>();
        this.affiListLhMap = new LinkedHashMap<>();
    }
}
