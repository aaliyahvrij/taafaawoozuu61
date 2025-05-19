package com.voteU.election.java.model;

import lombok.*;

@Getter
@Setter
public class Candidate {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private String localityName;
    private int partyId;
    private int votes;

    public Candidate() {
        this.id = 0;
        this.firstName = "";
        this.lastName = "";
        this.gender = "";
        this.localityName = "";
        this.partyId = 0;
        this.votes = 0;
    }
}
