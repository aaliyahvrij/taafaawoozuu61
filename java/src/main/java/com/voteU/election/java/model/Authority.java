package com.voteU.election.java.model;

import java.util.HashMap;
import java.util.Map;

public class Authority {
    String id;
    String name;
    Map<Integer, Party> authorityData;
    int contestId;

    public Authority(String id) {
        this.id = id;
        this.name = "";
        authorityData = new HashMap<>();
        contestId = 0;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Map<Integer, Party> getAuthorityData() {
        return authorityData;
    }

    public int getContestId() {
        return contestId;
    }
    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    @Override
    public String toString() {
        return "id " + id + " name " + name + " authorityData " + authorityData.toString();
    }
}
