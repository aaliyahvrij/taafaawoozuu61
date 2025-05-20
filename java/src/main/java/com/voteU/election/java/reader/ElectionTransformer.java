package com.voteU.election.java.reader;

import com.voteU.election.java.model.*;
import com.voteU.election.java.utils.xml.*;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
@Getter
@Slf4j
public class ElectionTransformer implements Transformer<Election> {
    private final Map<String, Election> elections = new HashMap<>();
    Map<String, Affiliation> affiMap = new HashMap<>();
    private static final Map<Integer, Integer> DISTRICT_TO_PROVINCE_ID = Map.ofEntries(Map.entry(3, 1),  // Drenthe
            Map.entry(5, 2),    // Flevoland
            Map.entry(2, 3),    // Friesland
            Map.entry(7, 4),    // Gelderland
            Map.entry(6, 4), Map.entry(1, 5),                             // Groningen
            Map.entry(19, 6),   // Limburg
            Map.entry(18, 7),   // Noord-Brabant
            Map.entry(17, 7), Map.entry(10, 8),                           // Noord-Holland
            Map.entry(9, 8), Map.entry(11, 8), Map.entry(4, 9),     // Overijssel
            Map.entry(8, 10),   // Utrecht
            Map.entry(16, 11),  // Zeeland
            Map.entry(14, 12),  // Zuid-Holland
            Map.entry(15, 12), Map.entry(13, 12), Map.entry(12, 12));

    @Override
    public void registerElection(Map<String, String> electionMap) {
        String electionId = electionMap.get(ElectionProcessor.ELECTION_ID);
        String electionName = electionMap.get(ElectionProcessor.ELECTION_NAME);
        String electionDate = electionMap.get(ElectionProcessor.ELECTION_DATE);
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
    public void registerNationalLevel_TotalVotes(Map<String, String> nationMap) {
        String electionId = nationMap.get(ElectionProcessor.ELECTION_ID);
        System.out.println("electionId in nationMap: " + electionId);
        Election election = elections.get(electionId);
        Map<Integer, Affiliation> affiMap = election.getAffiliations();
        System.out.println("Amount of affiliations in this election: " + affiMap.size());
        Affiliation affiliation;
        // Safely get the affiliation id
        String affIdStr = nationMap.get(ElectionProcessor.AFFILIATION_ID);
        System.out.println("affIdStr in nationMap: " + affIdStr);
        if (affIdStr == null) {
            System.err.println("Missing AFFILIATION_ID in nationMap: " + nationMap);
            return;
        }
        int affId = 0;
        try {
            affId = Integer.parseInt(affIdStr);
            System.out.println("affId in nationMap: " + affId);
        } catch (NumberFormatException e) {
            System.err.println("Invalid AFFILIATION_ID '" + affIdStr + "' in nationMap: " + nationMap);
            return;
        } finally {
            affiliation = affiMap.get(affId);
            System.out.println("affiliation object: " + affiliation);
        }
        // Handle affiliation-specific data
        if (!nationMap.containsKey(ElectionProcessor.CANDIDATE_ID)) {
            // Safely get the affiliation name
            String affiName = nationMap.get(ElectionProcessor.AFFILIATION_NAME);
            if (affiName == null) {
                System.out.println("AFFILIATION_NAME in nationMap: " + affiName);
                System.err.println("Missing AFFILIATION_NAME in nationMap: " + nationMap);
                return;
            }
            // Register the affiliation votes data within the TotalVotes tag if it is not already registered
            if (affiliation == null) {
                String affiVotesStr = nationMap.get(ElectionProcessor.VALID_VOTES);
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
        if (nationMap.containsKey("CandiVotes")) {
            String candId = nationMap.get(ElectionProcessor.CANDIDATE_ID);
            String shortCode = nationMap.get(ElectionProcessor.SHORT_CODE);
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
            if (!affiliation.hasCandiShortCode(candId)) {
                Candidate candidate = new Candidate(Integer.parseInt(candId), candiVotes);
                candidate.setShortCode(shortCode);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerConstiOrAuthorityLevel_ConstiData(Map<String, String> prcsConstiMap, Map<Integer, String> affiNames, Map<Integer, Integer> affiVotes, Map<Integer, Map<Integer, Integer>> candiVotes) {
        String electionId = prcsConstiMap.get(ElectionProcessor.ELECTION_ID);
        int constId = Integer.parseInt(prcsConstiMap.get(ElectionProcessor.CONTEST_ID));
        String constiName = prcsConstiMap.get(ElectionProcessor.CONTEST_NAME);
        Election election = elections.get(electionId);
        if (election == null) return;
        Map<Integer, Constituency> constiMap = election.getConstituencies();
        if (constiMap == null) {
            constiMap = new HashMap<>();
            election.setConstituencies(constiMap);
        }
        Constituency constituency = constiMap.get(constId);
        if (constituency == null) {
            constituency = new Constituency(constId, constiName);
            constiMap.put(constId, constituency);
        }
        // Add affiliations to the constituency
        for (Map.Entry<Integer, String> entry : affiNames.entrySet()) {
            int affId = entry.getKey();
            String affiName = entry.getValue();
            int totalVotes = affiVotes.getOrDefault(affId, 0);
            Affiliation affiliation = new Affiliation(affId, affiName, totalVotes);
            Map<Integer, Integer> votesForCandidates = candiVotes.getOrDefault(affId, new HashMap<>());
            Affiliation globalAffiliation = affiMap.get(String.valueOf(affId));
            if (globalAffiliation != null && globalAffiliation.getCandidates() != null) {
                for (Candidate original : globalAffiliation.getCandidates()) {
                    Candidate clone = new Candidate(original.getId(), original.getFirstName(), original.getLastName());
                    clone.setAffId(affId);
                    clone.setVotes(votesForCandidates.getOrDefault(clone.getId(), 0));
                    affiliation.getCandidates().add(clone);
                }
            }
            Integer provinceId = DISTRICT_TO_PROVINCE_ID.get(constId);
            if (provinceId != null) {
                for (Province province : election.getProvinces().values()) {
                    if (province.getId() == provinceId) {
                        province.getConstituencies().add(constituency);
                        break;
                    }
                }
            }
            constituency.getAffiliations().put(affId, affiliation);
        }
    }

    @Override
    public void registerAuthority(Map<String, String> prcsAuthorityMap) {
        String electionId = prcsAuthorityMap.get(ElectionProcessor.ELECTION_ID);
        String constIdStr = prcsAuthorityMap.get(ElectionProcessor.CONTEST_ID);
        String authorityId = prcsAuthorityMap.get(ElectionProcessor.AUTHORITY_ID);
        String authorityName = prcsAuthorityMap.get("AuthorityName");
        String affIdStr = prcsAuthorityMap.get(ElectionProcessor.AFFILIATION_ID);
        String affiName = prcsAuthorityMap.get(ElectionProcessor.AFFILIATION_NAME);
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
        if (affiliation == null) {
            String affiVotesStr = prcsAuthorityMap.get(ElectionProcessor.VALID_VOTES);
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
                int candId = Integer.parseInt(prcsAuthorityMap.get(ElectionProcessor.CANDIDATE_ID));
                int candiVotes = Integer.parseInt(prcsAuthorityMap.get("CandiVotes"));
                if (!affiliation.hasCandId(candId)) {
                    Candidate candidate = new Candidate(candId, candiVotes);
                    affiliation.addCandidate(candidate);
                }
            } catch (NumberFormatException | NullPointerException ignored) {
            }
        }
    }

    @Override
    public void registerRepUnit(Map<String, String> prcsRepUnitMap, Map<Integer, Affiliation> prcsRepUnitMap_affiliations) {
        String electionId = prcsRepUnitMap.get(ElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
        Map<String, RepUnit> repUnitMap = election.getRepUnits();
        String repUnitId = prcsRepUnitMap.get(ElectionProcessor.REP_UNIT_ID);
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
        String candIdStr = candiMap.get(ElectionProcessor.CANDIDATE_ID);
        String firstName = candiMap.get(ElectionProcessor.FIRST_NAME);
        String lastName = candiMap.get(ElectionProcessor.LAST_NAME);
        String gender = candiMap.get(ElectionProcessor.GENDER);
        String localityName = candiMap.get(ElectionProcessor.LOCALITY_NAME);
        String electionId = candiMap.get(ElectionProcessor.ELECTION_ID);
        String constIdStr = candiMap.get(ElectionProcessor.CONTEST_ID);
        String affIdStr = candiMap.get(ElectionProcessor.AFFILIATION_ID);
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

    @Override
    public void registerCandiLevel_ConstiData(Map<String, String> constiMap) {

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
                Candidate newCandidate = new Candidate(candId, firstName, lastName);
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
