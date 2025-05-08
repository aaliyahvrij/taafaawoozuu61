package com.voteU.election.java.model;

import java.util.*;

public class Province {
    int id;
    String name;
    List<Constituency> contests;

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.contests = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Constituency> getContests() {
        return this.contests;
    }
}
