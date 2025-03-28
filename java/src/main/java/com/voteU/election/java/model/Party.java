package com.voteU.election.java.model;



public class Party {
    int id;
    String partyName;


    public Party(int id, String partyName) {
        this.id = id;
        this.partyName = partyName;;
    }

    public int getId() {
        return id;
    }
    public String getPartyName() {
        return partyName;
    }



    @Override
    public String toString() {
        return "{ID: " + id + ", PartyName: " + partyName  + "}";
    }
}
