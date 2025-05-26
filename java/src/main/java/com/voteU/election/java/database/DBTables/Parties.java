package com.voteU.election.java.database.DBTables;

import jakarta.persistence.*;

@Entity
@Table(name = "parties")
public class Parties {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "election_id") // foreign key column in parties table
    private Elections election;

    // Constructors
    public Parties() {}


}
