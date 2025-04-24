package com.voteU.election.java.reader;

import com.voteU.election.java.model.*;
import com.voteU.election.java.utils.xml.DutchElectionProcessor;
import com.voteU.election.java.utils.xml.Transformer;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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
    public void registerNationalVotes(Map<String, String> votesData) {
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

    @Override
    public void registerAuthorityVotes(Map<String, String> gemeenteVoteData) {
        // System.out.println(gemeenteVoteData);

        String electionId = gemeenteVoteData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String contestIdStr = gemeenteVoteData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String authorityId = gemeenteVoteData.get(DutchElectionProcessor.AUTHORITY_IDENTIFIER);
        String partyIdStr = gemeenteVoteData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);
        String partyName = gemeenteVoteData.getOrDefault(DutchElectionProcessor.REGISTERED_NAME, "UNKNOWN");
        String authorityName = gemeenteVoteData.get(DutchElectionProcessor.AUTHORITY_NAME);
        boolean isTotalVotes = "GEMEENTE".equals(gemeenteVoteData.get("Source"));

        if (electionId == null || contestIdStr == null || authorityId == null || partyIdStr == null) {
            return;
        }

        int contestId, partyId;
        try {
            contestId = Integer.parseInt(contestIdStr);
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            return;
        }

        Election election = elections.get(electionId);
        if (election == null) {
            return;
        }

        Constituency constituency = election.getConstituencies().get(contestId);
        if (constituency == null) {
            return;
        }

        Map<String, Authority> authorityMap = constituency.getAuthorities();
        Authority authority = authorityMap.computeIfAbsent(authorityId, id -> {
            Authority a = new Authority(id);
            a.setName(authorityName);
            a.setContestId(contestId);
            return a;
        });

        Map<Integer, Party> partyMap = authority.getAuthorityParties();
        Party party = partyMap.get(partyId);

        // Handling total votes scenario
        if (isTotalVotes && party == null) {
            String votesStr = gemeenteVoteData.get(DutchElectionProcessor.VALID_VOTES);
            if (votesStr == null) {
                return;
            }
            try {
                int votes = Integer.parseInt(votesStr);
                party = new Party(partyId, partyName);
                party.setVotes(votes);
                partyMap.put(partyId, party);
            } catch (NumberFormatException ignored) {}
        }

        // Handling candidate votes
        if (gemeenteVoteData.containsKey("CandidateVotes") && party != null && isTotalVotes) {
            try {
                int candidateId = Integer.parseInt(gemeenteVoteData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER));
                int candidateVotes = Integer.parseInt(gemeenteVoteData.get("CandidateVotes"));

                if (!party.hasCandidateId(candidateId)) {
                    Candidate candidate = new Candidate();
                    candidate.setId(candidateId);
                    candidate.setVotes(candidateVotes);
                    party.addCandidate(candidate);
                }
            } catch (NumberFormatException | NullPointerException ignored) {}
        }
    }

    @Override
    public void registerConstituency(Map<String, String> constituencyData) {
        String electionId = constituencyData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String contestIdStr = constituencyData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String contestName = constituencyData.get(DutchElectionProcessor.CONTEST_NAME);
        String partyIdStr = constituencyData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);

        if (electionId == null || contestIdStr == null || partyIdStr == null || contestName == null) {
            return;
        }

        int contestId, partyId;
        try {
            contestId = Integer.parseInt(contestIdStr);
            partyId = Integer.parseInt(partyIdStr);
        } catch (NumberFormatException e) {
            return;
        }

        Election election = elections.get(electionId);
        if (election == null) {
            return;
        }

        Map<Integer, Constituency> contestMap = election.getConstituencies();
        Constituency constituency = contestMap.computeIfAbsent(contestId, id -> new Constituency(id, contestName));

        Map<Integer, Party> partyMap = constituency.getParties();
        boolean isTotalVotes = "GEMEENTE".equals(constituencyData.get("Source"));
        Party party = partyMap.get(partyId);

        // Handling total votes for the party
        if (isTotalVotes && party == null) {
            String partyVotesStr = constituencyData.get(DutchElectionProcessor.VALID_VOTES);
            if (partyVotesStr == null) {
                return;
            }
            try {
                int votes = Integer.parseInt(partyVotesStr);
                String partyName = constituencyData.getOrDefault(DutchElectionProcessor.REGISTERED_NAME, "UNKNOWN");
                party = new Party(partyId, partyName);
                party.setVotes(votes);
                partyMap.put(partyId, party);
            } catch (NumberFormatException ignore) {
                return;
            }
        }

        // Handling candidate votes if available
        if (constituencyData.containsKey("CandidateVotes") && party != null && isTotalVotes) {
            try {
                int candidateId = Integer.parseInt(constituencyData.get(DutchElectionProcessor.CANDIDATE_IDENTIFIER));
                int candidateVotes = Integer.parseInt(constituencyData.get("CandidateVotes"));

                if (!party.hasCandidateId(candidateId)) {
                    Candidate candidate = new Candidate();
                    candidate.setId(candidateId);
                    candidate.setVotes(candidateVotes);
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
        String electionId = candidateData.get(DutchElectionProcessor.ELECTION_IDENTIFIER);
        String contestIdStr = candidateData.get(DutchElectionProcessor.CONTEST_IDENTIFIER);
        String affIdStr = candidateData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER);

        if (caIdStr != null && caFirstName != null && caLastName != null &&
                electionId != null && contestIdStr != null && affIdStr != null) {

            int caId = Integer.parseInt(caIdStr);
            int affId = Integer.parseInt(affIdStr);
            int contestId = Integer.parseInt(contestIdStr);

            Election election = elections.get(electionId);
            if (election != null) {
                Map<Integer, Constituency> constituencies = election.getConstituencies();
                Constituency constituency = constituencies.get(contestId);

                if (constituency != null) {
                    // Update or insert candidate in Constituency-level Party
                    Map<Integer, Party> parties = constituency.getParties();
                    Party party = parties.get(affId);

                    if (party != null) {
                        List<Candidate> candidates = party.getCandidates();
                        Candidate existing = null;

                        for (Candidate c : candidates) {
                            if (c.getId() == caId) {
                                existing = c;
                                break;
                            }
                        }

                        if (existing != null) {
                            existing.setFirstName(caFirstName);
                            existing.setLastName(caLastName);
                        } else {
                            Candidate newCandidate = new Candidate();
                            newCandidate.setId(caId);
                            newCandidate.setFirstName(caFirstName);
                            newCandidate.setLastName(caLastName);
                            party.addCandidate(newCandidate);
                        }
                    }

                    // Update or insert candidate in each Authority-level Party
                    Map<String, Authority> authorities = constituency.getAuthorities();
                    for (Authority a : authorities.values()) {
                        Map<Integer, Party> partyMap = a.getAuthorityParties();
                        Party authorityParty = partyMap.get(affId);

                        if (authorityParty != null) {
                            List<Candidate> candidates = authorityParty.getCandidates();
                            Candidate existing = null;

                            for (Candidate c : candidates) {
                                if (c.getId() == caId) {
                                    existing = c;
                                    break;
                                }
                            }

                            if (existing != null) {
                                existing.setFirstName(caFirstName);
                                existing.setLastName(caLastName);
                            } else {
                                Candidate newCandidate = new Candidate();
                                newCandidate.setId(caId);
                                newCandidate.setFirstName(caFirstName);
                                newCandidate.setLastName(caLastName);
                                authorityParty.addCandidate(newCandidate);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void registerReportingUnit(Map<String, String> reportingUnitData) {
        System.out.println(reportingUnitData);
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
