package com.voteU.election.java.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Candidate {
    @Id
    private int id;
    private String shortCode;
    private String firstName;
    private String lastName;
    private int votes;
    private int partyId;



    public Candidate() {
        this.id = 0;
        this.shortCode = "";
        this.firstName = "";
        this.lastName = "";
        this.votes = 0;
    }
    public String toString() {
        return String.format("Candidate[id=%d, firstName=%s, lastName=%s, votes=%d, shortCode=%s]", this.id, this.firstName, this.lastName, this.votes, this.shortCode);
    }
}
