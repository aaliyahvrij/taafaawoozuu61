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
        this.candidates = new ArrayList<>();
        this.votes = 0;
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
    public void addCandidate(Candidate candidate){
        candidates.add(candidate);
    }
    public boolean hasCandidateShortCode(String candidateId){
        for (Candidate candidate : candidates) {
            if (candidate.getShortCode().equals(candidateId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCandidateId(int candidateId){
        for (Candidate candidate : candidates) {
            if(candidate.getId() ==  candidateId){
                return true;
            }
        }
        return false;
    }
    public int getVotes() {
        return votes;
    }
    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String toString() {
        return String.format("Party[id=%d, name=%s, votes=%d, candidates=%s]", id, name, votes, candidates);
    }
}
