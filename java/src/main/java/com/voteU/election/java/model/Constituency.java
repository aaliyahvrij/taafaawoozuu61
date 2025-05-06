package com.voteU.election.java.model;

import lombok.Getter;

import java.util.*;

public class Constituency {
    @Getter
    private int id;
    private String name;
    private Map<Integer, Party> constituencyData;
    private Map<String, Authority> authorities;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.constituencyData = new HashMap<>();
        this.authorities = new HashMap<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, Party> getParties() {
        return this.constituencyData;
    }

    public Map<String, Authority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String toString() {
        return String.format(
                "Constituency[id=%d, name=%s, constituencyData=%s, authorities=%s]",
                id, name, constituencyData, authorities
        );
    }
}
