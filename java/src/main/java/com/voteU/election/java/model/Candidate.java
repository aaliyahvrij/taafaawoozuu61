package com.voteU.election.java.model;

import lombok.*;

@Getter
@Setter
public class Candidate {
    private int id;
    public String shortCode;
    private String firstName;
    private String lastName;
    private int affId;
    public int validVotes;

    public Candidate() {
        this.id = 0;
        this.shortCode = "";
        this.firstName = "";
        this.lastName = "";
        this.affId = 0;
        this.validVotes = 0;
    }

    public String toString() {
        return String.format("Candidate[id=%d, shortCode=%s, firstName=%s, lastName=%s, affId=%d, validVotes=%d]", this.id, this.shortCode, this.firstName, this.lastName, this.affId, this.validVotes);
    }
}
