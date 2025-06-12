package com.voteU.election.java.models;

import lombok.*;

import java.util.LinkedHashMap;

@Getter
public class Municipality {
    @Setter
    private String id;
    @Setter
    private String name;
    @Setter
    private int constId;
    private final LinkedHashMap<String, PollingStation> poStList_lhMap;
    private final LinkedHashMap<Integer, Affiliation> affiList_lhMap;

    public Municipality(String id) {
        this.id = id;
        this.name = "";
        this.constId = 0;
        this.poStList_lhMap = new LinkedHashMap<>();
        this.affiList_lhMap = new LinkedHashMap<>();
    }
}
