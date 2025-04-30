package com.voteU.election.java.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Authority {
    @Id
    @Getter @Setter
    @Column(name = "authority_id")
    String id;

    @Getter @Setter
    String name;

    @OneToMany
    @MapKeyColumn(name = "party_id")  //
    @Getter @Setter
    Map<Integer, Party> authorityParties;

    @ManyToOne
    @JoinColumn(name = "election_id")  // Foreign key to Election
    @Getter @Setter
    Election election;

    public Authority(String id) {
        this.id = id;
        this.name = "";
        this.authorityParties = new HashMap<>();
    }

    public Authority() {

    }


    @Override
    public String toString() {
        return "id " + this.id + " name " + this.name + " authorityData " + this.authorityParties.toString();
    }
}
