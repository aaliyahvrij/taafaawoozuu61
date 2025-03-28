package com.voteU.election.java.reader;



import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.utils.xml.Transformer;

import java.util.HashMap;
import java.util.Map;

/**
 * A dummy {@link Transformer} that just prints the election data so you can get an understanding of what
 * information is available within each method.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class DutchElectionTransformer implements Transformer<Election> {

    private Election election = new Election();
    private Map<Integer, Party> parties = new HashMap<Integer, Party>();

    @Override
    public void registerElection(Map<String, String> electionData) {
        election.data = electionData;
        System.out.printf("Found election information: %s\n", electionData);
    }

    @Override
    public void registerContest(Map<String, String> contestData) {
        election.data = contestData;
        System.out.printf("Found contest information: %s\n", contestData);
    }

    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        election.data = affiliationData;
        String affiliationId = affiliationData.get("AffiliationIdentifier");
        String affiliationName = affiliationData.get("RegisteredName");

        if(affiliationId != null && affiliationName != null) {
            try{
                int id = Integer.parseInt(affiliationId);
                parties.put(id, new Party(id, affiliationName));
                System.out.printf("Found affiliation information: %s\n", affiliationData);
            }
            catch(NumberFormatException e){
                e.printStackTrace();
            }

        }
        else{
            System.out.println("No affiliation information found");
        }
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {
        election.data = candidateData;
        System.out.printf("Found votes information: %s\n", candidateData);
            }


    @Override
    public void registerVotes(Map<String, String> votesData) {
        election.data = votesData;
        System.out.printf("Found votes information: %s\n", votesData);
    }

    @Override
    public Election retrieve() {
        return election;
    }

    public Map<Integer, Party> getParties() {
        return parties;
    }


}
