package com.voteU.election.java.model;

import java.util.Map;

/**
 * Just a very silly election class that only demonstrates that a {@link nl.hva.ict.se.sm3.utils.xml.Transformer}
 * can return an instance of a class.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class Election {
    public Map<String, String> data;

    @Override
    public String toString() {
        return "The last information received is: %s".formatted(data);
    }
}
