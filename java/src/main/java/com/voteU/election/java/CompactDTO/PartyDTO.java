package com.voteU.election.java.CompactDTO;

import com.voteU.election.java.CompactDTO.CompactElection;
import jakarta.persistence.*;

@Entity
@Table(name = "parties")
public class PartyDTO {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "election_id") // foreign key column in parties table
    private CompactElection election;

    // Constructors
    public PartyDTO() {}


}
