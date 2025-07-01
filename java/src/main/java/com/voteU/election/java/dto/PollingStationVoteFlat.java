package com.voteU.election.java.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PollingStationVoteFlat {
    private String electionId;
    private String pollingStationId;
    private int partyId;
    private Integer candidateId; // nullable
    private int votes;

    public PollingStationVoteFlat(String electionId, String pollingStationId, int partyId, Integer candidateId, int votes) {
        this.electionId = electionId;
        this.pollingStationId = pollingStationId;
        this.partyId = partyId;
        this.candidateId = candidateId;
        this.votes = votes;
    }

    // Getters and setters here
}
