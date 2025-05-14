package com.voteU.election.java.model;

import com.voteU.election.java.utils.xml.Transformer;
import lombok.*;

import java.util.*;

/**
 * Just a very silly election class that only demonstrates that a {@link Transformer}
 * can return an instance of a class.
 *
 * <b>Please do NOT include this code in your project!</b>
 */
public class Election {
    @Getter
    @Setter
    private String id;
    @Getter
    private final String name;
    private final String date;
    private final List<Province> provinces;
    @Getter
    private final Map<String, Authority> authorities;
    @Getter
    private final Map<String, RepUnit> repUnits;
    @Getter
    private final Map<Integer, Party> affiliations;

    public Election(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.provinces = new ArrayList<>();
        this.authorities = new HashMap<>();
        this.repUnits = new HashMap<>();
        this.affiliations = new HashMap<>();
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

    @Override
    public String toString() {
        return "Election {" + "\n  id: '" + this.id + '\'' + ",\n  name: '" + this.name + '\'' + ",\n  date: '" + this.date + '\'' + ",\n  amount of provinces: " + this.provinces.size() + ",\n  nationalAffiliations=" + this.affiliations + ",\n  authorities: " + this.authorities + "\n}";
    }
}
