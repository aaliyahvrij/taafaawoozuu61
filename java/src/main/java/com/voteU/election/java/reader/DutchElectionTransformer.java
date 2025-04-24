package com.voteU.election.java.reader;

import com.voteU.election.java.model.Candidate;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.ReportingUnit;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
@Slf4j
public class DutchElectionTransformer implements Transformer<Election> {
    private Map<String, Election> elections = new HashMap<>();
    private Map<String, ReportingUnit> repUnits = new HashMap<>();

    @Override
    public void registerElection(Map<String, String> electionData) {
        String electionId = electionData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String electionName = electionData.get(DutchElectionProcessor.ELECTION_NAME);
        String electionDate = electionData.get(DutchElectionProcessor.ELECTION_DATE);

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
        // System.out.println(electionData);
    }

    @Override
    public void registerContest(Map<String, String> contestData) {
        // Handle contest data if needed
    }

    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        // Optional: Handle affiliation data if required separately
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {
        // Optional: Handle candidate data if required separately
    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
        String source = votesData.get("Source");
        boolean isTotalVotes = "TOTAL".equals(source);

        // Safely get party ID
        String partyIdStr = votesData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        if (partyIdStr == null) {
            System.err.println("❌ Missing AFFILIATION_IDENTIFIER in votesData: " + votesData);
            return;
        }

        int partyId;
        try {
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid AFFILIATION_IDENTIFIER: '" + partyIdStr + "' in " + votesData);
            return;
        }

        String partyName = votesData.get(DutchElectionProcessor.REGISTERED_NAME);
        if (partyName == null) {
            partyName = "UNKNOWN";
        }

        // Check if the party already exists
        String electionId = votesData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        Election election = elections.get(electionId);
        Map<Integer, Party> partyMap = election.getNationalParties();
        Party party = partyMap.get(partyId);

        // Register party on TOTAL only, if not already registered
        if (isTotalVotes && party == null) {
            String partyVotesStr = votesData.get(DutchElectionProcessor.VALID_VOTES);
            if (partyVotesStr == null) {
                System.err.println("❌ Missing VALID_VOTES for party " + partyName + ": " + votesData);
                return;
            }

            int partyVotes;
            try {
                partyVotes = Integer.parseInt(partyVotesStr);
            } catch (NumberFormatException e) {
                System.err.println("❌ Invalid VALID_VOTES value: '" + partyVotesStr + "' in " + votesData);
                return;
            }

            // Create and register the new party
            party = new Party(partyId, partyName);
            party.setVotes(partyVotes);
            partyMap.put(partyId, party);
            // Removed duplicate logging here to prevent repeated logs during multiple calls
        }

        // Handle candidate votes
        if (votesData.containsKey("CandidateVotes")) {
            String candidateId = votesData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER);
            String candidateVotesStr = votesData.get("CandidateVotes");

            if (candidateId == null || candidateVotesStr == null) {
                System.err.println("❌ Missing candidate data in: " + votesData);
                return;
            }

            int candidateVotes;
            try {
                candidateVotes = Integer.parseInt(candidateVotesStr);
            } catch (NumberFormatException e) {
                System.err.println("❌ Invalid CandidateVotes value: '" + candidateVotesStr + "' in " + votesData);
                return;
            }

            // Check if the candidate already exists and is added to the party
            if (isTotalVotes && party != null && !party.hasCandidateShortCode(candidateId)) {
                Candidate candidate = new Candidate();
                candidate.shortCode = candidateId;
                candidate.setVotes(candidateVotes);
                party.addCandidate(candidate);
                // Removed duplicate logging here for the candidate as well
            }
        }

        if (election != null) {
            partyMap.put(partyId, party);
        }

        // Ensure only the number of registered parties is logged, not each time for each party
        // This logging happens once at the end, after all votes are processed.
    }

    /**
     * This method is not needed since we now track elections by year.
     */
    @Override
    public Election retrieve() {
        return null; // Return null or logic to retrieve a specific election
    }

    /**
     * Get election data for a specific year.
     */
    public Election getElection(String year) {
        return elections.get(year);
    }

    public Map<String, Election> getElections() {
        return elections;
    }
}
