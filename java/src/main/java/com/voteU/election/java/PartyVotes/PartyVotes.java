package com.voteU.election.java.PartyVotes;

import com.voteU.election.java.CompactDTO.CompactElection;
import com.voteU.election.java.CompactDTO.PartyDTO;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class PartyVotes {

    @Id
    private Long id;

    private int votes;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private PartyDTO party;

    @ManyToOne
    @JoinColumn(name = "election_id")
    private CompactElection election;

    // getters/setters
}
