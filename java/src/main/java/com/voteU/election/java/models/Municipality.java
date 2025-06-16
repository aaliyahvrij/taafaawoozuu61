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
    private final LinkedHashMap<String, PollingStation> poStListLhMap;
    private final LinkedHashMap<Integer, Affiliation> affiListLhMap;

    public Municipality(String id, String name) {
        this.id = id;
        this.name = name;
        this.constId = 0;
        this.poStListLhMap = new LinkedHashMap<>();
        this.affiListLhMap = new LinkedHashMap<>();
    }
}
