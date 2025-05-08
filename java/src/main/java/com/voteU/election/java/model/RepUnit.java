package com.voteU.election.java.model;

import java.util.*;

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

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Party> getAffiliations() {
        return this.affiliations;
    }

    public int getTotalVotes() {
        return this.totalVotes;
    }

    public String toString() {
        return String.format("ReportingUnit[id=%d, name=%s]", id, name + ", parties: " + this.affiliations + " - total votes: " + this.totalVotes);
    }
}
