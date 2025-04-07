package com.voteU.election.java.reader;

import com.voteU.election.java.model.Candidate;
import com.voteU.election.java.model.Contest;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
public class DutchElectionTransformer implements Transformer<Election> {
    private Map<Integer, Candidate> candidates = new HashMap<>();
    private Map<String, Election> electionsByYear = new HashMap<>();
    private Map<String, Map<Integer, Contest>> contestsByYear = new HashMap<>(); // <YEAR, <CONTESTID, CONTEST>>
    private Map<String, Map<Integer, Party>> partiesByYear = new HashMap<>(); // <YEAR, <PARTYID, PARTY>>


    @Override
    public void registerElection(Map<String, String> electionData) {
        String year = electionData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        if (year == null) {
            System.out.println("No election year found!");
            return;
        }

        Election election = new Election();
        election.data = new HashMap<>(electionData);
        election.setId(year);
        election.setName(electionData.get(DutchElectionProcessor.ELECTION_NAME));

        electionsByYear.put(year, election);

        // Initialize year-specific maps if not already present
        contestsByYear.putIfAbsent(year, new HashMap<>());
        partiesByYear.putIfAbsent(year, new HashMap<>());

        //System.out.printf("Registered election: %s\n", electionData);
    }

    @Override
    public void registerContest(Map<String, String> contestData) {
        String year = contestData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        if (year == null) {
            System.err.println("Missing ElectionIdentifier");
            return;
        }

        String id = contestData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String name = contestData.get(DutchElectionProcessor.CONTEST_NAME);
        if (id != null && name != null) {
            int intId = Integer.parseInt(id);
            Contest contest = new Contest(intId, name);

            // Store the contest in the correct year's contest map
            contestsByYear.get(year).put(intId, contest);
        }

        //System.out.printf("Registered contest: %s\n", contestData);
    }

    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        String year = affiliationData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        if (year == null) {
            System.err.println("Missing ElectionIdentifier");
            return;
        }

        String id = affiliationData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        String name = affiliationData.get(DutchElectionProcessor.REGISTERED_NAME);
        String contestId = affiliationData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        if (id != null && name != null && contestId != null) {
            int intId = Integer.parseInt(id);
            int contestIntId = Integer.parseInt(contestId);

            Party party = new Party(intId, name);
            partiesByYear.get(year).put(intId, party);

            // Retrieve the contest and add the party
            Contest contest = contestsByYear.get(year).get(contestIntId);
            if (contest != null) {
                contest.getParties().add(party);
            } else {
                System.err.println("Contest not found for ID: " + contestIntId + " in year " + year);
            }
        }

        // System.out.printf("Registered party: %s\n", affiliationData);
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {
        String year = candidateData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        if (year == null) {
            System.err.println("Missing ElectionIdentifier");
            return;
        }

        String id = candidateData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER);
        String affiliationId = candidateData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        String firstName = candidateData.get(DutchElectionProcessor.FIRST_NAME);
        String lastName = candidateData.get(DutchElectionProcessor.LAST_NAME);

        if (id != null && affiliationId != null && lastName != null) {
            int intId = Integer.parseInt(id);
            int intAffiliationId = Integer.parseInt(affiliationId);
            firstName = (firstName != null) ? firstName : "";
            Candidate candidate = new Candidate(intId, firstName, lastName);
            candidates.put(intId, candidate);

            Party party = partiesByYear.get(year).get(intAffiliationId);
            if (party != null) {
                party.getCandidates().add(candidate);
            } else {
                System.err.println("Party not found for ID: " + intAffiliationId + " in year " + year);
            }
        }

        // System.out.printf("Registered candidate: %s\n", candidateData);
    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
        //System.out.printf("Registered votes: %s\n", votesData);
    }

    @Override
    public Election retrieve() {
        return null; // This method is not needed since we now track elections by year
    }

    public Map<Integer, Contest> getContests(String year) {
        return contestsByYear.getOrDefault(year, new HashMap<>());
    }

    public Election getElection(String year) {
        return electionsByYear.get(year);
    }


}
