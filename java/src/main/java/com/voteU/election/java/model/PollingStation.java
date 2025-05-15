package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class PollingStation {

    private String id;
    private String name;
    private String zipCode;
    private Map<Integer, Party> parties;
    private String authorityId;

    public PollingStation(String id, String name, String zipCode) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "PollingStation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", parties=" + parties +
                '}';
    }
}
