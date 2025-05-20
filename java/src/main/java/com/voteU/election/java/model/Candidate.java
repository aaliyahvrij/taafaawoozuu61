package com.voteU.election.java.model;

import lombok.*;

@Getter
@Setter
public class Candidate {
    private int id;
    private String shortCode;
    private String firstName;
    private String lastName;
    private String gender;
    private String localityName;
    private int affId;
    public int votes;

    public Candidate(int id, int votes) {                           // telling files
        this.id = id;
        this.votes = votes;
    }

    public Candidate(String shortCode, int votes) {                 // totaaltelling files
        this.shortCode = shortCode;
        this.votes = votes;
    }

    public Candidate(int id, String firstName, String lastName) {   // kandidatenlijst files
        this.id = id;
        this.shortCode = "";
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = "";
        this.localityName = "";
        this.affId = 0;
        this.votes = 0;
    }
}
