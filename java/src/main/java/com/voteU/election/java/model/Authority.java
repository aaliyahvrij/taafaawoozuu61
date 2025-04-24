package com.voteU.election.java.model;

import java.util.HashMap;
import java.util.Map;

public class Authority {
    String id;
    String name;
    Map<Integer, Party> authorityParties;
    int contestId;

    public Authority(String id) {
        this.id = id;
        this.name = "";
        this.authorityParties = new HashMap<>();
        this.contestId = 0;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, Party> getAuthorityParties() {
        return this.authorityParties;
    }

    public int getContestId() {
        return this.contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    @Override
    public String toString() {
        return "id " + this.id + " name " + this.name + " authorityParties " + this.authorityParties.toString();
    }
}
