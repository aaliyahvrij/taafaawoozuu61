package com.voteU.election.java.CompactDTO;

public class CompactPollingStation {
    String id;
    String name;
    String zipCode;

    public CompactPollingStation(String id, String name, String zipCode){
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
    }
}
