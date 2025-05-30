package com.voteU.election.java.models;

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
    public int vvCount;

    public Candidate(int id, int vvCount) {                           // telling files
        this.id = id;
        this.vvCount = vvCount;
    }

    public Candidate(String shortCode, int vvCount) {                 // totaaltelling files
        this.shortCode = shortCode;
        this.vvCount = vvCount;
    }

    public Candidate(int id, String firstName, String lastName) {   // kandidatenlijst files
        this.id = id;
        this.shortCode = "";
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = "";
        this.localityName = "";
        this.affId = 0;
        this.vvCount = 0;
    }
}
