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
    private Map<Integer, Party> parties;

    public Constituency(int id,  String name) {
        this.id = id;
        this.name = name;
        this.parties = new HashMap<>();
    }


    @Override
    public String toString() {
        return "Constituency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parties=" + parties +
                '}';
    }
}
