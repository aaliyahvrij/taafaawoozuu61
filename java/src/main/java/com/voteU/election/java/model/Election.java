package com.voteU.election.java.model;

import com.voteU.election.java.utils.xml.Transformer;
import java.util.*;

/**
 * Just a very silly election class that only demonstrates that a {@link Transformer}
 * can return an instance of a class.
 *
 * <b>Please do NOT include this code in your project!</b>
 */
public class Election {
    private String id;
    private String name;
    private String date;
    private List<Province> provinces;
    private Map<Integer, Party> nationalParties;
    private Map<String, Authority> authorities;
    private Map<Integer, RepUnit> repUnits;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.provinces = new ArrayList<>();
        this.nationalParties = new HashMap<>();
        this.authorities = new HashMap<>();

        // Initializing provinces
        Province Drenthe = new Province(1, "Drenthe");
        Province Flevoland = new Province(2, "Flevoland");
        Province Friesland = new Province(3, "Friesland");
        Province Gelderland = new Province(4, "Gelderland");
        Province Groningen = new Province(5, "Groningen");
        Province Limburg = new Province(6, "Limburg");
        Province Noord_Brabant = new Province(7, "Noord-Brabant");
        Province Noord_Holland = new Province(8, "Noord-Holland");
        Province Overijssel = new Province(9, "Overijssel");
        Province Utrecht = new Province(10, "Utrecht");
        Province Zeeland = new Province(11, "Zeeland");
        Province Zuid_Holland = new Province(12, "Zuid-Holland");

        // Adding provinces to the list
        this.provinces.add(Drenthe);
        this.provinces.add(Flevoland);
        this.provinces.add(Friesland);
        this.provinces.add(Gelderland);
        this.provinces.add(Groningen);
        this.provinces.add(Limburg);
        this.provinces.add(Noord_Brabant);
        this.provinces.add(Noord_Holland);
        this.provinces.add(Overijssel);
        this.provinces.add(Utrecht);
        this.provinces.add(Zeeland);
        this.provinces.add(Zuid_Holland);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Authority> getAuthorities() {
        return this.authorities;
    }

    public Map<Integer, Party> getNationalParties() {
        return this.nationalParties;
    }

    public Map<Integer, RepUnit> getRepUnits() { return this.repUnits; }

    @Override
    public String toString() {
        return "Election {"
                + "\n  id='" + this.id + '\''
                + ",\n  name='" + this.name + '\''
                + ",\n  date='" + this.date + '\''
                + ",\n  provinces=" + this.provinces.size()
                + ",\n  nationalParties=" + this.nationalParties
                + ",\n  authorities=" + this.authorities
                + "\n}";
    }
}
