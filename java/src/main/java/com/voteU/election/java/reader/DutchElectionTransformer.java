package com.voteU.election.java.reader;

import com.voteU.election.java.model.*;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;
import lombok.Getter;

import java.util.*;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
@Getter
public class DutchElectionTransformer implements Transformer<Election> {
    private final Map<String, Election> elections = new HashMap<>();

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
    }

    @Override
    public void registerContest(Map<String, String> contestData) {
        // Handle contest data if needed
    }

    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        // Optional: Handle affiliation data if required separately
    }



    public void registerConstituency(Map<String, String> constituencyData, Map<Integer, Integer> affiliationVotes, Map<Integer, Map<Integer, Integer>> candidateVotes, Map<Integer, String> affiliationNames) {
        // Step 1: Extract required info
        String electionId = constituencyData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        int contestId = Integer.parseInt(constituencyData.get(DutchElectionProcessor.CONTEST_IDENTIFIER));
        String contestName = constituencyData.get(DutchElectionProcessor.CONTEST_NAME);


        // Step 2: Retrieve election
        Election election = elections.get(electionId);
        if (election == null) {
            System.err.println("[registerConstituency] ❌ No election found for ID: '" + electionId + "'. Aborting.");
            return;
        }

        // Step 3: Get or create constituency map
        Map<Integer, Constituency> constituencyMap = election.getConstituencies();
        if (constituencyMap == null) {
            System.out.println("[registerConstituency] ⚠️ Constituencies map was null. Initializing new map.");
            constituencyMap = new HashMap<>();
            election.setConstituencies(constituencyMap);
        } else {
            System.out.println("[registerConstituency] ✅ Found existing constituencies map with size: " + constituencyMap.size());
        }

        // Step 4: Get or create the constituency
        boolean isNew = !constituencyMap.containsKey(contestId);
        Constituency constituency = constituencyMap.computeIfAbsent(contestId, id -> new Constituency(id, contestName));

        // Step 5: Create new party map for this constituency
        Map<Integer, Party> parties = new HashMap<>();

        for (Map.Entry<Integer, String> entry : affiliationNames.entrySet()) {
            int partyId = entry.getKey();
            String partyName = entry.getValue();
            int totalVotes = affiliationVotes.getOrDefault(partyId, 0);

            Party party = new Party(partyId, partyName);
            party.setVotes(totalVotes);

            // Handle candidates
            Map<Integer, Integer> candidateVoteMap = candidateVotes.getOrDefault(partyId, new HashMap<>());
            List<Candidate> candidates = new ArrayList<>();

            for (Map.Entry<Integer, Integer> candEntry : candidateVoteMap.entrySet()) {
                int candidateId = candEntry.getKey();
                int votes = candEntry.getValue();

                Candidate candidate = new Candidate();
                candidate.setId(candidateId);
                candidate.setVotes(votes);
                candidate.setPartyId(partyId);

                candidates.add(candidate);
            }

            party.setCandidates(candidates);
            parties.put(partyId, party);
            // Update total authority votes
            int totalConstituencyVotes = parties.values().stream().mapToInt(Party::getVotes).sum();
            constituency.setVotes(totalConstituencyVotes);

        }

        // Step 6: Set the parties to the constituency and store it back
        constituency.setParties(parties);
        constituencyMap.put(contestId, constituency);
    }

    @Override
    public void registerNation(Map<String, String> votesData) {
        String electionId = votesData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        Election election = elections.get(electionId);
        if (election == null) {
            System.err.println("❌ No election found for ID: '" + electionId + "'");
            return;
        }

        Map<Integer, Constituency> constituencies = election.getConstituencies();
        if (constituencies == null || constituencies.isEmpty()) {
            System.err.println("❌ No constituencies found for election ID: '" + electionId + "'");
            return;
        }

        // Accumulate national party votes
        Map<Integer, Integer> accumulatedVotes = new HashMap<>();
        Map<Integer, String> partyNames = new HashMap<>();

        // Map: partyId -> Map<candidateId, accumulatedVotes>
        Map<Integer, Map<Integer, Candidate>> accumulatedCandidates = new HashMap<>();

        for (Constituency constituency : constituencies.values()) {
            Map<Integer, Party> constituencyParties = constituency.getParties();
            if (constituencyParties == null) continue;

            for (Party party : constituencyParties.values()) {
                int partyId = party.getId();
                int votes = party.getVotes();
                String name = party.getName();

                accumulatedVotes.put(partyId, accumulatedVotes.getOrDefault(partyId, 0) + votes);
                partyNames.putIfAbsent(partyId, name);

                // Accumulate candidates for this party
                List<Candidate> candidates = party.getCandidates();
                if (candidates != null) {
                    Map<Integer, Candidate> partyCandidates = accumulatedCandidates.computeIfAbsent(partyId, k -> new HashMap<>());

                    for (Candidate candidate : candidates) {
                        int candidateId = candidate.getId();
                        int candidateVotes = candidate.getVotes();

                        Candidate accumulatedCandidate = partyCandidates.get(candidateId);
                        if (accumulatedCandidate == null) {
                            // Create new candidate with zero votes first, copy id and maybe name if needed
                            accumulatedCandidate = new Candidate();
                            accumulatedCandidate.setId(candidateId);
                            accumulatedCandidate.setPartyId(partyId);
                            accumulatedCandidate.setVotes(0);
                            // Optionally set candidate name if you have it
                            partyCandidates.put(candidateId, accumulatedCandidate);
                        }

                        // Accumulate votes
                        accumulatedCandidate.setVotes(accumulatedCandidate.getVotes() + candidateVotes);
                    }
                }
            }
        }

        // Build national party map with candidates
        Map<Integer, Party> nationalParties = new HashMap<>();
        int totalVotes = 0;
        for (Map.Entry<Integer, Integer> entry : accumulatedVotes.entrySet()) {
            int partyId = entry.getKey();
            int votes = entry.getValue();
            String name = partyNames.get(partyId);

            Party party = new Party(partyId, name);
            party.setVotes(votes);

            // Attach accumulated candidates list
            Map<Integer, Candidate> candidatesMap = accumulatedCandidates.get(partyId);
            if (candidatesMap != null) {
                party.setCandidates(new ArrayList<>(candidatesMap.values()));
            } else {
                party.setCandidates(new ArrayList<>());
            }

            nationalParties.put(partyId, party);
            totalVotes += votes;
        }

        // Update election
        election.setParties(nationalParties);
        election.setVotes(totalVotes);
    }


    @Override
    public void registerAuthority(Map<String, String> authorityData) {
        String electionId = authorityData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String contestIdStr = authorityData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String authorityId = authorityData.get(DutchElectionProcessor.AUTHORITY_IDENTIFIER);
        String partyIdStr = authorityData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        String partyName = authorityData.getOrDefault(DutchElectionProcessor.REGISTERED_NAME, "UNKNOWN");
        String authorityName = authorityData.get(DutchElectionProcessor.AUTHORITY_NAME);
        boolean isTotalVotes = "GEMEENTE".equals(authorityData.get("Source"));

        // Validate basic data
        if (electionId == null || contestIdStr == null || authorityId == null || partyIdStr == null) return;

        int contestId, partyId;
        try {
            contestId = Integer.parseInt(contestIdStr);
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            return;
        }

        // Retrieve election and constituency
        Election election = elections.get(electionId);
        if (election == null) return;

        Constituency constituency = election.getConstituencies().get(contestId);
        if (constituency == null) {
            System.err.println("[registerAuthority] ❌ Constituency with ID " + contestId + " not found in election " + electionId);
            return;
        }

        // Get or create authority in constituency
        Map<String, Authority> authorityMap = constituency.getAuthorities();
        Authority authorityInMap = authorityMap.computeIfAbsent(authorityId, id -> {
            Authority authority = new Authority(id);
            authority.setName(authorityName);
            authority.setConstituencyId(contestId);
            return authority;
        });

        // Register party votes under authority
        Map<Integer, Party> partyMap = authorityInMap.getAuthorityParties();
        Party party = partyMap.get(partyId);

        if (isTotalVotes && party == null) {
            String votesStr = authorityData.get(DutchElectionProcessor.VALID_VOTES);
            if (votesStr == null) return;

            try {
                int votes = Integer.parseInt(votesStr);
                party = new Party(partyId, partyName);
                party.setVotes(votes);
                partyMap.put(partyId, party);

                // Update total authority votes
                int totalVotes = partyMap.values().stream().mapToInt(Party::getVotes).sum();
                authorityInMap.setVotes(totalVotes);

            } catch (NumberFormatException ignored) {}
        }

        // Register candidate votes (if applicable)
        if (authorityData.containsKey("CandidateVotes") && party != null && isTotalVotes) {
            try {
                int candidateId = Integer.parseInt(authorityData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER));
                int candidateVotes = Integer.parseInt(authorityData.get("CandidateVotes"));

                if (!party.hasCandidateId(candidateId)) {
                    Candidate candidate = new Candidate();
                    candidate.setId(candidateId);
                    candidate.setVotes(candidateVotes);
                    candidate.setPartyId(partyId);
                    party.addCandidate(candidate);
                }
            } catch (NumberFormatException | NullPointerException ignored) {}
        }
    }

    @Override
    public void registerPollingStation(Map<String, String> reportingUnitData) {
        String electionId = reportingUnitData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String contestIdStr = reportingUnitData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String authorityId = reportingUnitData.get(DutchElectionProcessor.AUTHORITY_IDENTIFIER);
        String pollingStationId = reportingUnitData.get(DutchElectionProcessor.REPORTING_UNIT_IDENTIFIER);
        String partyIdStr = reportingUnitData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        String partyName = reportingUnitData.getOrDefault(DutchElectionProcessor.REGISTERED_NAME, "UNKNOWN");
        String pollingStationName = reportingUnitData.get(DutchElectionProcessor.REPORTING_UNIT_NAME);
        String zipcode = reportingUnitData.get(DutchElectionProcessor.ZIPCODE);

        // Validate basic data
        if (electionId == null || contestIdStr == null || authorityId == null || pollingStationId == null || partyIdStr == null) return;

        int contestId, partyId;
        try {
            contestId = Integer.parseInt(contestIdStr);
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            return;
        }

        // Retrieve election and constituency
        Election election = elections.get(electionId);
        if (election == null) return;

        Constituency constituency = election.getConstituencies().get(contestId);
        if (constituency == null) {
            System.err.println("[registerPollingStation] ❌ Constituency with ID " + contestId + " not found in election " + electionId);
            return;
        }

        // Get the authority inside the constituency
        Map<String, Authority> authorityMap = constituency.getAuthorities();
        Authority authority = authorityMap.get(authorityId);
        if (authority == null) {
            System.err.println("[registerPollingStation] ❌ Authority with ID " + authorityId + " not found in constituency " + contestId);
            return;
        }

        // Get or create polling station inside authority
        Map<String, PollingStation> pollingStationMap = authority.getPollingStations();
        PollingStation pollingStation = pollingStationMap.computeIfAbsent(pollingStationId, id -> {
            PollingStation ps = new PollingStation(pollingStationId,pollingStationName, zipcode);
            ps.setAuthorityId(authorityId);
            return ps;
        });

        // Register party votes under polling station
        Map<Integer, Party> partyMap = pollingStation.getParties();
        Party party = partyMap.get(partyId);

        if (party == null) {
            String votesStr = reportingUnitData.get("AffiliationVotes");
            if (votesStr == null) return;

            try {
                int votes = Integer.parseInt(votesStr);
                party = new Party(partyId, partyName);
                party.setVotes(votes);
                partyMap.put(partyId, party);

                // Update total polling station votes
                int totalVotes = partyMap.values().stream().mapToInt(Party::getVotes).sum();
                pollingStation.setVotes(totalVotes);

            } catch (NumberFormatException ignored) {}
        }

        // Register candidate votes (if applicable)
        if (reportingUnitData.containsKey("CandidateVotes") && party != null) {
            try {
                int candidateId = Integer.parseInt(reportingUnitData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER));
                int candidateVotes = Integer.parseInt(reportingUnitData.get("CandidateVotes"));

                if (!party.hasCandidateId(candidateId)) {
                    Candidate candidate = new Candidate();
                    candidate.setId(candidateId);
                    candidate.setVotes(candidateVotes);
                    candidate.setPartyId(partyId);
                    party.addCandidate(candidate);
                }
            } catch (NumberFormatException | NullPointerException ignored) {}
        }
    }


    @Override
    public void registerCandidate(Map<String, String> candidateData) {
        String caIdStr = candidateData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER);
        String caFirstName = candidateData.get(DutchElectionProcessor.FIRST_NAME);
        String caLastName = candidateData.get(DutchElectionProcessor.LAST_NAME);
        String localityName = candidateData.get(DutchElectionProcessor.LOCALITY_NAME);
        String gender = candidateData.get(DutchElectionProcessor.GENDER);
        String electionId = candidateData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String contestIdStr = candidateData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String affIdStr = candidateData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);

        if (caIdStr != null && caLastName != null && electionId != null && contestIdStr != null && affIdStr != null) {

            int caId = Integer.parseInt(caIdStr);
            int affId = Integer.parseInt(affIdStr);
            int contestId = Integer.parseInt(contestIdStr);
            Election election = elections.get(electionId);

            if (election != null) {
                Map<Integer, Party> electionParties = election.getParties();
                populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, electionParties);

                Map<Integer, Constituency> constituencies = election.getConstituencies();
                Constituency constituency = constituencies.get(contestId);

                if (constituency != null) {
                    // Update or insert candidate in Constituency-level Party
                    Map<Integer, Party> parties = constituency.getParties();
                    populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, parties);

                    // Update or insert candidate in each Authority-level Party
                    Map<String, Authority> authorities = constituency.getAuthorities();
                    for (Authority authority : authorities.values()) {
                        Map<Integer, Party> partyMap = authority.getAuthorityParties();
                        populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, partyMap);

                        Map<String, PollingStation> pollingStations = authority.getPollingStations();
                        for (PollingStation pollingStation : pollingStations.values()) {
                            Map<Integer, Party> pollingStationParties = pollingStation.getParties();
                            populateCandidate(caFirstName, caLastName, localityName, gender, caId, affId, pollingStationParties);
                        }
                    }
                }
            }
        }
    }

    private void populateCandidate(String caFirstName, String caLastName, String localityName, String gender, int caId, int affId, Map<Integer, Party> parties) {
        Party party = parties.get(affId);

        if (party != null) {
            List<Candidate> candidates = party.getCandidates();
            Candidate existingCandidate = null;

            for (Candidate candidate : candidates) {
                if (candidate.getId() == caId && candidate.getPartyId() == affId) {
                    existingCandidate = candidate;
                    break;
                }
            }

            if (existingCandidate != null) {
                existingCandidate.setFirstName(caFirstName);
                existingCandidate.setLastName(caLastName);
                existingCandidate.setGender(gender);
                existingCandidate.setLocalityName(localityName);
            } else {
                Candidate newCandidate = new Candidate();
                newCandidate.setId(caId);
                newCandidate.setFirstName(caFirstName);
                newCandidate.setLastName(caLastName);
                party.addCandidate(newCandidate);
            }
        }
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
