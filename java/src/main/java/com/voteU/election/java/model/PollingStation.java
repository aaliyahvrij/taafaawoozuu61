package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a polling station within an election environment.
 * A polling station belongs to a specific authority and contains details
 * about its unique identifier, name, voting information, and associated parties.
 *
 * This class also facilitates the management of votes for its associated parties.
 */
@Setter
@Getter
public class PollingStation {

    private String id;
    private String name;
    private String zipCode;
    private int votes;
    private Map<Integer, Party> parties;
    private String authorityId;

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
