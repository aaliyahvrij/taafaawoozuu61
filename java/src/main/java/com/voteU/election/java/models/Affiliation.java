package com.voteU.election.java.models;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class Affiliation {
    private final int id;
    private final String name;
    private final LinkedHashMap<Integer, Candidate> candiListLhMap;
    private final int vvCount;

    public Affiliation(int id, String name, int vvCount) {
        this.id = id;
        this.name = name;
        this.candiListLhMap = new LinkedHashMap<>();
        this.vvCount = vvCount;
    }

    public boolean hasCandId(int theId) {
        for (Map.Entry<Integer, Candidate> candiPair : this.candiListLhMap.keySet()) {
            if (candiPair == theId) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCandiShortCode(String candiShortCode) {
        for (Candidate candi : this.candiList) {
            if (candi.getShortCode().equals(candiShortCode)) {
                return true;
            }
        }
        return false;
    }
}
