package com.voteU.election.java.model;

public class Candidate {
    int id;
    public String shortCode;
    String firstName;
    String lastName;
    int votes;


    public Candidate() {
        this.id = id;
        this.shortCode = "";
        this.firstName = firstName;
        this.lastName = lastName;
        this.votes = 0;
    }
    public int getId() {
        return id;
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
