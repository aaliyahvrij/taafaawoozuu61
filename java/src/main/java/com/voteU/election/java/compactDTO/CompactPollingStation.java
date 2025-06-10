package com.voteU.election.java.compactDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompactPollingStation {
    String id;
    String name;
    String zipCode;

    public CompactPollingStation(String id, String name, String zipCode) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
    }
}
