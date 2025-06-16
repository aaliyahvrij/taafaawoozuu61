package com.voteU.election.java.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvinceVotesDto {
    private int provinceId;
    private String name;
    private int votes;

    public ProvinceVotesDto(int provinceId, String name, int votes) {
        this.provinceId = provinceId;
        this.name = name;
        this.votes = votes;
    }

}
