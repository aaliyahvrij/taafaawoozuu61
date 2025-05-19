package com.voteU.election.java.model;

import lombok.*;

import java.util.*;

@Getter
@Setter
public class Election {
    private String id;
    private final String name;
    private final String date;
    private Map<Integer, Province> provinces;
    private Map<Integer, Constituency> constituencies;
    private final Map<String, Authority> authorities;
    private final Map<String, RepUnit> repUnits;
    private final Map<Integer, Affiliation> affiliations;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.provinces = new HashMap<>();
        this.constituencies = new HashMap<>();
        this.authorities = new HashMap<>();
        this.repUnits = new HashMap<>();
        this.affiliations = new HashMap<>();
    }
}
