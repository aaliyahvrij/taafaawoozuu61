package com.voteU.election.java.model;

import com.voteU.election.java.utils.xml.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Just a very silly election class that only demonstrates that a {@link Transformer}
 * can return an instance of a class.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class Election {
    private String id;
    private String name;
    private String date;
    private List<Province> provinces;
    private List<Party> parties;
    private List<Candidate> candidates;
    private List<Constituency> constituencies;
    private List<VotingData> nationalVotingData;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.parties = new ArrayList<>();
        this.candidates = new ArrayList<>();
        this.constituencies = new ArrayList<>();
        this.nationalVotingData = new ArrayList<>();
        this.provinces = new ArrayList<>();

        Province Drenthe = new Province(1, "Drenthe");
        Province Flevoland = new Province(2, "Flevoland");
        Province Friesland = new Province(3, "Friesland");
        Province Gelderland = new Province(4, "Gelderland");
        Province Groningen = new Province(5, "Groningen");
        Province Limburg = new Province(6, "Limburg");
        Province Noord_Brabant  = new Province(7, "Noord-Brabant");
        Province Noord_Holland  = new Province(8, "Noord-Holland");
        Province Overijssel = new Province(9, "Overijssel");
        Province Utrecht = new Province(10, "Utrecht");
        Province Zeeland = new Province(11, "Zeeland");
        Province Zuid_Holland  = new Province(12, "Zuid-Holland");
        provinces.add(Drenthe);
        provinces.add(Flevoland);
        provinces.add(Friesland);
        provinces.add(Gelderland);
        provinces.add(Groningen);
        provinces.add(Limburg);
        provinces.add(Noord_Brabant);
        provinces.add(Noord_Holland);
        provinces.add(Overijssel);
        provinces.add(Utrecht);
        provinces.add(Zeeland);
        provinces.add(Zuid_Holland);
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

    public List<Province> getProvinces() {
        return provinces;
    }

    public List<Party> getParties() {
        return parties;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void addCandidate(Candidate candidate){
        candidates.add(candidate);
    }

    public List<Constituency> getConstituencies() {
        return constituencies;
    }

    public void addConstituencies(Constituency constituency) {
        constituencies.add(constituency);
    }
    public List<VotingData> getNationalVotingData() {
        return nationalVotingData;
    }

    @Override
    public String toString(){
        return "Election( id = " + id + ", name = " + name + ", date = " + date + " )";
    }

}

