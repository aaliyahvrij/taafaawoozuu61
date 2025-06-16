package com.voteU.election.java.entities.electiondata;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "provinces")
public class Provinces {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Auto-increment primary key

    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "election_id")
    private String electionId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "votes")
    private int votes;

    public Provinces() {
    }
}
