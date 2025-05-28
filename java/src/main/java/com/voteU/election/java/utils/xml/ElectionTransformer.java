package com.voteU.election.java.utils.xml;

import com.voteU.election.java.models.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
@Getter
@Slf4j
public class ElectionTransformer implements Transformer<Election> {
    private final Map<String, Election> electionListMap = new HashMap<>();
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
    public void registerElection(Map<String, String> prcsElectionMap) {
        String electionId = prcsElectionMap.get(ElectionProcessor.ELECTION_ID);
        String electionName = prcsElectionMap.get(ElectionProcessor.ELECTION_NAME);
        String electionDate = prcsElectionMap.get(ElectionProcessor.ELECTION_DATE);

        // Get or create an Election object
        Election electionMap = electionListMap.get(electionId);
        if (electionMap == null) {
            electionMap = new Election(electionId, electionName, electionDate);
            electionListMap.put(electionId, electionMap);
        }
    }

    @Override
    public void registerNationalLevelData(Map<String, String> nationMap) {
        String electionId = nationMap.get(ElectionProcessor.ELECTION_ID);
        Election election = electionListMap.get(electionId);
        Map<Integer, Affiliation> affiListMap = election.getAffiliations();
        System.out.println("The amount of affiliations in this election: " + affiListMap.size());
        Affiliation affiliation;
        int affId = Integer.parseInt(nationMap.get(ElectionProcessor.AFFI_ID));
        affiliation = affiListMap.get(affId);

        // Handle affiliation-specific data
        if (!nationMap.containsKey(ElectionProcessor.SHORT_CODE)) {
            String affiName = nationMap.get(ElectionProcessor.AFFI_NAME);
            int affiVotes = Integer.parseInt(nationMap.get(ElectionProcessor.VALID_VOTES));
            affiliation = new Affiliation(affId, affiName, affiVotes);
            affiListMap.put(affId, affiliation);
        }

        // Handle candidate-specific data
        else {
            String candiShortCode = nationMap.get(ElectionProcessor.SHORT_CODE);
            int candiVotes = Integer.parseInt(nationMap.get("candiVotes"));
            if (!affiliation.hasCandiShortCode(candiShortCode)) {
                Candidate candidate = new Candidate(candiShortCode, candiVotes);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerConstiLevelData(Map<String, String> prcsConstiMap, List<String> affiNamesList, List<Integer> affiVotesList, Map<Integer, Map<Integer, Integer>> candiVotesMap) {
        // had to remove the code here due to confusion - will fix this later
    }

    @Override
    public void registerMunicipality(Map<String, String> prcsAuthoMap) {
        int constId = Integer.parseInt(prcsAuthoMap.get(ElectionProcessor.CONSTI_ID));
        String munId = prcsAuthoMap.get(ElectionProcessor.MUNI_ID);
        String muniName = prcsAuthoMap.get("muniName");
        int affId = Integer.parseInt(prcsAuthoMap.get(ElectionProcessor.AFFI_ID));
        String affiName = prcsAuthoMap.get(ElectionProcessor.AFFI_NAME);
        String electionId = prcsAuthoMap.get(ElectionProcessor.ELECTION_ID);
        Election election = electionListMap.get(electionId);
        Map<String, Municipality> authoMap = election.getMunicipalities();
        Municipality authority = authoMap.computeIfAbsent(munId, id -> {
            Municipality m = new Municipality(id);
            m.setName(muniName);
            m.setConstId(constId);
            return m;
        });
        Map<Integer, Affiliation> affiListMap = authority.getAffiliations();
        Affiliation affiliation = affiListMap.get(affId);
        if (affiliation == null) {
            int affiVotes = Integer.parseInt(prcsAuthoMap.get(ElectionProcessor.VALID_VOTES));
            affiliation = new Affiliation(affId, affiName, affiVotes);
            affiListMap.put(affId, affiliation);
        }
        if (authoMap.containsKey("candiVotes")) {
            int candId = Integer.parseInt(prcsAuthoMap.get(ElectionProcessor.CANDI_ID));
            int candiVotes = Integer.parseInt(prcsAuthoMap.get("candiVotes"));
            if (!affiliation.hasCandId(candId)) {
                Candidate candidate = new Candidate(candId, candiVotes);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerPollingStation(Map<String, String> prcsPollingStationMap, Map<Integer, Affiliation> pollingStationLevel_affiListMap) {
        String electionId = prcsPollingStationMap.get(ElectionProcessor.ELECTION_ID);
        Election election = electionListMap.get(electionId);
        Map<String, PollingStation> electionLevel_pollingStationListMap = election.getPollingStations();
        String pollingStationId = prcsPollingStationMap.get(ElectionProcessor.POLLING_STATION_ID);
        String pollingStationName = prcsPollingStationMap.get("pollingStationName");
        int pollingStationVotes = Integer.parseInt(prcsPollingStationMap.get("pollingStationVotes"));
        PollingStation pollingStation = new PollingStation(pollingStationId, pollingStationName, pollingStationLevel_affiListMap, pollingStationVotes);
        electionLevel_pollingStationListMap.put(pollingStationId, pollingStation);
    }

    @Override
    public void registerCandidate(Map<String, String> candiMap) {
        int candId = Integer.parseInt(candiMap.get(ElectionProcessor.CANDI_ID));
        String firstName = candiMap.get(ElectionProcessor.FIRST_NAME);
        String lastName = candiMap.get(ElectionProcessor.LAST_NAME);
        String gender = candiMap.get(ElectionProcessor.GENDER);
        String localityName = candiMap.get(ElectionProcessor.LOCALITY_NAME);
        int constId = Integer.parseInt(candiMap.get(ElectionProcessor.CONSTI_ID));
        int affId = Integer.parseInt(candiMap.get(ElectionProcessor.AFFI_ID));
        String electionId = candiMap.get(ElectionProcessor.ELECTION_ID);
        Election election = electionListMap.get(electionId);
        Map<Integer, Constituency> electionLevel_constiListMap = election.getConstituencies();
        Constituency constituency = electionLevel_constiListMap.get(constId);
        if (constituency != null) {
            // Update or insert a candidate in a constituency-level affiliation
            Map<Integer, Affiliation> constiLevel_affiListMap = constituency.getAffiliations();
            populateCandidate(candId, firstName, lastName, gender, localityName, affId, constiLevel_affiListMap);

            // Update or insert a candidate in each authority-level affiliation
            Map<String, Municipality> constiLevel_muniListMap = constituency.getMunicipalities();
            for (Municipality municipality : constiLevel_muniListMap.values()) {
                Map<Integer, Affiliation> authoLevel_affiListMap = municipality.getAffiliations();
                populateCandidate(candId, firstName, lastName, gender, localityName, affId, authoLevel_affiListMap);
                Map<String, PollingStation> pollingStationListMap = municipality.getPollingStations();
                for (PollingStation pollingStation : pollingStationListMap.values()) {
                    populateCandidate(candId, firstName, lastName, gender, localityName, affId, pollingStation.getAffiliations());
                }
            }
        }
    }

    @Override
    public void registerCandiLevel_constiData(Map<String, String> constiMap) {

    }

    private void populateCandidate(int candId, String firstName, String lastName, String gender, String localityName, int affId, Map<Integer, Affiliation> affiListMap) {
        Affiliation affiliation = affiListMap.get(affId);
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
                newCandidate.setAffId(affId); // This may be missing in your original, depending on the Candidate class
                affiliation.addCandidate(newCandidate);
            }
        }
    }

    /**
     * Get election data for a specific year.
     */
    public Election getElection(String year) {
        return electionListMap.get(year);
    }
}
