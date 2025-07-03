package com.voteU.election.java.entities.electiondata;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pollingstation_party_votes")
public class PollingStationPartyVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "election_id")
    private String electionId;

    @Column(name = "pollingstation_id")
    private String pollingStationId;

    @Column(name = "party_id")
    private int partyId;

    @Column(name = "votes")
    private int votes;

    public PollingStationPartyVotes() {
    }
}
