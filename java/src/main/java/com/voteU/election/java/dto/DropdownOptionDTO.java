package com.voteU.election.java.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DropdownOptionDTO<T> {
    T id;
    String name;

    public DropdownOptionDTO(T id, String name) {
        this.id = id;
        this.name = name;
    }
}
