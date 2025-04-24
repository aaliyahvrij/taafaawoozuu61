package com.voteU.election.java.model;

public class Candidate {
    int id;
    public String shortCode;
    String firstName;
    String lastName;
    int validVotes;

    public Candidate() {
        this.id = id;
        this.shortCode = "";
        this.firstName = firstName;
        this.lastName = lastName;
        this.validVotes = 0;
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

    public int getValidVotes() {
        return validVotes;
    }

    public void setVotes(int votes) {
        this.validVotes = votes;
    }

    public String toString() {
        return String.format("Candidate[id=%d, firstName=%s, lastName=%s, validVotes=%d, shortCode=%s]", id, firstName, lastName, validVotes, shortCode);
    }
}
