package com.voteU.election.java.model;

import java.util.List;

public class Constituency {
    int id;
    String name;
    private List<Candidate> candidates;
    private List<Party> parties;

    public Constituency(int id, List<Party> parties, List<Candidate> candidates, String name) {
        this.id = id;
        this.parties = parties;
        this.candidates = candidates;
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

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
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
                ", candidates=" + candidates +
                ", parties=" + parties +
                '}';
    }
}
