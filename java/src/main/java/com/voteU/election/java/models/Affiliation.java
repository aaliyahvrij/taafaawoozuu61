package com.voteU.election.java.models;

import lombok.*;

import java.util.List;
import java.util.ArrayList;

@Getter
public class Affiliation {
    private final int id;
    private final String name;
    private final List<Candidate> candiList;
    private final int vvCount;

    public Affiliation(int id, String name, int vvCount) {
        this.id = id;
        this.name = name;
        this.candiList = new ArrayList<>();
        this.vvCount = vvCount;
    }

    public void addCandi(Candidate candidate) {
        candiList.add(candidate);
    }

    public boolean hasCandiShortCode(String candiShortCode) {
        for (Candidate candi : this.candiList) {
            if (candi.getShortCode().equals(candiShortCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCandId(int candId) {
        for (Candidate candi : this.candiList) {
            if (candi.getId() == candId) {
                return true;
            }
        }
        return false;
    }
}
