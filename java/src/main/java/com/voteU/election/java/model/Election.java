package com.voteU.election.java.model;

import com.voteU.election.java.utils.xml.Transformer;

import java.util.Map;

/**
 * Just a very silly election class that only demonstrates that a {@link Transformer}
 * can return an instance of a class.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class Election {
    public Map<String, String> data;
    String id;
    String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "The last information received is: %s".formatted(data);
    }
}
