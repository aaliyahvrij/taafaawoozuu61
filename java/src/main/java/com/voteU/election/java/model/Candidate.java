package com.voteU.election.java.model;

import lombok.*;

@Getter
@Setter
public class Candidate {
    private int id;
    public String shortCode;
    private String firstName;
    private String lastName;
    private String gender;
    private String localityName;
    private int affId;
    public int votes;

    public Candidate(int id, String firstName, String lastName) {
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
