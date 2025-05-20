package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Constituency {
    private int id;
    private String name;
    private int votes;
    private Map<Integer, Party> parties;
    Map<String, Authority> authorities;
    String electionId;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
        this.votes = 0;
        this.parties = new HashMap<>();
        this.authorities = new HashMap<>();
        this.electionId = "";
    }


    @Override
    public String toString() {
        return "Constituency{" + "id=" + id + ", name='" + name + '\'' + ", parties=" + parties + '}';
    }
}
