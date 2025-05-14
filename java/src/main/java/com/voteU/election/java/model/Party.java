package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Party {
    int id;
    String name;
    int votes;
    List<Candidate> candidates;
    public Party(int id, String name) {
        this.id = id;
        this.name = name;
        this.candidates = new ArrayList<>();
        this.votes = 0;
    }

    public void addCandidate(Candidate candidate){
        this.candidates.add(candidate);
    }

    public boolean hasCandidateShortCode(String candidateId){
        for (Candidate candidate : this.candidates) {
            if (candidate.getShortCode().equals(candidateId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCandidateId(int candidateId){
        for (Candidate candidate : this.candidates) {
            if(candidate.getId() ==  candidateId){
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return String.format("Party[id=%d, name=%s, votes=%d, candidates=%s]", this.id, this.name, this.votes, this.candidates);
    }
}
