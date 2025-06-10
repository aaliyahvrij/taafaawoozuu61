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
    private final LinkedHashMap<String, PollingStation> poStListMap;
    private final LinkedHashMap<Integer, Affiliation> affiListMap;

    public Municipality(String id) {
        this.id = id;
        this.name = "";
        this.constId = 0;
        this.poStListMap = new LinkedHashMap<>();
        this.affiListMap = new LinkedHashMap<>();
    }
}
