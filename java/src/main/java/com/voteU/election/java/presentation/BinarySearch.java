package com.voteU.election.java.presentation;

import com.voteU.election.java.model.Candidate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinarySearch {

    public static void main(String[] args) {
        List<Candidate> candidates = new ArrayList<>();

        for (int i = 1; i <= 1000000; i++) {
            Candidate c = new Candidate();
            c.setId(i);
            c.setFirstName("Kandidaat" + i);
            c.setLastName("Achternaam" + i);
            candidates.add(c);
        }

        // Sorteer de lijst op ID (vereist voor binary search)
        candidates.sort(Comparator.comparingInt(Candidate::getId));

        int searchId = 500000;
        long startTime = System.currentTimeMillis();
        Candidate result = binarySearchById(candidates, searchId);
        long endTime = System.currentTimeMillis(); // Eindtijd

        System.out.println("Tijd voor Binary Search: " + (endTime - startTime) + " ms");

        if (result != null) {
            System.out.println("Gevonden: " + result.getFirstName() + " " + result.getLastName());
        } else {
            System.out.println("Kandidaat met ID " + searchId + " niet gevonden.");
        }
    }

    // Methode voor binary search op ID
    public static Candidate binarySearchById(List<Candidate> list, int targetId) {
        int low = 0; // Begin van de lijst
        int high = list.size() - 1; // Eind van de lijst

        while (low <= high) {
            int mid = (low + high) / 2; // Zoek het midden
            Candidate midCandidate = list.get(mid); // Haal kandidaat op die in het midden staat

            // Als ID gelijk is aan wat we zoeken, return kandidaat
            if (midCandidate.getId() == targetId) {
                return midCandidate;
            }
            // Als het ID kleiner is dan target, zoek in de rechterhelft
            else if (midCandidate.getId() < targetId) {
                low = mid + 1;
            }
            // Als het ID groter is dan target, zoek in de linkerhelft
            else {
                high = mid - 1;
            }
        }

        return null; // Niets gevonden
    }
}

