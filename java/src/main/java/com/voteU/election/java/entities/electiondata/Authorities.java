package com.voteU.election.java.entities.electiondata;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authorities{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-increment primary key

    @Column(name = "authority_id")
    private String authorityId;

    @Column(name = "constituency_id")
    private int constituencyId;

    @Column(name = "election_id")
    private String electionId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "votes")
    private int votes;

    public Authorities() {
    }
}
