package com.voteU.election.java.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Candidate {
    private int id;
    private String shortCode;
    private String firstName;
    private String lastName;
    private int votes;
    private int partyId;
    private String gender;
    private String locality;





    public Candidate() {
        this.id = 0;
        this.shortCode = "";
        this.firstName = "";
        this.lastName = "";
        this.votes = 0;
        this.gender = "";
        this.locality = "";
    }
    public String toString() {
        return String.format("Candidate[id=%d, firstName=%s, lastName=%s, votes=%d, shortCode=%s, gender=%s, locality=%s]", this.id, this.firstName, this.lastName, this.votes, this.shortCode, this.gender, this.locality);
    }
}
