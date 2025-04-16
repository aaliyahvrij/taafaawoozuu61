package com.voteU.election.java.reader;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
@Slf4j
public class DutchElectionTransformer implements Transformer<Election> {
    Map<String, Election> elections = new HashMap<>();
    Map<String, Map<Integer, Constituency>> constituencyMap = new HashMap<>();


    @Override
    public void registerElection(Map<String, String> electionData) {
        String electionId = electionData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String electionName = electionData.get(DutchElectionProcessor.ELECTION_NAME);
        String electionDate = electionData.get(DutchElectionProcessor.ELECTION_DATE);

        String partyIdStr = electionData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        String partyName = electionData.get(DutchElectionProcessor.REGISTERED_NAME);
        String votesStr = electionData.get(DutchElectionProcessor.VALID_VOTES);

        if (electionId == null || electionName == null || electionDate == null) {
            System.out.println("Incomplete election data: Missing ID, Name, or Date.");
            return;
        }

        // Get or create the Election object
        Election election = elections.get(electionId);
        if (election == null) {
            election = new Election(electionId, electionName, electionDate);
            elections.put(electionId, election);
        }

        if (partyIdStr != null && partyName != null && votesStr != null) {
            try {
                int partyId = Integer.parseInt(partyIdStr);
                int votes = Integer.parseInt(votesStr);

                // Check if the party was already added to this election
                boolean alreadyAdded = false;
                for (Party existingParty : election.getParties()) {
                    if (existingParty.getId() == partyId) {
                        alreadyAdded = true;
                        break;
                    }
                }

                if (!alreadyAdded) {
                    Party party = new Party(partyId, partyName);
                    party.setVotes(votes);
                    election.getParties().add(party);

                    //System.out.println(party.getId() + " " + party.getName() + " has " + party.getVotes() + " votes.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for party ID or votes: " + e.getMessage());
            }
        }
    }


    @Override
    public void registerContest(Map<String, String> contestData) {
    }

    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        //System.out.println(affiliationData);
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {

    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
        // System.out.println(votesData);
    }

    @Override
    public void registerConstituency(Map<String, String> constituencyData, Map<Integer, Integer> affiliationVotes, Map<Integer, Integer> candidateVotes) {


        String electionId = constituencyData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        int contestId = Integer.parseInt(constituencyData.get(DutchElectionProcessor.CONTEST_IDENTIFIER));
        String contestName = constituencyData.get(DutchElectionProcessor.CONTEST_NAME);

        constituencyMap.putIfAbsent(electionId, new HashMap<>());
        Map<Integer, Constituency> innerMap = constituencyMap.get(electionId);

        if (!innerMap.containsKey(contestId)) {
            Constituency c = new Constituency(contestId, new ArrayList<>(), new ArrayList<>(), contestName);
            innerMap.put(contestId, c);

        }

    }

    @Override
    public Election retrieve() {
        return null; // This method is not needed since we now track elections by year
    }


    public Election getElection(String year) {
        return elections.get(year);
    }


public Map<String, Map<Integer, Constituency>> getConstituencyMap() {
        return constituencyMap;
    }
}
