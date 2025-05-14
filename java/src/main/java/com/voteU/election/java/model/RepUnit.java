package com.voteU.election.java.model;

import lombok.Getter;

import java.util.*;

@Getter
public class RepUnit {
    String id;
    String name;
    List<Party> affiliations;
    int totalVotes;

    public RepUnit(String id, String name, List<Party> affiliations, int totalVotes) {
        this.id = id;
        this.name = name;
        this.affiliations = affiliations;
        this.totalVotes = totalVotes;
    }

    public String toString() {
        return String.format("ReportingUnit[id=%s, name=%s, affiliations=%s, totalVotes=%d]", this.id, this.name, this.affiliations, this.totalVotes);
    }
}
