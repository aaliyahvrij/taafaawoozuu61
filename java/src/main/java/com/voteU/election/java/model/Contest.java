package com.voteU.election.java.model;

import java.util.ArrayList;
import java.util.List;

public class Contest {
    int id;
    String name;
    List<Party> parties;

    public Contest(int id, String name) {
        this.id = id;
        this.name = name;
        this.parties = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<Party> getParties() {
        return parties;
    }

    public String toString() {
        return String.format("Contest[id=%d, name=%s]", id, name + ", parties: " + parties);
    }


}
