package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class Authority {
    String id;
    String name;
    Map<Integer, Party> authorityParties;
    int constituencyId;

    public Authority(String id) {
        this.id = id;
        this.name = "";
        this.authorityParties = new HashMap<>();
        this.constituencyId = 0;
    }

    @Override
    public String toString() {
        return "id " + this.id + " name " + this.name + " authorityData " + this.authorityParties.toString();
    }
}
