package com.voteU.election.java.model;

import com.voteU.election.java.utils.xml.Transformer;
import lombok.*;

import java.util.*;

/**
 * Just a very silly election class that only demonstrates that a {@link Transformer}
 * can return an instance of a class.
 *
 * <b>Please do NOT include this code in your project!</b>
 */
@Getter
@Setter
public class Election {
    private String id;
    private final String name;
    private final String date;
    private final Map<Integer, Constituency> constituencies;
    private final Map<String, Authority> authorities;
    private final Map<String, RepUnit> repUnits;
    private final Map<Integer, Party> affiliations;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.constituencies = new HashMap<>();
        this.authorities = new HashMap<>();
        this.repUnits = new HashMap<>();
        this.affiliations = new HashMap<>();
    }
}
