package com.voteU.election.java.model;

import lombok.*;

import java.util.*;

@Getter
public class Authority {
    @Setter
    private String id;
    @Setter
    private String name;
    @Setter
    private int constId;
    private final Map<String, RepUnit> repUnits;
    private final Map<Integer, Party> affiliations;

    public Authority(String id) {
        this.id = id;
        this.name = "";
        this.constId = 0;
        this.repUnits = new HashMap<>();
        this.affiliations = new HashMap<>();
    }
}
