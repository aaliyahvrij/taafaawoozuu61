package com.voteU.election.java.model;

import lombok.*;

import java.util.*;

@Setter
@Getter
public class Constituency {
    private int id;
    private String name;
    private Map<Integer, Party> parties;
    Map<String, Authority> authorities;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.parties = new HashMap<>();
        this.authorities = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Constituency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parties=" + parties +
                '}';
    }
}
