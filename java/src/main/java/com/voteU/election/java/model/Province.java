package com.voteU.election.java.model;

import java.util.ArrayList;
import java.util.List;

public class Province {
    int id;
    String name;
    //List<Constituency> contests;

    public Province(){}
    public Province(int id, String name) {
        this.id = id;
        this.name = name;
      //  this.contests = new ArrayList<Constituency>();
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
   // public List<Constituency> getContests() {
        //return contests;
   // }
}

