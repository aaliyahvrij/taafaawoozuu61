package com.voteU.election.java.models;

import lombok.*;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Constituency {
    private int id;
    private String name;
    LinkedHashMap<String, Municipality> municipalities;
    private LinkedHashMap<Integer, Affiliation> affiliations;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.municipalities = new LinkedHashMap<>();
        this.affiliations = new LinkedHashMap<>();
    }
}
