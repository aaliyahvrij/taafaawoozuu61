package com.voteU.election.java.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Candidate {
    @Id
    @Getter @Setter
    int id;

    @Getter @Setter
    public String shortCode;

    @Getter @Setter
    String firstName;

    @Getter @Setter
    String lastName;

    @Getter @Setter
    public int validVotes;

    @ManyToOne
    @JoinColumn(name = "party_id")  // Foreign key column in the Candidate table
    private Party party;  // The Party this candidate belongs to


    public Candidate() {
        this.id = 0;
        this.shortCode = "";
        this.firstName = "";
        this.lastName = "";
        this.validVotes = 0;
    }
    public String toString() {
        return String.format("Candidate[id=%d, firstName=%s, lastName=%s, votes=%d, shortCode=%s]", this.id, this.firstName, this.lastName, this.validVotes, this.shortCode);
    }
}
