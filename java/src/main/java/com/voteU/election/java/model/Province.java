package com.voteU.election.java.model;

import lombok.*;
import java.util.*;

@Getter
@Setter
public class Province {
    int id;
    String name;
    List<Constituency> constituencies;

    /*public Province() {
        this.constituencies = new ArrayList<>();
    }*/

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.constituencies= new ArrayList<>();
    }
}
