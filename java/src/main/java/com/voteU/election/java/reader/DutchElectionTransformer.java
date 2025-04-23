package com.voteU.election.java.reader;

import com.voteU.election.java.model.Candidate;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
@Slf4j
public class DutchElectionTransformer implements Transformer<Election> {
    Map<String, Election> elections = new HashMap<>();
    Map<String, Map<Integer, Constituency>> constituencyMap = new HashMap<>();
    Map<Integer, Candidate> candidateMap = new HashMap<>();
    Map<String, Party> partyMap = new HashMap<>();


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

        int id = Integer.parseInt(affiliationData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER));
        String name = affiliationData.get(DutchElectionProcessor.REGISTERED_NAME);

        Party party = new Party(id, name);

        partyMap.put(String.valueOf(id), party);
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {
        int id = Integer.parseInt(candidateData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER));
        String firstName = candidateData.get(DutchElectionProcessor.FIRST_NAME);
        String lastName = candidateData.get(DutchElectionProcessor.LAST_NAME);
        String lastNamePrefix = candidateData.get(DutchElectionProcessor.LAST_NAME_PREFIX);
        int affiliationId = Integer.parseInt(candidateData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER));

        Candidate candidate = new Candidate(id, firstName, lastNamePrefix != null ? lastNamePrefix + " " + lastName : lastName);

        candidate.setPartyId(affiliationId);
        candidateMap.put(candidate.setId(id), candidate);
    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
        // System.out.println(votesData);
    }

    @Override
    public void registerConstituency(Map<String, String> constituencyData, Map<Integer, Integer> affiliationVotes, Map<Integer, Map<Integer, Integer>> candidateVotes, Map<Integer, String> affiliationNames) {

        String electionId = constituencyData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        int contestId = Integer.parseInt(constituencyData.get(DutchElectionProcessor.CONTEST_IDENTIFIER));
        String contestName = constituencyData.get(DutchElectionProcessor.CONTEST_NAME);

        constituencyData.put(DutchElectionProcessor.ELECTION_IDENTIFIER, electionId);

        constituencyMap.putIfAbsent(electionId, new HashMap<>());
        Map<Integer, Constituency> innerMap = constituencyMap.get(electionId);

        log.info("registerConstituency method called with contestId: " + contestId);
        log.info("Candidate votes map: " + candidateVotes);

        if (!innerMap.containsKey(contestId)) {
            Constituency c = new Constituency(contestId, new ArrayList<>(), new ArrayList<>(), contestName);
            innerMap.put(contestId, c);

            log.info("Entering loop for affiliationVotes: " + affiliationVotes);
            for (Map.Entry<Integer, Integer> entry : affiliationVotes.entrySet()) {
                int affiliationId = entry.getKey();
                String affiliationName = affiliationNames.getOrDefault(affiliationId, "");
                int affiliationVotesCount = entry.getValue();

                Party party = new Party(affiliationId, affiliationName);
                party.setVotes(affiliationVotesCount);
                party.setCandidates(new ArrayList<>());

                Map<Integer, Integer> candidatesForAffiliation = candidateVotes.getOrDefault(affiliationId, new HashMap<>());
                for (Map.Entry<Integer, Integer> candidateEntry : candidatesForAffiliation.entrySet()) {
                    int candidateId = candidateEntry.getKey();
                    int candidateVotesCount = candidateEntry.getValue();
                    Candidate candidateData = candidateMap.get(candidateId);

                    Candidate candidate = new Candidate(candidateId, candidateData.getFirstName(), candidateData.getLastName());
//                    candidate.setPartyId(affiliationId);
                    candidate.setPartyId(candidateData.getPartyId());
                    candidate.setVotes(candidateVotesCount);
                    party.getCandidates().add(candidate);
                }

                c.getParties().add(party);

            }


        }


    }

    public void addConstituencies(String year, List<Constituency> list) {
        Map<Integer, Constituency> map = new HashMap<>();
        for (Constituency c : list) {
            map.put(c.getId(), c);
        }
        constituencyMap.put(year, map);
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

    public Map<Integer, Candidate> getCandidateMap() {
        return candidateMap;
    }
}
