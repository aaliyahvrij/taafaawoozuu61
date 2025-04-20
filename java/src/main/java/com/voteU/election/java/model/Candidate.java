package com.voteU.election.java.model;

public class Candidate {
    int id;
    int partyId;
    String firstName;
    String lastName;
    int votes;


    public Candidate(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.votes = 0;
    }

    public int getId() {
        return id;
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

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String toString() {
        return String.format("Candidate[id=%d, firstName=%s, lastName=%s, votes=%d]", id, firstName, lastName, votes);
    }

}

