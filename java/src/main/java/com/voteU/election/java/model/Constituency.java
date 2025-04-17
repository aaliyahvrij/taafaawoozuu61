package com.voteU.election.java.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constituency {
    int id;
    String name;
    private Map<Integer, Party> constituencyData;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.constituencyData = new HashMap<>();
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

    public Map<Integer, Party> getConstituencyData() {
        return constituencyData;
    }

    @Override
    public String toString() {
        return "Constituency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
