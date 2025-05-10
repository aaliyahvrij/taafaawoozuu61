package com.voteU.election.java.model;

import java.util.List;

public class PollingStation {

    String id;
    String name;
    String zipCode;
    List<Party> parties;

    public PollingStation(String id, String name, String zipCode, List<Party> parties) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
        this.parties = parties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
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
