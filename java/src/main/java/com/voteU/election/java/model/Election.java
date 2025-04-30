package com.voteU.election.java.model;

import com.voteU.election.java.utils.xml.Transformer;
import jakarta.persistence.*;
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

@Entity
public class Election {
    @Id
    @Getter @Setter
    @Column(name = "election_id")  // Specify column name if needed
    private String id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String date;

    @OneToMany
    @MapKeyColumn(name = "party_id")  // Assuming 'Party' has an id
    @Getter @Setter
    private Map<Integer, Party> nationalParties;

    @OneToMany
    @MapKeyColumn(name = "authority_id")  // Assuming 'Authority' has an id
    @Getter @Setter
    private Map<String, Authority> authorities;

    public Election() {

    }

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.nationalParties = new HashMap<>();
        this.authorities = new HashMap<>();
    }

        @Override
        public String toString() {
            return "Name=" + this.name ;
        }


    }

