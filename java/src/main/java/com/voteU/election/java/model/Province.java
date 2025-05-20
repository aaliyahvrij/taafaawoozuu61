package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a province within an election system. A province is identified by its unique ID and name
 * and consists of a list of constituencies.
 * Each constituency within the province contains its own specific data such as parties and authorities.
 *
 * The class provides constructors for creating a province either with default values
 * or with specific ID and name. The list of constituencies is always initialized to avoid null values.
 */
@Getter
@Setter
public class Province {
    int id;
    String name;
    List<Constituency> constituencies;


    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.constituencies= new ArrayList<>();
    }
}

