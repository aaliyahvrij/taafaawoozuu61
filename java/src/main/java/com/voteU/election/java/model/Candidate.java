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
    private int votes;

    public Candidate() {
        this.id = 0;
        this.shortCode = "";
        this.firstName = "";
        this.lastName = "";
        this.gender = "";
        this.localityName = "";
        this.affId = 0;
        this.votes = 0;
    }
}
