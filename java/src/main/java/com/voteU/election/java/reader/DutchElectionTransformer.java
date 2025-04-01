package com.voteU.election.java.reader;



import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.utils.xml.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A dummy {@link Transformer} that just prints the election data so you can get an understanding of what
 * information is available within each method.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class DutchElectionTransformer implements Transformer<Election> {

    private final Map<String, Election> elections = new HashMap<>();
    private Map<Integer, Party> parties = new HashMap<Integer, Party>();

    @Override
    public Election registerElection(Map<String, String> electionData) {
        String year = electionData.get("ElectionIdentifier");  // Haal het jaar op (TK2021, TK2023)

        if (year != null) {
            Election election = new Election();  // ✅ Maak een nieuw Election object
            election.data = new HashMap<>(electionData);  // ✅ Zet de data correct

            elections.put(year, election);  // ✅ Bewaar het als een Election object
        }

        System.out.printf("Found election information for %s: %s\n", year, electionData);
        return null;
    }


    @Override
    public void registerContest(Map<String, String> contestData) {
        String year = contestData.get("ElectionIdentifier"); // Haal het jaar op
        Election election = elections.get(year); // Zoek de verkiezing

        if (election != null) {
            if (election.data == null) {
                election.data = new HashMap<>();
            }
            election.data.putAll(contestData); // Voeg data toe zonder te overschrijven
            System.out.printf("Found contest information for year %s: %s\n", year, contestData);
        } else {
            System.out.println("Error: No election found for year " + year);
        }
    }

    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        String year = affiliationData.get("ElectionIdentifier"); // Haal het jaar op
        Election election = elections.get(year); // Zoek de verkiezing

        if (election != null) {
            if (election.data == null) {
                election.data = new HashMap<>();
            }
            election.data.putAll(affiliationData); // Voeg data toe zonder te overschrijven

            String affiliationId = affiliationData.get("AffiliationIdentifier");
            String affiliationName = affiliationData.get("RegisteredName");

            if (affiliationId != null && affiliationName != null) {
                try {
                    int id = Integer.parseInt(affiliationId);
                    parties.put(id, new Party(id, affiliationName));
                    System.out.printf("Found affiliation information for year %s: %s\n", year, affiliationData);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No affiliation information found for year " + year);
            }
        } else {
            System.out.println("Error: No election found for year " + year);
        }
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {
//        System.out.println("Received candidate data: " + candidateData); // Debug print
//
//        String year = candidateData.get("ElectionIdentifier");
//        if (year == null || year.isEmpty()) {
//            System.out.println("Error: No ElectionIdentifier found in candidate data.");
//            return;
//        }
//
//        // Controleer of er al een election-object bestaat
//        Election election = elections.get(year);
//        if (election == null) {
//            System.out.println("Creating new election object for year " + year);
//            election = new Election();
//            elections.put(year, election); // Zorg dat 2021 en 2023 worden opgeslagen
//        }
//
//        // Voeg kandidaat toe zonder de lijst te overschrijven
//        if (election.candidates == null) {
//            election.candidates = new ArrayList<>();
//        }
//        election.candidates.add(new HashMap<>(candidateData)); // Kopie van de data
//
//        System.out.println("Added candidate for year " + year + ": " + candidateData);
    }


    @Override
    public void registerVotes(Map<String, String> votesData) {
        String year = votesData.get("ElectionIdentifier");
        Election election = elections.get(year);

        if (election != null) {
            if (election.votes == null) {
                election.votes = new ArrayList<>();
            }
            election.votes.add(new HashMap<>(votesData)); // Voeg een kopie toe
        } else {
            System.out.println("Error: No election found for year " + year);
        }
    }

    @Override
    public Election retrieve() {
        return null;
    }


    public Map<Integer, Party> getParties() {
        return parties;
    }


}

