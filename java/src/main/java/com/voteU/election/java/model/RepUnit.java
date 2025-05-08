package com.voteU.election.java.model;

import lombok.Getter;

import java.util.*;

@Getter
public class RepUnit {
    int id;
    String name;
    List<Party> affiliations;
    int totalVotes;

    public RepUnit(int id, String name, int totalVotes) {
        this.id = id;
        this.name = name;
        this.affiliations = new ArrayList<>();
        this.totalVotes = totalVotes;
    }

    public String toString() {
        return String.format("ReportingUnit[id=%d, name=%s]", id, name + ", parties: " + this.affiliations + " - total votes: " + this.totalVotes);
    }
}
