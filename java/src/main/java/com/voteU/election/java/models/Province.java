package com.voteU.election.java.models;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class Province {
    private int id;
    private String name;
    private LinkedHashMap<Integer, Constituency> constiList_lhMap;

    public Province() {
        this.constiList_lhMap = new LinkedHashMap<>();
    }

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.constiList_lhMap = new LinkedHashMap<>();
    }
}
