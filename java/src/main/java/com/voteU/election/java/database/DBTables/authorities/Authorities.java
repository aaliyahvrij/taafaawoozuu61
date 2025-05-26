package com.voteU.election.java.database.DBTables.authorities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authorities {

    @Id
    @Column(name = "id")
    private String id;  // Assuming id is a String like '0014'

    @Column(name = "election_id")
    private String electionId;

    @Column(name = "constituency_id")
    private String constituencyId;

    @Column(name = "name")
    private String name;

    public Authorities() {
    }

    public Authorities(String id, String electionId, String constituencyId, String name) {
        this.id = id;
        this.electionId = electionId;
        this.constituencyId = constituencyId;
        this.name = name;
    }

}
