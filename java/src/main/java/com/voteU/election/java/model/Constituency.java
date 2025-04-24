package com.voteU.election.java.model;

import java.util.*;

public class Constituency {
    int id;
    String name;
    Map<Integer, Party> constituencyData;
    Map<String, Authority> authorities;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.constituencyData = new HashMap<>();
        this.authorities = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, Party> getParties() {
        return constituencyData;
    }

    public Map<String, Authority> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return String.format(
                "Constituency[id=%d, name=%s, constituencyData=%s, authorities=%s]",
                id, name, constituencyData, authorities
        );
    }
}
