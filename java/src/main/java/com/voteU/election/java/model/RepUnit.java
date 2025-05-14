package com.voteU.election.java.model;

import lombok.Getter;

import java.util.*;

@Getter
public class RepUnit {
    String id;
    String name;
    List<Party> affiliations;
    int votes;

    public RepUnit(String id, String name, List<Party> affiliations, int votes) {
        this.id = id;
        this.name = name;
        this.affiliations = affiliations;
        this.votes = votes;
    }

    public String toString() {
        return String.format("ReportingUnit[id=%s, name=%s, affiliations=%s, votes=%d]", this.id, this.name, this.affiliations, this.votes);
    }
}
