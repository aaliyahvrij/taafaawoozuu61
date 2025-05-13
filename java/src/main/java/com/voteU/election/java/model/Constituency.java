package com.voteU.election.java.model;

import lombok.*;

import java.util.*;

@Getter
@Setter
public class Constituency {
    private int id;
    private String name;
    private Map<Integer, Party> affiliations;
    Map<String, Authority> authorities;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.affiliations = new HashMap<>();
        this.authorities = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Constituency{" + "id=" + this.id + ", name='" + this.name + '\'' + ", parties=" + this.affiliations + '}';
    }
}
