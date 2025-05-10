package com.voteU.election.java.reader;

import com.voteU.election.java.model.*;
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
    Map<String, Map<String, PollingStation>> pollingStationMap = new HashMap<>();
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

        Party party = partyMap.get(String.valueOf(affiliationId));
        if (party != null) {
            if (party.getCandidates() == null) {
                party.setCandidates(new ArrayList<>());
            }
            party.getCandidates().add(candidate);
        }
    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
        // System.out.println(votesData);
    }

    /**
     * Registers a constituency and associates its affiliations and candidates with vote counts.
     *
     * <p>This method initializes a {@link Constituency} for a given election and contest if it does not already exist.
     * It then populates the constituency's list of parties and their candidates, including their vote counts.</p>
     *
     * @param constituencyData   Map containing data about the constituency (e.g., election ID, contest ID, name).
     * @param affiliationVotes   A map of affiliation IDs to the number of votes they received.
     * @param candidateVotes     A nested map: affiliation ID → (candidate ID → vote count).
     * @param affiliationNames   A map of affiliation IDs to their registered names.
     */
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
            Constituency c = new Constituency(contestId, new ArrayList<>(), contestName);
            innerMap.put(contestId, c);

            log.info("Entering loop for affiliationVotes: " + affiliationVotes);
            connectPartyWithCandidate(affiliationVotes, candidateVotes, affiliationNames, c.getParties());

        }
    }

    /**
     * Registers a polling station and associates its affiliations and candidates with vote counts.
     *
     * <p>This method initializes a {@link PollingStation} for a given election and station ID if it does not already exist.
     * It then populates the polling station’s list of parties and their candidates, including their vote counts.</p>
     *
     * @param reportingUnitData  Map containing data about the polling station (e.g., election ID, polling ID, name, ZIP code).
     * @param affiliationVotes   A map of affiliation IDs to the number of votes they received.
     * @param candidateVotes     A nested map: affiliation ID → (candidate ID → vote count).
     * @param affiliationNames   A map of affiliation IDs to their registered names.
     */
    @Override
    public void registerPollingStation(Map<String, String> reportingUnitData, Map<Integer, Integer> affiliationVotes, Map<Integer, Map<Integer, Integer>> candidateVotes, Map<Integer, String> affiliationNames) {

        String electionId = reportingUnitData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String pollingId = reportingUnitData.get(DutchElectionProcessor.REPORTING_UNIT_IDENTIFIER);
        String pollingName = reportingUnitData.get(DutchElectionProcessor.REPORTING_UNIT_NAME);
        String zipCode = reportingUnitData.get(DutchElectionProcessor.ZIPCODE);

        reportingUnitData.put(DutchElectionProcessor.ELECTION_IDENTIFIER, electionId);

        pollingStationMap.putIfAbsent(electionId, new HashMap<>());
        Map<String, PollingStation> pollingStationWithYear = pollingStationMap.get(electionId);

        if (!pollingStationWithYear.containsKey(pollingId)) {
            PollingStation p = new PollingStation(pollingId, pollingName, zipCode, new ArrayList<>());
            pollingStationWithYear.put(pollingId, p);

            log.info("Entering loop for affiliationVotes in gemeente: " + affiliationVotes);
            connectPartyWithCandidate(affiliationVotes, candidateVotes, affiliationNames, p.getParties());

        }
    }

    /**
     * Links affiliations with their corresponding candidates and vote counts, and adds them to the given list of parties.
     *
     * <p>This method creates a new {@link Party} instance for each affiliation, populates it with candidates from the
     * registered party map, and adds it to the provided list. Each candidate includes their personal info and vote count.</p>
     *
     * @param affiliationVotes   A map of affiliation IDs to their total vote counts.
     * @param candidateVotes     A nested map: affiliation ID → (candidate ID → vote count).
     * @param affiliationNames   A map of affiliation IDs to their registered names.
     * @param parties            A list to which newly constructed Party instances will be added.
     */
    private void connectPartyWithCandidate(Map<Integer, Integer> affiliationVotes, Map<Integer, Map<Integer, Integer>> candidateVotes, Map<Integer, String> affiliationNames, List<Party> parties) {
        for (Map.Entry<Integer, Integer> entry : affiliationVotes.entrySet()) {
            int affiliationId = entry.getKey();
            String affiliationName = affiliationNames.getOrDefault(affiliationId, "");
            int affiliationVotesCount = entry.getValue();

            Party party = new Party(affiliationId, affiliationName);
            party.setVotes(affiliationVotesCount);
            party.setCandidates(new ArrayList<>());

            Party registeredParty = partyMap.get(String.valueOf(affiliationId));
            if (registeredParty != null) {
                List<Candidate> originalCandidates = registeredParty.getCandidates();
                Map<Integer, Integer> candidatesForAffiliation = candidateVotes.getOrDefault(affiliationId, new HashMap<>());

                for (Candidate original : originalCandidates) {
                    int votes = candidatesForAffiliation.getOrDefault(original.getId(), 0);
                    Candidate candidate = new Candidate(original.getId(), original.getFirstName(), original.getLastName());
                    candidate.setPartyId(affiliationId);
                    candidate.setVotes(votes);
                    party.getCandidates().add(candidate);
                }
            }

            parties.add(party);

        }
    }

    public void addPollingStations(String year, List<PollingStation> list) {
        Map<String, PollingStation> map = new HashMap<>();
        for (PollingStation p : list) {
            map.put(p.getId(), p);
        }
        pollingStationMap.put(year, map);
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
