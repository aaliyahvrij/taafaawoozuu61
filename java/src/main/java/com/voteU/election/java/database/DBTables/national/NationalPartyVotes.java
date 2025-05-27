package com.voteU.election.java.database.DBTables.national;

import com.voteU.election.java.database.PartyVotes;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "national_party_votes")
public class NationalPartyVotes  extends PartyVotes {

    @EmbeddedId
    private NationalPartyVotesId id;


    public NationalPartyVotes() {}



    // Override equals and hashCode — very important for composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NationalPartyVotes)) return false;
        NationalPartyVotes that = (NationalPartyVotes) o;
        return Objects.equals(party, that.party) &&
                Objects.equals(election, that.election);
    }

    @Override
    public int hashCode() {
        return Objects.hash(party, election);
    }

}