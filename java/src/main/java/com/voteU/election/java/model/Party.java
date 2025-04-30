package com.voteU.election.java.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Party {
    @Id
    @Getter @Setter
    @Column(name = "party_id")
    int id;

    @Getter @Setter
    String name;

    @Getter @Setter
    int votes;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    List<Candidate> candidates;

    @ManyToOne
    @JoinColumn(name = "election_id")  // Foreign key to Election
    @Getter @Setter
    private Election election;  // Reference to the Election in which this Party participates

    public Party() {
        this.candidates = new ArrayList<>();
    }

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
