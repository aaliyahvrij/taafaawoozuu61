package com.voteU.election.java.model;

import java.util.ArrayList;
import java.util.List;

public class Party {
    int id;
    String name;
    List<Candidate> candidates;
    int votes;

    public Party(int id, String name) {
        this.id = id;
        this.name = name;
        this.candidates = new ArrayList<>();
        this.votes = 0;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Candidate> getCandidates() {
        return this.candidates;
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }

    public boolean hasCandidateShortCode(String candidateId) {
        for (Candidate candidate : this.candidates) {
            if (candidate.getShortCode().equals(candidateId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCandidateId(int candidateId) {
        for (Candidate candidate : this.candidates) {
            if (candidate.getId() == candidateId) {
                return true;
            }
        }
        return false;
    }

    public int getVotes() {
        return this.votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return String.format("Party[id=%d, name=%s, votes=%d, candidates=%s]", this.id, this.name, this.votes, this.candidates);
    }
}
