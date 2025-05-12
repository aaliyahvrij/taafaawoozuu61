package com.voteU.election.java.reader;

import com.voteU.election.java.model.*;
import com.voteU.election.java.utils.xml.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
@Getter
@Slf4j
public class DutchElectionTransformer implements Transformer<Election> {
    private Map<String, Election> elections = new HashMap<>();

    @Override
    public void registerElection(Map<String, String> electionData) {
        String electionId = electionData.get(DutchElectionProcessor.ELECTION_ID);
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
        //System.out.println(electionData);
    }

    @Override
    public void registerConstituency(Map<String, String> constiData) {
        // Handle constituency data if needed
    }

    @Override
    public void registerAffiliation(Map<String, String> affiData) {
        // Optional: Handle affiliation data if required separately
    }

    @Override
    public void registerNation(Map<String, String> votesData) {
        String source = votesData.get("Source");
        boolean isTotalVotes = "TOTAL".equals(source);

        // Safely get party ID
        String partyIdStr = votesData.get(DutchElectionProcessor.AFFILIATION_ID);
        if (partyIdStr == null) {
            System.err.println("❌ Missing AFFILIATION_ID in votesData: " + votesData);
            return;
        }
        int partyId;
        try {
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid AFFILIATION_ID: '" + partyIdStr + "' in " + votesData);
            return;
        }
        String partyName = votesData.get(DutchElectionProcessor.REGISTERED_NAME);
        if (partyName == null) {
            partyName = "UNKNOWN";
        }

        // Check if the party already exists
        String electionId = votesData.get(DutchElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
        Map<Integer, Party> partyMap = election.getParties();
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
        }

        // Handle candidate votes
        if (votesData.containsKey("CandidateVotes")) {
            String candId = votesData.get(DutchElectionProcessor.CANDIDATE_ID);
            String candiVotesStr = votesData.get("CandidateVotes");
            if (candId == null || candiVotesStr == null) {
                System.err.println("❌ Missing candidate data in: " + votesData);
                return;
            }
            int candiVotes;
            try {
                candiVotes = Integer.parseInt(candiVotesStr);
            } catch (NumberFormatException e) {
                System.err.println("❌ Invalid CandidateVotes value: '" + candiVotesStr + "' in " + votesData);
                return;
            }

            // Check if the candidate already exists and is added to the party
            if (isTotalVotes && !party.hasCandiShortCode(candId)) {
                Candidate candidate = new Candidate();
                candidate.shortCode = candId;
                candidate.setValidVotes(candiVotes);
                party.addCandidate(candidate);
            }
        }
        partyMap.put(partyId, party);
        // Ensure only the number of registered parties is logged, not each time for each party
        // This logging happens once at the end, after all votes are processed.
    }

    @Override
    public void registerAuthority(Map<String, String> authorityData) {
        String electionId = authorityData.get(DutchElectionProcessor.ELECTION_ID);
        String constIdStr = authorityData.get(DutchElectionProcessor.CONTEST_ID);
        String authorityId = authorityData.get(DutchElectionProcessor.AUTHORITY_ID);
        String partyIdStr = authorityData.get(DutchElectionProcessor.AFFILIATION_ID);
        String partyName = authorityData.getOrDefault(DutchElectionProcessor.REGISTERED_NAME, "UNKNOWN");
        String authorityName = authorityData.get(DutchElectionProcessor.AUTHORITY_NAME);
        boolean isTotalVotes = "GEMEENTE".equals(authorityData.get("Source"));
        if (electionId == null || constIdStr == null || authorityId == null || partyIdStr == null) {
            return;
        }
        int constId, partyId;
        try {
            constId = Integer.parseInt(constIdStr);
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            return;
        }
        Election election = elections.get(electionId);
        if (election == null) {
            return;
        }
        Map<String, Authority> authorityMap = election.getAuthorities();
        Authority authority = authorityMap.computeIfAbsent(authorityId, id -> {
            Authority a = new Authority(id);
            a.setName(authorityName);
            a.setConstId(constId);
            return a;
        });
        Map<Integer, Party> partyMap = authority.getAuthorityParties();
        Party party = partyMap.get(partyId);
        if (isTotalVotes && party == null) {
            String votesStr = authorityData.get(DutchElectionProcessor.VALID_VOTES);
            if (votesStr == null) return;
            try {
                int votes = Integer.parseInt(votesStr);
                party = new Party(partyId, partyName);
                party.setVotes(votes);
                partyMap.put(partyId, party);
            } catch (NumberFormatException ignored) {
            }
        }
        if (authorityData.containsKey("CandidateVotes") && party != null && isTotalVotes) {
            try {
                int candId = Integer.parseInt(authorityData.get(DutchElectionProcessor.CANDIDATE_ID));
                int candiVotes = Integer.parseInt(authorityData.get("CandidateVotes"));
                if (!party.hasCandId(candId)) {
                    Candidate candidate = new Candidate();
                    candidate.setId(candId);
                    candidate.setValidVotes(candiVotes);
                    party.addCandidate(candidate);
                }
            } catch (NumberFormatException | NullPointerException ignored) {
            }
        }
    }

    @Override
    public void registerRepUnit(Map<String, String> repUnitData) {
        String electionId = repUnitData.get(DutchElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
        Map<String, RepUnit> repUnitMap = election.getRepUnits();
        String repUnitId = repUnitData.get(DutchElectionProcessor.REP_UNIT_ID);
        RepUnit repUnit;
        String repUnitName = repUnitData.get(DutchElectionProcessor.REP_UNIT_NAME);
        String repUnitAffis = repUnitData.get("RepUnitAffis");
        String repUnitTotalVotesStr = repUnitData.get("RepUnitTotalVotes");
        if (repUnitId == null) {
            System.err.println("❌ Missing REP_UNIT_ID in repUnitData: " + repUnitData);
            return;
        }
        if (repUnitName == null) {
            repUnitName = "UNKNOWN";
        }
        if (repUnitTotalVotesStr == null) {
            System.err.println("❌ Missing RepUnitTotalVotes for " + repUnitName + ": " + repUnitData);
            return;
        }
        int repUnitTotalVotes;
        try {
            repUnitTotalVotes = Integer.parseInt(repUnitTotalVotesStr);
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid RepUnitTotalVotes value: '" + repUnitTotalVotesStr + "' in " + repUnitData);
            return;
        }

        // Create and register the new reporting unit
        repUnit = new RepUnit(repUnitId, repUnitName, repUnitAffis, repUnitTotalVotes);
        repUnitMap.put(repUnitId, repUnit);
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {

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
}
