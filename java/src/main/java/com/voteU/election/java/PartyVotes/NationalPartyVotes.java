package com.voteU.election.java.PartyVotes;

import com.voteU.election.java.PartyVotes.PartyVotes;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "national_party_votes")
public class NationalPartyVotes extends PartyVotes {
    // No extra fields if national votes don't have location id
}
