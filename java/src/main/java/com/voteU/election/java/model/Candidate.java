package com.voteU.election.java.model;

public class Candidate {
    int id;
    public String shortCode;
    int partyId;
    String firstName;
    String lastName;
    public int validVotes;

    public Candidate() {
        this.id = id;
        this.shortCode = "";
        this.firstName = "";
        this.lastName = "";
        this.partyId = partyId;
        this.validVotes = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortCode() {
        return this.shortCode;
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
        return String.format(
                "Candidate[id=%d, firstName=%s, lastName=%s, votes=%d, shortCode=%s]",
                this.id, this.firstName, this.lastName, this.validVotes, this.shortCode
        );
    }
}
