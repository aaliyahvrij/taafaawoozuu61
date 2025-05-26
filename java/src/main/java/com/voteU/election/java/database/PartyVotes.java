package com.voteU.election.java.database;

import com.voteU.election.java.database.DBTables.Elections;
import com.voteU.election.java.database.DBTables.Parties;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class PartyVotes {

    @ManyToOne
    @JoinColumn(name = "party_id")
    @MapsId("partyId")
    protected Parties party;

    @ManyToOne
    @JoinColumn(name = "election_id")
    @MapsId("electionId")
    protected Elections election;

    protected int votes;

}
