package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Authority {
    @Setter
    private String id;
    @Setter
    private String name;
    private Map<Integer, Party> authorityParties;
    @Setter
    private int contestId;

    public Authority(String id) {
        this.id = id;
        this.name = "";
        this.authorityParties = new HashMap<>();
        this.contestId = 0;
    }

    @Override
    public String toString() {
        return "id " + this.id + " name " + this.name + " authorityParties " + this.authorityParties.toString();
    }
}
