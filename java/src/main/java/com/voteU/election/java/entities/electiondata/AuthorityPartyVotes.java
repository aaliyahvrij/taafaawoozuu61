package com.voteU.election.java.entities.electiondata;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "authority_party_votes")
public class AuthorityPartyVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // int, auto-increment primary key

    @Column(name = "authority_id")
    private String authorityId;  // nullable int

    @Column(name = "election_id", length = 255)
    private String electionId;  // nullable varchar(255)

    @Column(name = "party_id")
    private int partyId;  // nullable int

    @Column(name = "votes")
    private int votes;  // nullable int

    public AuthorityPartyVotes() {
    }
}
