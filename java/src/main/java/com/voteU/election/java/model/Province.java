package com.voteU.election.java.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Province {
    int id;
    String name;
    int votes;
    List<Constituency> constituencies;
    Map<Integer,Party> parties;

    public Province(){
        this.constituencies = new ArrayList<>();
    }

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
        this.votes = 0;
        this.constituencies= new ArrayList<>();
        this.parties = new HashMap<>();
    }

    public Map<String, Object> getSummary(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("votes", votes);
        return map;
    }


}
