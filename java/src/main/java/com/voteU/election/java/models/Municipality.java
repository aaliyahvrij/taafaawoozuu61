package com.voteU.election.java.models;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
public class Municipality {
    @Setter
    private String id;
    private final String name;
    @Setter
    private int constId;
    private final LinkedHashMap<String, PollingStation> poStList_lhMap;
    private final LinkedHashMap<Integer, Affiliation> affiList_lhMap;

    public Municipality(String id, String name) {
        this.id = id;
        this.name = name;
        this.constId = 0;
        this.poStList_lhMap = new LinkedHashMap<>();
        this.affiList_lhMap = new LinkedHashMap<>();
    }
}
