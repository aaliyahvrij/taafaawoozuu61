package com.voteU.election.java.models;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Constituency {
    private int id;
    private String name;
    private LinkedHashMap<String, Municipality> muniList_lhMap;
    private LinkedHashMap<Integer, Affiliation> affiList_lhMap;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.muniList_lhMap = new LinkedHashMap<>();
        this.affiList_lhMap = new LinkedHashMap<>();
    }
}
