package com.voteU.election.java.entities.electiondata;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pollingstations")
public class PollingStations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-increment primary key

    @Column(name = "pollingstation_id")
    private String pollingStationId;

    @Column(name = "authority_id")
    private String authorityId;

    @Column(name = "authority_name")
    private String authorityName;

    @Column(name = "election_id")
    private String electionId;

    @Column(name = "name")
    private String name;

    @Column(name = "votes")
    private int votes;

    @Column(name = "zipcode")
    private String zipcode;

    public PollingStations() {
    }
}
