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
    private final Map<String, Election> elections = new HashMap<>();

    @Override
    public void registerElection(Map<String, String> electionData) {
        String electionId = electionData.get(DutchElectionProcessor.ELECTION_ID);
        String electionName = electionData.get(DutchElectionProcessor.ELECTION_NAME);
        String electionDate = electionData.get(DutchElectionProcessor.ELECTION_DATE);
        if (electionId == null) {
            System.err.println("Missing ELECTION_ID in electionData: " + electionData);
            return;
        }
        if (electionName == null) {
            System.err.println("Missing ELECTION_NAME in electionData: " + electionData);
            return;
        }
        if (electionDate == null) {
            System.err.println("Missing ELECTION_DATE in electionData: " + electionData);
            return;
        }

        // Get or create an Election object
        Election election = elections.get(electionId);
        if (election == null) {
            election = new Election(electionId, electionName, electionDate);
            elections.put(electionId, election);
        }
    }

    @Override
    public void registerNation(Map<String, String> nationData) {
        boolean isTotalVotes = "TOTAL".equals(nationData.get("Source"));

        // Safely get the affiliation id
        String affIdStr = nationData.get(DutchElectionProcessor.AFFILIATION_ID);
        if (affIdStr == null) {
            System.err.println("Missing AFFILIATION_ID in nationData: " + nationData);
            return;
        }
        int affId;
        try {
            affId = Integer.parseInt(affIdStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid AFFILIATION_ID '" + affIdStr + "' in nationData: " + nationData);
            return;
        }

        // Safely get the affiliation name
        String affiName = nationData.get(DutchElectionProcessor.REGISTERED_NAME);
        if (affiName == null) {
            System.err.println("Missing AFFILIATION_NAME in nationData: " + nationData);
            return;
        }

        // Check if the affiliation already exists
        String electionId = nationData.get(DutchElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
        Map<Integer, Party> affiMap = election.getAffiliations();
        Party affiliation = affiMap.get(affId);

        // Register the affiliation votes data within the TotalVotes tag if it is not already registered
        if (isTotalVotes && affiliation == null) {
            String affiVotesStr = nationData.get(DutchElectionProcessor.VALID_VOTES);
            if (affiVotesStr == null) {
                System.err.println("Missing VALID_VOTES for affiliation " + affiName + " in nationData: " + nationData);
                return;
            }
            int affiVotes;
            try {
                affiVotes = Integer.parseInt(affiVotesStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid VALID_VOTES value '" + affiVotesStr + "' in nationData: " + nationData);
                return;
            }

            // Create and register the new affiliation
            affiliation = new Party(affId, affiName, affiVotes);
            affiMap.put(affId, affiliation);
        }

        // Handle candidate data
        if (nationData.containsKey("CandiVotes")) {
            String candId = nationData.get(DutchElectionProcessor.CANDIDATE_ID);
            String candiVotesStr = nationData.get("CandiVotes");
            if (candId == null) {
                System.err.println("Missing CANDIDATE_ID in nationData: " + nationData);
                return;
            }
            if (candiVotesStr == null) {
                System.err.println("Missing CandiVotes in nationData: " + nationData);
                return;
            }
            int candiVotes;
            try {
                candiVotes = Integer.parseInt(candiVotesStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid CandiVotes value '" + candiVotesStr + "' in nationData: " + nationData);
                return;
            }

            // Check if the candidate already exists and is added to the affiliation
            if (isTotalVotes && !affiliation.hasCandiShortCode(candId)) {
                Candidate candidate = new Candidate();
                candidate.shortCode = candId;
                candidate.setVotes(candiVotes);
                affiliation.addCandidate(candidate);
            }
        }
        affiMap.put(affId, affiliation);
        // Ensure only the number of registered affiliations is logged, not each time for each affiliation
        // This logging happens once at the end, after all votes are processed.
    }

    @Override
    public void registerConstituency(Map<String, String> constiData) {
        // Handle constituency data if needed
    }

    @Override
    public void registerAuthority(Map<String, String> authorityData) {
        String electionId = authorityData.get(DutchElectionProcessor.ELECTION_ID);
        String constIdStr = authorityData.get(DutchElectionProcessor.CONTEST_ID);
        String authorityId = authorityData.get(DutchElectionProcessor.AUTHORITY_ID);
        String authorityName = authorityData.get("AuthorityName");
        String affIdStr = authorityData.get(DutchElectionProcessor.AFFILIATION_ID);
        String affiName = authorityData.get(DutchElectionProcessor.REGISTERED_NAME);
        boolean isTotalVotes = "AUTHORITY".equals(authorityData.get("Source"));
        if (electionId == null) {
            System.err.println("Missing ELECTION_ID in authorityData: " + authorityData);
            return;
        }
        if (constIdStr == null) {
            System.err.println("Missing CONTEST_ID in authorityData: " + authorityData);
            return;
        }
        if (authorityId == null) {
            System.err.println("Missing AUTHORITY_ID in authorityData: " + authorityData);
            return;
        }
        if (affIdStr == null) {
            System.err.println("Missing AFFILIATION_ID in authorityData: " + authorityData);
            return;
        }
        int constId, affId;
        try {
            constId = Integer.parseInt(constIdStr);
            affId = Integer.parseInt(affIdStr);
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
        Map<Integer, Party> affiMap = authority.getAffiliations();
        Party affiliation = affiMap.get(affId);
        if (isTotalVotes) {
            if (affiliation == null) {
                String affiVotesStr = authorityData.get(DutchElectionProcessor.VALID_VOTES);
                if (affiVotesStr == null) return;
                try {
                    int affiVotes = Integer.parseInt(affiVotesStr);
                    affiliation = new Party(affId, affiName, affiVotes);
                    affiMap.put(affId, affiliation);
                } catch (NumberFormatException ignored) {
                }
            }
            if (authorityData.containsKey("CandiVotes") && affiliation != null) {
                try {
                    int candId = Integer.parseInt(authorityData.get(DutchElectionProcessor.CANDIDATE_ID));
                    int candiVotes = Integer.parseInt(authorityData.get("CandiVotes"));
                    if (!affiliation.hasCandId(candId)) {
                        Candidate candidate = new Candidate();
                        candidate.setId(candId);
                        candidate.setVotes(candiVotes);
                        affiliation.addCandidate(candidate);
                    }
                } catch (NumberFormatException | NullPointerException ignored) {
                }
            }
        }
    }

    @Override
    public void registerRepUnit(Map<String, String> repUnitData, Map<Integer, Party> repUnitData_affiliations) {
        String electionId = repUnitData.get(DutchElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
        Map<String, RepUnit> repUnitMap = election.getRepUnits();
        String repUnitId = repUnitData.get(DutchElectionProcessor.REP_UNIT_ID);
        String repUnitName = repUnitData.get("RepUnitName");
        String repUnitVotesStr = repUnitData.get("RepUnitVotes");
        if (repUnitId == null) {
            System.err.println("Missing REP_UNIT_ID in repUnitData: " + repUnitData);
            return;
        }
        if (repUnitName == null) {
            System.err.println("Missing RepUnitName in repUnitData: " + repUnitData);
        }
        if (repUnitVotesStr == null) {
            System.err.println("Missing RepUnitVotes in repUnitData: " + repUnitData);
            return;
        }
        int repUnitVotes;
        try {
            repUnitVotes = Integer.parseInt(repUnitVotesStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid RepUnitVotes value '" + repUnitVotesStr + "' in repUnitData: " + repUnitData);
            return;
        }

        // Create and register the new reporting unit
        RepUnit repUnit = new RepUnit(repUnitId, repUnitName, repUnitData_affiliations, repUnitVotes);
        repUnitMap.put(repUnitId, repUnit);
    }

    @Override
    public void registerCandidate(Map<String, String> candiData) {
        String candIdStr = candiData.get(DutchElectionProcessor.CANDIDATE_ID);
        String firstName = candiData.get(DutchElectionProcessor.FIRST_NAME);
        String lastName = candiData.get(DutchElectionProcessor.LAST_NAME);
        String gender = candiData.get(DutchElectionProcessor.GENDER);
        String localityName = candiData.get(DutchElectionProcessor.LOCALITY_NAME);
        String electionId = candiData.get(DutchElectionProcessor.ELECTION_ID);
        String constIdStr = candiData.get(DutchElectionProcessor.CONTEST_ID);
        String affIdStr = candiData.get(DutchElectionProcessor.AFFILIATION_ID);

        if (candIdStr != null && lastName != null && electionId != null && constIdStr != null && affIdStr != null) {
            int candId = Integer.parseInt(candIdStr);
            int constId = Integer.parseInt(constIdStr);
            int affId = Integer.parseInt(affIdStr);
            Election election = elections.get(electionId);
            if (election != null) {
                Map<Integer, Constituency> constituencies = election.getConstituencies();
                Constituency constituency = constituencies.get(constId);
                if (constituency != null) {
                    // Update or insert a candidate in Constituency-level Affiliation
                    Map<Integer, Party> affiliations = constituency.getAffiliations();
                    populateCandidate(candId, firstName, lastName, gender, localityName, affId, affiliations);

                    // Update or insert a candidate in each Authority-level Affiliation
                    Map<String, Authority> authorities = constituency.getAuthorities();
                    for (Authority authority : authorities.values()) {
                        Map<Integer, Party> affiMap = authority.getAffiliations();
                        populateCandidate(candId, firstName, lastName, gender, localityName, affId, affiMap);
                        Map<String, RepUnit> repUnits = authority.getRepUnits();
                        for (RepUnit repUnit : repUnits.values()) {
                            populateCandidate(candId, firstName, lastName, gender, localityName, affId, repUnit.getAffiliations());
                        }
                    }
                }
            }
        }
    }

    private void populateCandidate(int candId, String firstName, String lastName, String gender, String localityName, int affId, Map<Integer, Party> affiliations) {
        Party affiliation = affiliations.get(affId);
        if (affiliation != null) {
            List<Candidate> candidates = affiliation.getCandidates();
            Candidate existingCandidate = null;
            for (Candidate candidate : candidates) {
                if (candidate.getId() == candId && candidate.getAffId() == affId) {
                    existingCandidate = candidate;
                    break;
                }
            }
            if (existingCandidate != null) {
                existingCandidate.setFirstName(firstName);
                existingCandidate.setLastName(lastName);
                existingCandidate.setGender(gender);
                existingCandidate.setLocalityName(localityName);
            } else {
                Candidate newCandidate = new Candidate();
                newCandidate.setId(candId);
                newCandidate.setFirstName(firstName);
                newCandidate.setLastName(lastName);
                newCandidate.setGender(gender);
                newCandidate.setLocalityName(localityName);
                newCandidate.setAffId(affId); // This may be missing in your original, depending on Candidate class
                affiliation.addCandidate(newCandidate);
            }
        }
    }

    /**
     * Get election data for a specific year.
     */
    public Election getElection(String year) {
        return elections.get(year);
    }
}
