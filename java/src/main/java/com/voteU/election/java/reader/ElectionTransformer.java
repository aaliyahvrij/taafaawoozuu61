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
        // Get or create an Election object
        Election election = elections.get(electionId);
        if (election == null) {
            election = new Election(electionId, electionName, electionDate);
            elections.put(electionId, election);
        }
    }

    @Override
    public void registerNationalLevelData(Map<String, String> nationMap) {
        String electionId = nationMap.get(ElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
        Map<Integer, Affiliation> affiMap = election.getAffiliations();
        System.out.println("Amount of affiliations in this election: " + affiMap.size());
        Affiliation affiliation;
        int affId = Integer.parseInt(nationMap.get(ElectionProcessor.AFFILIATION_ID));
        affiliation = affiMap.get(affId);
        System.out.println("affiliation object: " + affiliation);

        // Handle affiliation-specific data
        if (!nationMap.containsKey(ElectionProcessor.SHORT_CODE)) {
            String affiName = nationMap.get(ElectionProcessor.AFFILIATION_NAME);
            int affiVotes = Integer.parseInt(nationMap.get(ElectionProcessor.VALID_VOTES));
            affiliation = new Affiliation(affId, affiName, affiVotes);
            affiMap.put(affId, affiliation);
        }

        // Handle candidate-specific data
        else {
            String candiShortCode = nationMap.get(ElectionProcessor.SHORT_CODE);
            int candiVotes = Integer.parseInt(nationMap.get("CandiVotes"));
            // Check if the candidate has already been registered, and added to their respective affiliation
            if (!affiliation.hasCandiShortCode(candiShortCode)) {
                Candidate candidate = new Candidate(candiShortCode, candiVotes);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerConstiLevelData(Map<String, String> prcsConstiMap, Map<String, String> affiNamesMap, Map<String, Integer> affiVotesMap, Map<Integer, Map<Integer, Integer>> candiVotesMap) {
        // had to remove the code here due to confusion - will fix this later
    }

    @Override
    public void registerAuthority(Map<String, String> prcsAuthorityMap) {
        int constId = Integer.parseInt(prcsAuthorityMap.get(ElectionProcessor.CONTEST_ID));
        String authorityId = prcsAuthorityMap.get(ElectionProcessor.AUTHORITY_ID);
        String authorityName = prcsAuthorityMap.get("AuthorityName");
        int affId = Integer.parseInt(prcsAuthorityMap.get(ElectionProcessor.AFFILIATION_ID));
        String affiName = prcsAuthorityMap.get(ElectionProcessor.AFFILIATION_NAME);
        String electionId = prcsAuthorityMap.get(ElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
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
            if (affiVotesStr == null) {
                return;
            }
            int affiVotes = 0;
            try {
                affiVotes = Integer.parseInt(affiVotesStr);
            } catch (NumberFormatException ignored) {
            }
            affiliation = new Affiliation(affId, affiName, affiVotes);
            affiMap.put(affId, affiliation);
        }
        if (authorityMap.containsKey("CandiVotes")) {
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
    public void registerRepUnit(Map<String, String> prcsRepUnitMap, Map<Integer, Affiliation> repUnitAffiliationsMap) {
        String electionId = prcsRepUnitMap.get(ElectionProcessor.ELECTION_ID);
        Election election = elections.get(electionId);
        Map<String, RepUnit> repUnitMap = election.getRepUnits();
        String repUnitId = prcsRepUnitMap.get(ElectionProcessor.REP_UNIT_ID);
        String repUnitName = prcsRepUnitMap.get("RepUnitName");
        int repUnitVotes = Integer.parseInt(prcsRepUnitMap.get("RepUnitVotes"));
        RepUnit repUnit = new RepUnit(repUnitId, repUnitName, repUnitAffiliationsMap, repUnitVotes);
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
        int candId = Integer.parseInt(candIdStr);
        int constId = Integer.parseInt(constIdStr);
        int affId = Integer.parseInt(affIdStr);
        Election election = elections.get(electionId);
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
