package com.voteU.election.java.model;

import java.util.ArrayList;
import java.util.List;

public class Contest {
    private int id;
    private String name;
    private List<Party> parties;

    public Contest(int id, String name) {
        this.id = id;
        this.name = name;
        this.parties = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public List<Party> getParties() {
        return this.parties;
    }

    public String toString() {
        return String.format("Contest[id=%d, name=%s]", this.id, this.name + ", parties: " + this.parties);
    }
}
