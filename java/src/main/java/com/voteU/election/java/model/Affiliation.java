package com.voteU.election.java.model;

import lombok.*;

import java.util.*;

@Getter
public class Affiliation {
    int id;
    String name;
    List<Candidate> candidates;
    int votes;

    public Affiliation(int id, String name, int votes) {
        this.id = id;
        this.name = name;
        this.candidates = new ArrayList<>();
        this.votes = votes;
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }

    public boolean hasCandiShortCode(String candiShortCode) {
        for (Candidate candidate : this.candidates) {
            if (candidate.getShortCode().equals(candiShortCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCandId(int candId) {
        for (Candidate candidate : this.candidates) {
            if (candidate.getId() == candId) {
                return true;
            }
        }
        return false;
    }
}
