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
    private static final Map<Integer, Integer> DISTRICT_TO_PROVI_ID = Map.ofEntries(Map.entry(3, 1),  // Drenthe
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
    public void registerElectoralLevelData(Map<String, String> prcsElectionMap) {
        String electionId = prcsElectionMap.get(ElectionProcessor.ELECTION_ID);
        String electionName = prcsElectionMap.get(ElectionProcessor.ELECTION_NAME);
        String electionDate = prcsElectionMap.get(ElectionProcessor.ELECTION_DATE);

        // Get or create an Election object
        Election electionMap = this.electionListMap.get(electionId);
        if (electionMap == null) {
            electionMap = new Election(electionId, electionName, electionDate);
            this.electionListMap.put(electionId, electionMap);
        }
    }

    @Override
    public void registerNationalLevelData(Map<String, String> nationMap) {
        String electionId = nationMap.get(ElectionProcessor.ELECTION_ID);
        Election election = this.electionListMap.get(electionId);
        Map<Integer, Affiliation> affiListMap = election.getAffiliations();
        System.out.println("The amount of affiliations of this election: " + affiListMap.size());
        Affiliation affiliation;
        int affId = Integer.parseInt(nationMap.get(ElectionProcessor.AFFI_ID));
        affiliation = affiListMap.get(affId);

        // Handle affiliation-specific data
        if (!nationMap.containsKey(ElectionProcessor.SHORT_CODE)) {
            String affiName = nationMap.get(ElectionProcessor.AFFI_NAME);
            int affiVVCount = Integer.parseInt(nationMap.get(ElectionProcessor.VV_COUNT));
            affiliation = new Affiliation(affId, affiName, affiVVCount);
            affiListMap.put(affId, affiliation);
        }

        // Handle candidate-specific data
        else {
            String candiShortCode = nationMap.get(ElectionProcessor.SHORT_CODE);
            int candiVVCount = Integer.parseInt(nationMap.get("candiVVCount"));
            if (!affiliation.hasCandiShortCode(candiShortCode)) {
                Candidate candidate = new Candidate(candiShortCode, candiVVCount);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerConstiLevelData(Map<String, String> prcsConstiMap, List<String> affiNamesList, List<Integer> affiVotesList, Map<Integer, Map<Integer, Integer>> candiVotesMap) {
        // had to remove the code here due to confusion - will fix this later
    }

    @Override
    public void registerMuniLevelData(Map<String, String> prcsMuniMap) {
        int constId = Integer.parseInt(prcsMuniMap.get(ElectionProcessor.CONSTI_ID));
        String munId = prcsMuniMap.get(ElectionProcessor.MUNI_ID);
        String muniName = prcsMuniMap.get("muniName");
        int affId = Integer.parseInt(prcsMuniMap.get(ElectionProcessor.AFFI_ID));
        String affiName = prcsMuniMap.get(ElectionProcessor.AFFI_NAME);
        String electionId = prcsMuniMap.get(ElectionProcessor.ELECTION_ID);
        Election election = this.electionListMap.get(electionId);
        Map<String, Municipality> muniMap = election.getMunicipalities();
        Municipality municipality = muniMap.computeIfAbsent(munId, id -> {
            Municipality m = new Municipality(id);
            m.setName(muniName);
            m.setConstId(constId);
            return m;
        });
        Map<Integer, Affiliation> affiListMap = municipality.getAffiliations();
        Affiliation affiliation = affiListMap.get(affId);
        if (affiliation == null) {
            int affiVVCount = Integer.parseInt(prcsMuniMap.get(ElectionProcessor.VV_COUNT));
            affiliation = new Affiliation(affId, affiName, affiVVCount);
            affiListMap.put(affId, affiliation);
        }
        if (muniMap.containsKey("candiVVCount")) {
            int candId = Integer.parseInt(prcsMuniMap.get(ElectionProcessor.CANDI_ID));
            int candiVVCount = Integer.parseInt(prcsMuniMap.get("candiVVCount"));
            if (!affiliation.hasCandId(candId)) {
                Candidate candidate = new Candidate(candId, candiVVCount);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerPollingStationLevelData(Map<String, String> prcsPollingStationMap, Map<Integer, Affiliation> pollingStationLevel_affiListMap) {
        String electionId = prcsPollingStationMap.get(ElectionProcessor.ELECTION_ID);
        Election election = this.electionListMap.get(electionId);
        Map<String, PollingStation> electionLevel_pollingStationListMap = election.getPollingStations();
        String pollingStationId = prcsPollingStationMap.get(ElectionProcessor.POLLING_STATION_ID);
        String pollingStationName = prcsPollingStationMap.get("pollingStationName");
        int pollingStationVVCount = Integer.parseInt(prcsPollingStationMap.get("pollingStationVVCount"));
        PollingStation pollingStation = new PollingStation(pollingStationId, pollingStationName, pollingStationLevel_affiListMap, pollingStationVVCount);
        electionLevel_pollingStationListMap.put(pollingStationId, pollingStation);
    }

    @Override
    public void registerCandiLevelData(Map<String, String> candiMap) {
        int candId = Integer.parseInt(candiMap.get(ElectionProcessor.CANDI_ID));
        String firstName = candiMap.get(ElectionProcessor.FIRST_NAME);
        String lastName = candiMap.get(ElectionProcessor.LAST_NAME);
        String gender = candiMap.get(ElectionProcessor.GENDER);
        String localityName = candiMap.get(ElectionProcessor.LOCALITY_NAME);
        int constId = Integer.parseInt(candiMap.get(ElectionProcessor.CONSTI_ID));
        int affId = Integer.parseInt(candiMap.get(ElectionProcessor.AFFI_ID));
        String electionId = candiMap.get(ElectionProcessor.ELECTION_ID);
        Election election = this.electionListMap.get(electionId);
        Map<Integer, Constituency> electionLevel_constiListMap = election.getConstituencies();
        Constituency constituency = electionLevel_constiListMap.get(constId);
        if (constituency != null) {
            // Update or insert a candidate in a constituencial level affiliation
            Map<Integer, Affiliation> constiLevel_affiListMap = constituency.getAffiliations();
            populateCandidate(candId, firstName, lastName, gender, localityName, affId, constiLevel_affiListMap);

            // Update or insert a candidate in each municipal level affiliation
            Map<String, Municipality> constiLevel_muniListMap = constituency.getMunicipalities();
            for (Municipality municipality : constiLevel_muniListMap.values()) {
                Map<Integer, Affiliation> muniLevel_affiListMap = municipality.getAffiliations();
                populateCandidate(candId, firstName, lastName, gender, localityName, affId, muniLevel_affiListMap);
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
                newCandidate.setAffId(affId); // This may be missing in your original
                affiliation.addCandidate(newCandidate);
            }
        }
    }

    /**
     * Retrieves all the data of a specific election.
     */
    public Election getElectoralLevelDataOf(String year) {
        return this.electionListMap.get(year);
    }
}
