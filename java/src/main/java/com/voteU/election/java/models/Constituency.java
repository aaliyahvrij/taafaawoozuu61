package com.voteU.election.java.models;

import lombok.*;

import java.util.*;

@Getter
@Setter
public class Constituency {
    private int id;
    private String name;
    private Map<Integer, Affiliation> affiliations;
    Map<String, Municipality> municipalities;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.affiliations = new HashMap<>();
        this.municipalities = new HashMap<>();
    }
}
