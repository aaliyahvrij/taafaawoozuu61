package com.voteU.election.java.model;

import java.util.List;

public class Constituency {
    int id;
    String name;
    private List<Party> parties;

    public Constituency(int id, List<Party> parties, String name) {
        this.id = id;
        this.parties = parties;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
