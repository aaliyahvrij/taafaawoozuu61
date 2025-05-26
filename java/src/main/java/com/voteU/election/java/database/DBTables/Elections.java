package com.voteU.election.java.database.DBTables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "elections")
public class Elections {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "votes")
    int votes;

    @Column(name = "date")
    String date;
    int partiesSize;

    public Elections(String stringId, String name, int votes, int partiesSize){
        this.id = stringId;
        this.name = name;
        this.votes = votes;
        this.partiesSize = partiesSize;
    }

    public Elections() {

    }
}
