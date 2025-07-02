package com.voteU.election.java.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollingStationPartyVoteDTO {
    private String electionId;
    private String pollingStationId;
    private int partyId;
    private int votes;

    public PollingStationPartyVoteDTO(String electionId, String pollingStationId, int partyId, int votes) {
        this.electionId = electionId;
        this.pollingStationId = pollingStationId;
        this.partyId = partyId;
        this.votes = votes;
    }

}