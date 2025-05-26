package com.voteU.election.java.CompactDTO;

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
public class CompactElection {
    @Id
    String stringId;

    @Column(name = "name")
    String name;

    @Column(name = "votes")
    int votes;

    @Column(name = "date")
    String date;
    int partiesSize;

    public CompactElection(String stringId, String name, int votes, int partiesSize){
        this.stringId = stringId;
        this.name = name;
        this.votes = votes;
        this.partiesSize = partiesSize;
    }

    public CompactElection() {

    }
}
