package com.voteU.election.java.database.DBTables.national;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@Embeddable
public class NationalPartyVotesId implements Serializable {

    private int partyId;
    private String electionId;

    public NationalPartyVotesId() {}

    public NationalPartyVotesId(int partyId, String electionId) {
        this.partyId = partyId;
        this.electionId = electionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NationalPartyVotesId)) return false;
        NationalPartyVotesId that = (NationalPartyVotesId) o;
        return partyId == that.partyId &&
                Objects.equals(electionId, that.electionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId, electionId);
    }
}
