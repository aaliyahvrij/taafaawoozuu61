package com.voteU.election.java.database.DBTables.constituencies;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "constituency_party_votes")
public class ConstituencyPartyVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // int, auto-increment primary key

    @Column(name = "constituency_id")
    private int constituencyId;  // nullable int

    @Column(name = "election_id", length = 255)
    private String electionId;  // nullable varchar(255)

    @Column(name = "party_id")
    private int partyId;  // nullable int

    @Column(name = "votes")
    private int votes;  // nullable int

    public ConstituencyPartyVotes() {
    }
}
