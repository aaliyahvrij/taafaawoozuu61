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
public class DutchElectionTransformer implements Transformer<ArrayList<Election>> {

    private final Map<String, Election> elections = new HashMap<>();
    private Map<Integer, Party> parties = new HashMap<Integer, Party>();

    @Override
    public Election registerElection(Map<String, String> electionData) {
        String year = electionData.get("ElectionIdentifier");

        // Haal bestaande Election op of maak een nieuwe
        Election election = elections.getOrDefault(year, new Election());

        if (election.data == null) {
            election.data = new HashMap<>();
        }
        // Voeg nieuwe data toe zonder bestaande te overschrijven
        election.data.putAll(electionData);

        elections.put(year, election);
        System.out.println("Elections currently stored: " + elections.keySet());


        return election;

    }



    @Override
    public void registerContest(Map<String, String> contestData) {
        String year = contestData.get("ElectionIdentifier"); // Haal het jaar op
        Election election = elections.get(year); // Haal de juiste Election op

        if (election != null) {
            if (election.data == null) {
                election.data = new HashMap<>();
            }


            election.data.putAll(contestData); // Voeg toe zonder te overschrijven
            //System.out.printf("Found contest information: %s\n", contestData);
        } else {
            System.out.println("Error: No election found for year " + year);
        }
    }

    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        String year = affiliationData.get("ElectionIdentifier");
        Election election = elections.get(year);

        if (election != null) {
            if (election.data == null) {
                election.data = new HashMap<>();
            }
            election.data.putAll(affiliationData); // Voeg toe zonder te overschrijven
        } else {
            System.out.println("Error: No election found for year " + year);
            return;
        }

        String affiliationId = affiliationData.get("AffiliationIdentifier");
        String affiliationName = affiliationData.get("RegisteredName");

        if (affiliationId != null && affiliationName != null) {
            try {
                int id = Integer.parseInt(affiliationId);
                parties.put(id, new Party(id, affiliationName));
                System.out.printf("Found affiliation information: %s\n", affiliationData);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No affiliation information found");
        }
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {
        String year = candidateData.get("ElectionIdentifier");
        Election election = elections.get(year);

        if (election != null) {
            if (election.data == null) {
                election.data = new HashMap<>();
            }
            election.data.putAll(candidateData); // Voeg toe zonder te overschrijven
            //System.out.printf("Found candidate information: %s\n", candidateData);
        } else {
            System.out.println("Error: No election found for year " + year);
        }


    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
        String year = votesData.get("ElectionIdentifier");
        Election election = elections.get(year);

        if (election != null) {
            if (election.data == null) {
                election.data = new HashMap<>();
            }
            election.data.putAll(votesData); // Voeg toe zonder te overschrijven
            //System.out.printf("Found votes information: %s\n", votesData);
        } else {
            System.out.println("Error: No election found for year " + year);
        }
    }

    public Election retrieve(String electionId) {
        return elections.get(electionId); // Haalt de juiste verkiezing op
    }


    @Override
    public ArrayList<Election> retrieve() {
        return new ArrayList<>(elections.values()); // Geeft ALLE verkiezingen terug
    }



    public Map<Integer, Party> getParties() {
        return parties;
    }


}
