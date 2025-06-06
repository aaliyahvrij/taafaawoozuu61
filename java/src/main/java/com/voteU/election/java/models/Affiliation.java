package com.voteU.election.java.models;

import lombok.*;

import java.util.List;
import java.util.ArrayList;

@Getter
public class Affiliation {
    private final int id;
    private final String name;
    private final List<Candidate> candidates;
    private final int vvCount;

    public Affiliation(int id, String name, int vvCount) {
        this.id = id;
        this.name = name;
        this.candidates = new ArrayList<>();
        this.vvCount = vvCount;
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
