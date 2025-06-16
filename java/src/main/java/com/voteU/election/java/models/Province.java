package com.voteU.election.java.models;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Province {
    private int id;
    private String name;
    private LinkedHashMap<Integer, Constituency> constiListLhMap;

    public Province() {
        this.constiListLhMap = new LinkedHashMap<>();
    }

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.constiListLhMap = new LinkedHashMap<>();
    }
}
