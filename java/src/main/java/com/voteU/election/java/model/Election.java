package com.voteU.election.java.model;

import com.voteU.election.java.utils.xml.Transformer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Just a very silly election class that only demonstrates that a {@link Transformer}
 * can return an instance of a class.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */

@Getter @Setter
public class Election {
    private String id;
    private String name;
    private String date;
    private List<Province> provinces;
    private Map<Integer, Party> nationalParties;
    private Map<Integer, Constituency> constituencies;
    private Map<String, Authority> authorities;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.provinces = new ArrayList<>();
        this.nationalParties = new HashMap<>();
        this.constituencies = new HashMap<>();
        this.authorities = new HashMap<>();

    }


    @Override
    public String toString() {
        return "Election {" +
                "\n  id='" + id + '\'' +
                ",\n  name='" + name + '\'' +
                ",\n  date='" + date + '\'' +
                ",\n  nationalParties=" + nationalParties +
                ",\n  contests=" + constituencies +
                "\n}";
    }


}

