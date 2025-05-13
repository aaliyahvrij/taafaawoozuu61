package com.voteU.election.java.model;

import lombok.*;

public class Candidate {
    @Setter
    @Getter
    private int id;
    @Getter
    public String shortCode;
    private int affId;
    private String firstName;
    private String lastName;
    public int validVotes;

    public Candidate() {
        this.id = 0;
        this.shortCode = "";
        this.firstName = "";
        this.lastName = "";
        this.affId = 0;
        this.validVotes = 0;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getValidVotes() {
        return this.validVotes;
    }

    public void setValidVotes(int validVotes) {
        this.validVotes = validVotes;
    }

    public String toString() {
        return String.format("Candidate[id=%d, firstName=%s, lastName=%s, votes=%d, shortCode=%s]", this.id, this.firstName, this.lastName, this.validVotes, this.shortCode);
    }
}
