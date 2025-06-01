package com.voteU.election.java.models;

import lombok.*;

import java.util.*;

@Getter
@Setter
public class Constituency {
    private int id;
    private String name;
    Map<String, Municipality> municipalities;
    private Map<Integer, Affiliation> affiliations;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.municipalities = new HashMap<>();
        this.affiliations = new HashMap<>();
    }
}
