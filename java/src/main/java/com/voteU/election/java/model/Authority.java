package com.voteU.election.java.model;

import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

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

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Map<Integer, Party> getAuthorityParties() {
        return this.authorityParties;
    }

    public int getContestId() {
        return this.contestId;
    }

    @Override
    public String toString() {
        return "id " + this.id + " name " + this.name + " authorityParties " + this.authorityParties.toString();
    }
}
