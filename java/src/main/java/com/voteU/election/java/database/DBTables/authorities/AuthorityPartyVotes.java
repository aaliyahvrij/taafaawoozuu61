    package com.voteU.election.java.database.DBTables.authorities;

    import com.voteU.election.java.database.PartyVotes;
    import jakarta.persistence.*;

    import java.util.Objects;

    @Entity
    @Table(name = "authority_party_votes")
    public class AuthorityPartyVotes  extends PartyVotes {

        @EmbeddedId
        private AuthorityPartyVotesId id;

        @MapsId("authorityId")
        @ManyToOne
        @JoinColumn(name = "authority_id", nullable = false)
        private Authorities authority;

        public AuthorityPartyVotes() {}



        // Override equals and hashCode — very important for composite keys
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AuthorityPartyVotes)) return false;
            AuthorityPartyVotes that = (AuthorityPartyVotes) o;
            return Objects.equals(party, that.party) &&
                    Objects.equals(authority, that.authority) &&
                    Objects.equals(election, that.election);
        }

        @Override
        public int hashCode() {
            return Objects.hash(party, authority, election);
        }

    }