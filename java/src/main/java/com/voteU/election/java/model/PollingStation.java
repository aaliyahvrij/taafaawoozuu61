package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class PollingStation {

    private String id;
    private String name;
    private String zipCode;
    private int votes;
    private String electionId;
    private String authorityId;
    private Map<Integer, Party> parties;

    public PollingStation(String id, String name, String zipCode) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
        this.votes = 0;
        this.parties = new HashMap<>();
        this.authorityId = "";
    }


    @Override
    public String toString() {
        return "PollingStation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", parties=" + parties +
                '}';
    }
}
