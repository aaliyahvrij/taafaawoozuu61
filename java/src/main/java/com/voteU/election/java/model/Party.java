package com.voteU.election.java.model;

import java.util.ArrayList;
import java.util.List;

public class Party {
    int id;
    String name;
    int votes;
    List<Candidate> candidates;


    public Party(int id, String name) {
        this.id = id;
        this.name = name;
        this.votes = 0;
        this.candidates = new ArrayList<>();
    }
    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public int getVotes() {
        return votes;
    }
    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<Candidate> getCandidates() {
        return candidates;
    }

    public String toString() {
        return String.format("Party[id=%d, name=%s]", id, name + ", candidates: " + candidates);
    }
}
