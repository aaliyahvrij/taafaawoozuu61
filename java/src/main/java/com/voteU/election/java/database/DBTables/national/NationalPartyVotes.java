package com.voteU.election.java.database.DBTables.national;

import com.voteU.election.java.database.DBTables.Parties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "national_party_votes")
@Getter
@Setter
public class NationalPartyVotes {

    @EmbeddedId
    private NationalPartyVotesId id;

    @Column(name = "votes")
    private int votes;

    // Map the party relationship using composite key and @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("partyId")  // This tells JPA to use partyId part of embedded id
    @JoinColumns({
            @JoinColumn(name = "party_id", referencedColumnName = "id", insertable = false , updatable = false),
            @JoinColumn(name = "election_id", referencedColumnName = "election_id", insertable = false , updatable = false),
    })
    private Parties party;

    @Embeddable
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class NationalPartyVotesId implements Serializable {

        @Column(name = "party_id")
        private Integer partyId;

        @Column(name = "election_id")
        private String electionId;

        public NationalPartyVotesId() {}

        public NationalPartyVotesId(Integer partyId, String electionId) {
            this.partyId = partyId;
            this.electionId = electionId;
        }
    }
}
