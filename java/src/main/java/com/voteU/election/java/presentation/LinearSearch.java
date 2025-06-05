package com.voteU.election.java.presentation;

import com.voteU.election.java.model.Candidate;

import java.util.ArrayList;
import java.util.List;

public class LinearSearch {

    public static void main(String[] args) {
        // Kandidatenlijst maken
        List<Candidate> candidates = new ArrayList<>();


        for (int i = 1; i <= 1000; i++) {//
            Candidate c = new Candidate();//
            c.setId(i); //
            c.setFirstName("Kandidaat" + i); //
            c.setLastName("Achternaam" + i); //
            candidates.add(c); //
        }

        int searchId = 200; //
        long startTime = System.nanoTime(); // Start tijd
        Candidate results = linearSearchById(candidates, searchId); //
        long endTime = System.nanoTime(); // Eind tijd

        System.out.println("Tijd genomen voor Linear Search: " + (endTime - startTime) + " nanoseconds");

        if (results != null) {//
            System.out.println("Gevonden: " + results.getFirstName() + " " + results.getLastName());
        } else {
            System.out.println("Kandidaat met ID " + searchId + " niet gevonden.");
        }
    }

    // Linear search methode voor Candidate objecten
    public static Candidate linearSearchById(List<Candidate> list, int targetId) { //
        for (Candidate c : list) { //
            if (c.getId() == targetId) { //
                return c;
            }
        }
        return null;


    }
}
