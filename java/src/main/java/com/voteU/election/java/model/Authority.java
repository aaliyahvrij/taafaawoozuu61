package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class Authority {
    /**
     * Represents the unique identifier for an authority.
     * This identifier is used to distinguish and reference authorities
     * within the election system.
     */
    String id;
    /**
     * Represents the name associated with the entity, such as an authority, party, polling station, or election.
     * This field is intended to provide a human-readable identifier for the object, often assisting in its differentiation
     * and identification within the system.
     */
    String name;
    /**
     * Represents the number of votes associated with a specific entity (e.g., Authority, Party, Candidate, PollingStation, or Constituency).
     * This value is used to track and manage vote counts within the election system.
     */
    int votes;
    /**
     * Represents the identifier of the election associated with the authority.
     * This variable is used to establish a relationship between the authority
     * and a specific election.
     */
    String electionId;
    /**
     * A mapping of parties associated with the authority, where the key represents the unique identifier
     * of the party and the value represents the corresponding Party object.
     *
     * This map provides a way to manage and access parties linked to a specific authority and their
     * associated data like votes, percentage, and candidates.
     */
    Map<Integer, Party> parties;
    /**
     * Represents a collection of polling stations associated with an authority.
     * Each polling station is identified by its unique ID (as a String) and mapped
     * to its corresponding {@link PollingStation} object.
     *
     * This map facilitates the management and retrieval of polling station data,
     * such as location, votes, and associated parties, within an authority.
     */
    Map<String, PollingStation> pollingStations;
    /**
     * Represents the unique identifier for a constituency.
     * This variable is used to link an {@code Authority} to its respective constituency.
     * It ensures proper mapping and association within the electoral system.
     */
    int constituencyId;

    /**
     * Constructs a new Authority instance with a provided unique identifier.
     * This constructor initializes the name as an empty string, as well as
     * empty mappings for authority parties and polling stations. Also, it
     * sets default values for constituencyId, electionId, and votes.
     *
     * @param id the unique identifier for the Authority instance
     */
    public Authority(String id) {
        this.id = id;
        this.name = "";
        this.parties = new HashMap<>();
        this.pollingStations = new HashMap<>();
        this.constituencyId = 0;
        this.electionId = "";
        this.votes = 0;
    }

    public Authority(String id, String name) {
        this.id = id;
        this.name = name;
    }



    /**
     * Returns a string representation of the Authority object. The string includes
     * the ID, name, and the string representation of its associated authority parties.
     *
     * @return A string that represents the Authority object, including its ID, name,
     *         and authority parties.
     */
    @Override
    public String toString() {
        return "id " + this.id + " name " + this.name + " authorityData " + this.parties.toString();
    }
}
