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
    public void registerElection(Map<String, String> electionMap) {
        String electionId = electionMap.get(DutchElectionProcessor.ELECTION_ID);
        String electionName = electionMap.get(DutchElectionProcessor.ELECTION_NAME);
        String electionDate = electionMap.get(DutchElectionProcessor.ELECTION_DATE);
        if (electionId == null) {
            System.err.println("Missing ELECTION_ID in electionMap: " + electionMap);
            return;
        }
        if (electionName == null) {
            System.err.println("Missing ELECTION_NAME in electionMap: " + electionMap);
            return;
        }
        if (electionDate == null) {
            System.err.println("Missing ELECTION_DATE in electionMap: " + electionMap);
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
    public void registerNationalLevelTotalVotes(Map<String, String> nationMap) {
        boolean isTotalVotes = "TOTAL".equals(nationMap.get("Source"));
        String electionId = nationMap.get(DutchElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
        Map<Integer, Affiliation> affiMap = election.getAffiliations();
        Affiliation affiliation;
        // Safely get the affiliation id
        String affIdStr = nationMap.get(DutchElectionProcessor.AFFILIATION_ID);
        System.out.println("affIdStr in nationMap: " + affIdStr);
        if (affIdStr == null) {
            System.err.println("Missing AFFILIATION_ID in nationMap: " + nationMap);
            return;
        }
        int affId = 0;
        try {
            affId = Integer.parseInt(affIdStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid AFFILIATION_ID '" + affIdStr + "' in nationMap: " + nationMap);
            return;
        }
        finally {
            System.out.println(" 'affiliation'");
            affiliation = affiMap.get(affId);
        }
        // Handle affiliation-specific data
        if (!nationMap.containsKey(DutchElectionProcessor.CANDIDATE_ID)) {
            // Safely get the affiliation name
            String affiName = nationMap.get(DutchElectionProcessor.AFFILIATION_NAME);
            if (affiName == null) {
                System.out.println("AFFILIATION_NAME in nationMap: " + affiName);
                System.err.println("Missing AFFILIATION_NAME in nationMap: " + nationMap);
                return;
            }
            // Register the affiliation votes data within the TotalVotes tag if it is not already registered
            if (isTotalVotes && affiliation == null) {
                String affiVotesStr = nationMap.get(DutchElectionProcessor.VALID_VOTES);
                if (affiVotesStr == null) {
                    System.err.println("Missing VALID_VOTES for affiliation " + affiName + " in nationMap: " + nationMap);
                    return;
                }
                int affiVotes;
                try {
                    affiVotes = Integer.parseInt(affiVotesStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid VALID_VOTES value '" + affiVotesStr + "' in nationMap: " + nationMap);
                    return;
                }
                // Create and register the new affiliation
                affiliation = new Affiliation(affId, affiName, affiVotes);
                affiMap.put(affId, affiliation);
            }
        }
        // Handle candidate-specific data
        else if (nationMap.containsKey("CandiVotes")) {
            String candId = nationMap.get(DutchElectionProcessor.CANDIDATE_ID);
            String candiVotesStr = nationMap.get("CandiVotes");
            if (candId == null) {
                System.err.println("Missing CANDIDATE_ID in nationMap: " + nationMap);
                return;
            }
            if (candiVotesStr == null) {
                System.err.println("Missing CandiVotes in nationMap: " + nationMap);
                return;
            }
            int candiVotes;
            try {
                candiVotes = Integer.parseInt(candiVotesStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid CandiVotes value '" + candiVotesStr + "' in nationMap: " + nationMap);
                return;
            }
            // Check if the candidate has already been registered, and added to their respective affiliation
            if (isTotalVotes && !affiliation.hasCandiShortCode(candId)) {
                Candidate candidate = new Candidate();
                candidate.shortCode = candId;
                candidate.setVotes(candiVotes);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerConstituency(Map<String, String> constiMap) {
        // Handle constituency data if needed
    }

    @Override
    public void registerAuthority(Map<String, String> prcsAuthorityMap) {
        String electionId = prcsAuthorityMap.get(DutchElectionProcessor.ELECTION_ID);
        String constIdStr = prcsAuthorityMap.get(DutchElectionProcessor.CONTEST_ID);
        String authorityId = prcsAuthorityMap.get(DutchElectionProcessor.AUTHORITY_ID);
        String authorityName = prcsAuthorityMap.get("AuthorityName");
        String affIdStr = prcsAuthorityMap.get(DutchElectionProcessor.AFFILIATION_ID);
        String affiName = prcsAuthorityMap.get(DutchElectionProcessor.AFFILIATION_NAME);
        boolean isTotalVotes = "AUTHORITY".equals(prcsAuthorityMap.get("Source"));
        if (electionId == null) {
            System.err.println("Missing ELECTION_ID in authorityMap: " + prcsAuthorityMap);
            return;
        }
        if (constIdStr == null) {
            System.err.println("Missing CONSTITUENCY_ID in authorityMap: " + prcsAuthorityMap);
            return;
        }
        if (authorityId == null) {
            System.err.println("Missing AUTHORITY_ID in authorityMap: " + prcsAuthorityMap);
            return;
        }
        if (affIdStr == null) {
            System.err.println("Missing AFFILIATION_ID in authorityMap: " + prcsAuthorityMap);
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
        Map<Integer, Affiliation> affiMap = authority.getAffiliations();
        Affiliation affiliation = affiMap.get(affId);
        if (isTotalVotes) {
            if (affiliation == null) {
                String affiVotesStr = prcsAuthorityMap.get(DutchElectionProcessor.VALID_VOTES);
                if (affiVotesStr == null) return;
                try {
                    int affiVotes = Integer.parseInt(affiVotesStr);
                    affiliation = new Affiliation(affId, affiName, affiVotes);
                    affiMap.put(affId, affiliation);
                } catch (NumberFormatException ignored) {
                }
            }
            if (authorityMap.containsKey("CandiVotes") && affiliation != null) {
                try {
                    int candId = Integer.parseInt(prcsAuthorityMap.get(DutchElectionProcessor.CANDIDATE_ID));
                    int candiVotes = Integer.parseInt(prcsAuthorityMap.get("CandiVotes"));
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
    public void registerRepUnit(Map<String, String> prcsRepUnitMap, Map<Integer, Affiliation> prcsRepUnitMap_affiliations) {
        String electionId = prcsRepUnitMap.get(DutchElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
        Map<String, RepUnit> repUnitMap = election.getRepUnits();
        String repUnitId = prcsRepUnitMap.get(DutchElectionProcessor.REP_UNIT_ID);
        String repUnitName = prcsRepUnitMap.get("RepUnitName");
        String repUnitVotesStr = prcsRepUnitMap.get("RepUnitVotes");
        if (repUnitId == null) {
            System.err.println("Missing REP_UNIT_ID in repUnitMap: " + prcsRepUnitMap);
            return;
        }
        if (repUnitName == null) {
            System.err.println("Missing RepUnitName in repUnitMap: " + prcsRepUnitMap);
        }
        if (repUnitVotesStr == null) {
            System.err.println("Missing RepUnitVotes in repUnitMap: " + prcsRepUnitMap);
            return;
        }
        int repUnitVotes;
        try {
            repUnitVotes = Integer.parseInt(repUnitVotesStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid RepUnitVotes value '" + repUnitVotesStr + "' in repUnitMap: " + prcsRepUnitMap);
            return;
        }
        // Create and register the new reporting unit
        RepUnit repUnit = new RepUnit(repUnitId, repUnitName, prcsRepUnitMap_affiliations, repUnitVotes);
        repUnitMap.put(repUnitId, repUnit);
    }

    @Override
    public void registerCandidate(Map<String, String> candiMap) {
        String candIdStr = candiMap.get(DutchElectionProcessor.CANDIDATE_ID);
        String firstName = candiMap.get(DutchElectionProcessor.FIRST_NAME);
        String lastName = candiMap.get(DutchElectionProcessor.LAST_NAME);
        String gender = candiMap.get(DutchElectionProcessor.GENDER);
        String localityName = candiMap.get(DutchElectionProcessor.LOCALITY_NAME);
        String electionId = candiMap.get(DutchElectionProcessor.ELECTION_ID);
        String constIdStr = candiMap.get(DutchElectionProcessor.CONTEST_ID);
        String affIdStr = candiMap.get(DutchElectionProcessor.AFFILIATION_ID);
        if (candIdStr != null && lastName != null && electionId != null && constIdStr != null && affIdStr != null) {
            int candId = Integer.parseInt(candIdStr);
            int constId = Integer.parseInt(constIdStr);
            int affId = Integer.parseInt(affIdStr);
            Election election = elections.get(electionId);
            if (election != null) {
                Map<Integer, Constituency> constituencies = election.getConstituencies();
                Constituency constituency = constituencies.get(constId);
                if (constituency != null) {
                    // Update or insert a candidate in a constituency-level affiliation
                    Map<Integer, Affiliation> affiliations = constituency.getAffiliations();
                    populateCandidate(candId, firstName, lastName, gender, localityName, affId, affiliations);
                    // Update or insert a candidate in each authority-level affiliation
                    Map<String, Authority> authorities = constituency.getAuthorities();
                    for (Authority authority : authorities.values()) {
                        Map<Integer, Affiliation> affiMap = authority.getAffiliations();
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

    private void populateCandidate(int candId, String firstName, String lastName, String gender, String localityName, int affId, Map<Integer, Affiliation> affiliations) {
        Affiliation affiliation = affiliations.get(affId);
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
