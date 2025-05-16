package com.voteU.election.java.model;

import com.voteU.election.java.utils.xml.Transformer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Just a very silly election class that only demonstrates that a {@link Transformer}
 * can return an instance of a class.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */

@Getter
@Setter
public class Election {
    private String id;
    private String name;
    private String date;
    private int votes;
    private Map<Integer, Constituency> constituencies;
    private Map<String, Authority> authorities;
    private Map<Integer, Party> parties;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.votes = 0;
        this.constituencies = new HashMap<>();
        this.authorities = new HashMap<>();
        this.parties = new HashMap<>();

    }
    public void recalculateTotalVotes() {
        int totalVotes = 0;
        for (Party party : this.getParties().values()) {
            totalVotes += party.getVotes();
        }
        this.setVotes(totalVotes);  // Assuming you have setVotes() method
    }


    @Override
    public String toString() {
        return "Election {" + "\n id='" + this.id + '\'' + ",\n  name='" + this.name + '\'' + ",\n  date='" + this.date + '\'' + ",\n  parties=" + this.parties + ",\n  contests=" + this.constituencies + "\n}";
    }
}

