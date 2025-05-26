package com.voteU.election.java.database.DBTables.authorities;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class AuthorityPartyVotesId implements Serializable {

    @Column(name = "party_id")
    private int partyId;

    @Column(name = "authority_id")
    private String authorityId;

    @Column(name = "election_id")
    private String electionId;

    public AuthorityPartyVotesId() {}

    public AuthorityPartyVotesId(int partyId, String authorityId, String electionId) {
        this.partyId = partyId;
        this.authorityId = authorityId;
        this.electionId = electionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorityPartyVotesId)) return false;
        AuthorityPartyVotesId that = (AuthorityPartyVotesId) o;
        return Objects.equals(partyId, that.partyId) &&
                Objects.equals(authorityId, that.authorityId) &&
                Objects.equals(electionId, that.electionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId, authorityId, electionId);
    }
}
