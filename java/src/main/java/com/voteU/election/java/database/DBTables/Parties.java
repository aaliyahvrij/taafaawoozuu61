package com.voteU.election.java.database.DBTables;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "parties")
@Getter
@Setter
public class Parties {

    @EmbeddedId
    private PartyId id;

    @Column(name = "name")
    private String name;

    @Embeddable
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class PartyId implements Serializable {
        @Column(name = "id")
        private Integer partyId;

        @Column(name = "election_id")
        private String electionId;

        public PartyId() {}

        public PartyId(Integer partyId, String electionId) {
            this.partyId = partyId;
            this.electionId = electionId;
        }
    }
}
