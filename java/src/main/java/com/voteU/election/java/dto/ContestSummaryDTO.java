package com.voteU.election.java.dto;

public class ContestSummaryDTO {
    private String contestName;
    private int partyCount;

    public ContestSummaryDTO(String contestName, int partyCount) {
        this.contestName = contestName;
        this.partyCount = partyCount;
    }

    public String getContestName() {
        return contestName;
    }

    public int getPartyCount() {
        return partyCount;
    }
}
