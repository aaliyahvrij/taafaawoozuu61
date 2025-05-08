package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Party {
    int id;
    String name;
    List<Candidate> candidates;
    @Setter
    int votes;

    public Party(int id, String name) {
        this.id = id;
        this.name = name;
        this.candidates = new ArrayList<>();
        this.votes = 0;
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

    @Override
    public String toString() {
        return String.format("Party[id=%d, name=%s, votes=%d, candidates=%s]", this.id, this.name, this.votes, this.candidates);
    }
}
