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
    private final Map<String, Election> pendingElectionListDataMap = new HashMap<>();
    //Map<Integer, Affiliation> affiListMap = new HashMap<>();
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
        Election pendingElectionDataMap = pendingElectionListDataMap.get(electionId);
        if (pendingElectionDataMap == null) {
            pendingElectionDataMap = new Election(electionId, electionName, electionDate);
            pendingElectionListDataMap.put(electionId, pendingElectionDataMap);
        }
    }

    @Override
    public void registerNationalLevelData(Map<String, String> nationMap) {
        String electionId = nationMap.get(ElectionProcessor.ELECTION_ID);
        Election election = pendingElectionListDataMap.get(electionId);
        Map<Integer, Affiliation> affiListMap = election.getAffiliations();
        System.out.println("The amount of affiliations in this election: " + affiListMap.size());
        Affiliation affiliation;
        int affId = Integer.parseInt(nationMap.get(ElectionProcessor.AFFILIATION_ID));
        affiliation = affiListMap.get(affId);

        // Handle affiliation-specific data
        if (!nationMap.containsKey(ElectionProcessor.SHORT_CODE)) {
            String affiName = nationMap.get(ElectionProcessor.AFFILIATION_NAME);
            int affiVotes = Integer.parseInt(nationMap.get(ElectionProcessor.VALID_VOTES));
            affiliation = new Affiliation(affId, affiName, affiVotes);
            affiListMap.put(affId, affiliation);
        }

        // Handle candidate-specific data
        else {
            String candiShortCode = nationMap.get(ElectionProcessor.SHORT_CODE);
            int candiVotes = Integer.parseInt(nationMap.get("CandiVotes"));
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
        Election election = pendingElectionListDataMap.get(electionId);
        Map<String, Authority> authorityMap = election.getAuthorities();
        Authority authority = authorityMap.computeIfAbsent(authorityId, id -> {
            Authority a = new Authority(id);
            a.setName(authorityName);
            a.setConstId(constId);
            return a;
        });
        Map<Integer, Affiliation> affiListMap = authority.getAffiliations();
        Affiliation affiliation = affiListMap.get(affId);
        if (affiliation == null) {
            int affiVotes = Integer.parseInt(prcsAuthorityMap.get(ElectionProcessor.VALID_VOTES));
            affiliation = new Affiliation(affId, affiName, affiVotes);
            affiListMap.put(affId, affiliation);
        }
        if (authorityMap.containsKey("CandiVotes")) {
            int candId = Integer.parseInt(prcsAuthorityMap.get(ElectionProcessor.CANDIDATE_ID));
            int candiVotes = Integer.parseInt(prcsAuthorityMap.get("CandiVotes"));
            if (!affiliation.hasCandId(candId)) {
                Candidate candidate = new Candidate(candId, candiVotes);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerRepUnit(Map<String, String> prcsRepUnitMap, Map<Integer, Affiliation> repUnitAffiliationsMap) {
        String electionId = prcsRepUnitMap.get(ElectionProcessor.ELECTION_ID);
        Election election = pendingElectionListDataMap.get(electionId);
        Map<String, RepUnit> repUnitListMap = election.getRepUnits();
        String repUnitId = prcsRepUnitMap.get(ElectionProcessor.REP_UNIT_ID);
        String repUnitName = prcsRepUnitMap.get("RepUnitName");
        int repUnitVotes = Integer.parseInt(prcsRepUnitMap.get("RepUnitVotes"));
        RepUnit repUnit = new RepUnit(repUnitId, repUnitName, repUnitAffiliationsMap, repUnitVotes);
        repUnitListMap.put(repUnitId, repUnit);
    }

    @Override
    public void registerCandidate(Map<String, String> candiMap) {
        int candId = Integer.parseInt(candiMap.get(ElectionProcessor.CANDIDATE_ID));
        String firstName = candiMap.get(ElectionProcessor.FIRST_NAME);
        String lastName = candiMap.get(ElectionProcessor.LAST_NAME);
        String gender = candiMap.get(ElectionProcessor.GENDER);
        String localityName = candiMap.get(ElectionProcessor.LOCALITY_NAME);
        String electionId = candiMap.get(ElectionProcessor.ELECTION_ID);
        int constId = Integer.parseInt(candiMap.get(ElectionProcessor.CONTEST_ID));
        int affId = Integer.parseInt(candiMap.get(ElectionProcessor.AFFILIATION_ID));
        Election election = pendingElectionListDataMap.get(electionId);
        Map<Integer, Constituency> constituencies = election.getConstituencies();
        Constituency constituency = constituencies.get(constId);
        if (constituency != null) {
            // Update or insert a candidate in a constituency-level affiliation
            Map<Integer, Affiliation> affiliations = constituency.getAffiliations();
            populateCandidate(candId, firstName, lastName, gender, localityName, affId, affiliations);

            // Update or insert a candidate in each authority-level affiliation
            Map<String, Authority> authorities = constituency.getAuthorities();
            for (Authority authority : authorities.values()) {
                Map<Integer, Affiliation> affiListMap = authority.getAffiliations();
                populateCandidate(candId, firstName, lastName, gender, localityName, affId, affiListMap);
                Map<String, RepUnit> repUnitListMap = authority.getRepUnits();
                for (RepUnit repUnit : repUnitListMap.values()) {
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
        return pendingElectionListDataMap.get(year);
    }
}
