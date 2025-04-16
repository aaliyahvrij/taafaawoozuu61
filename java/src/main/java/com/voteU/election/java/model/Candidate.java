package com.voteU.election.java.model;

public class Candidate {
    int id;
    public String shortCode;
    int partyId;
    String firstName;
    String lastName;
    public int votes;


    public Candidate() {
        this.id = id;
        this.shortCode = "";
        this.firstName = "";
        this.lastName = "";
        this.partyId = partyId;
        this.votes = 0;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getShortCode() {
        return shortCode;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getVotes() {
        return votes;
    }
    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String toString() {
        return String.format("Candidate[id=%d, firstName=%s, lastName=%s, votes=%d, shortCode=%s]", id, firstName, lastName, votes, shortCode);
    }
}
