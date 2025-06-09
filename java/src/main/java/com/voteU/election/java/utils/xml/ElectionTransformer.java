package com.voteU.election.java.utils.xml;

import com.voteU.election.java.models.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.LinkedHashMap;

/**
 * A Transformer that processes election data and organizes it into Election objects.
 */
@Getter
@Slf4j
public class ElectionTransformer implements Transformer<Election> {
    private final LinkedHashMap<String, Election> electionListMap = new LinkedHashMap<>();

    @Override
    public void registerElectoralLevelData(LinkedHashMap<String, String> prcsElectionMap) {
        String electionId = prcsElectionMap.get("electionId");
        String electionName = prcsElectionMap.get("electionName");
        String electionDate = prcsElectionMap.get("electionDate");

        // Get or create an Election object
        Election electionMap = this.electionListMap.get(electionId);
        if (electionMap == null) {
            electionMap = new Election(electionId, electionName, electionDate);
            this.electionListMap.put(electionId, electionMap);
        }
    }

    @Override
    public void registerNationalLevel_affiOrCandiData(LinkedHashMap<String, String> nationMap) {
        String electionId = nationMap.get("electionId");
        Election election = this.electionListMap.get(electionId);
        LinkedHashMap<Integer, Affiliation> affiListMap = election.getAffiliations();
        Affiliation affiliation;
        int affId = Integer.parseInt(nationMap.get("affId"));
        affiliation = affiListMap.get(affId);

        // Handle affiliation-specific data
        if (!nationMap.containsKey("candiShortCode")) {
            String affiName = nationMap.get("affiName");
            int affiVVCount = Integer.parseInt(nationMap.get("affiVVCount"));
            affiliation = new Affiliation(affId, affiName, affiVVCount);
            affiListMap.put(affId, affiliation);
        }

        // Handle candidate-specific data
        else {
            String candiShortCode = nationMap.get("candiShortCode");
            int candiVVCount = Integer.parseInt(nationMap.get("candiVVCount"));
            if (!affiliation.hasCandiShortCode(candiShortCode)) {
                Candidate candidate = new Candidate(candiShortCode, candiVVCount);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerConstiLevelData(LinkedHashMap<String, String> prcsConstiMap, List<String> affiNameList, List<Integer> affiVVCountList, LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> candiMap) {
        // had to remove the code here due to confusion - will fix this later
    }

    @Override
    public void registerMuniLevelData(LinkedHashMap<String, String> muniMap) {
        int constId = Integer.parseInt(muniMap.get("constId"));
        String munId = muniMap.get("munId");
        String muniName = muniMap.get("muniName");
        int affId = Integer.parseInt(muniMap.get("affId"));
        String affiName = muniMap.get("affiName");
        String electionId = muniMap.get("electionId");
        Election election = this.electionListMap.get(electionId);
        LinkedHashMap<String, Municipality> electoralLevel_muniListMap = election.getMunicipalities();
        Municipality municipality = electoralLevel_muniListMap.computeIfAbsent(munId, id -> {
            Municipality muni = new Municipality(id);
            muni.setName(muniName);
            muni.setConstId(constId);
            return muni;
        });
        LinkedHashMap<Integer, Affiliation> muniLevel_affiListMap = municipality.getAffiliations();
        Affiliation affiliation = muniLevel_affiListMap.get(affId);
        if (affiliation == null) {
            int affiVVCount = Integer.parseInt(muniMap.get("affiVVCount"));
            affiliation = new Affiliation(affId, affiName, affiVVCount);
            muniLevel_affiListMap.put(affId, affiliation);
        }
        if (electoralLevel_muniListMap.containsKey("candiVVCount")) {
            int candId = Integer.parseInt(muniMap.get("candId"));
            int candiVVCount = Integer.parseInt(muniMap.get("candiVVCount"));
            if (!affiliation.hasCandId(candId)) {
                Candidate candidate = new Candidate(candId, candiVVCount);
                affiliation.addCandidate(candidate);
            }
        }
    }

    @Override
    public void registerPoStLevelData(LinkedHashMap<String, String> poStMap, LinkedHashMap<Integer, Affiliation> poStLevel_affiListMap) {
        String electionId = poStMap.get("electionId");
        Election election = this.electionListMap.get(electionId);
        LinkedHashMap<String, PollingStation> electoralLevel_poStListMap = election.getPollingStations();
        String poStId = poStMap.get("poStId");
        String poStName = poStMap.get("poStName");
        int poStVVCount = Integer.parseInt(poStMap.get("poStVVCount"));
        PollingStation pollingStation = new PollingStation(poStId, poStName, poStLevel_affiListMap, poStVVCount);
        electoralLevel_poStListMap.put(poStId, pollingStation);
    }

    @Override
    public void registerCandiLevelData(LinkedHashMap<String, String> candiMap) {
        int candId = Integer.parseInt(candiMap.get("candId"));
        String firstName = candiMap.get("firstName");
        String lastName = candiMap.get("lastName");
        String gender = candiMap.get("gender");
        String localityName = candiMap.get("localityName");
        int constId = Integer.parseInt(candiMap.get("constId"));
        int affId = Integer.parseInt(candiMap.get("affId"));
        String electionId = candiMap.get("electionId");
        Election election = this.electionListMap.get(electionId);
        LinkedHashMap<Integer, Constituency> electoralLevel_constiListMap = election.getConstituencies();
        Constituency constituency = electoralLevel_constiListMap.get(constId);
        if (constituency != null) {
            // Update or insert a candidate in a constituencial level affiliation
            LinkedHashMap<Integer, Affiliation> constiLevel_affiListMap = constituency.getAffiliations();
            populateCandidate(candId, firstName, lastName, gender, localityName, affId, constiLevel_affiListMap);

            // Update or insert a candidate in each municipal level affiliation
            LinkedHashMap<String, Municipality> constiLevel_muniListMap = constituency.getMunicipalities();
            for (Municipality municipality : constiLevel_muniListMap.values()) {
                LinkedHashMap<Integer, Affiliation> muniLevel_affiListMap = municipality.getAffiliations();
                populateCandidate(candId, firstName, lastName, gender, localityName, affId, muniLevel_affiListMap);
                LinkedHashMap<String, PollingStation> pollingStationListMap = municipality.getPollingStations();
                for (PollingStation pollingStation : pollingStationListMap.values()) {
                    populateCandidate(candId, firstName, lastName, gender, localityName, affId, pollingStation.getAffiliations());
                }
            }
        }
    }

    @Override
    public void registerCandiLevel_constiData(LinkedHashMap<String, String> constiMap) {

    }

    private void populateCandidate(int candId, String firstName, String lastName, String gender, String localityName, int affId, LinkedHashMap<Integer, Affiliation> affiListMap) {
        Affiliation affiliation = affiListMap.get(affId);
        if (affiliation != null) {
            List<Candidate> affiLevel_candidates = affiliation.getCandidates();
            Candidate candidate = null;
            for (Candidate affiLevel_candidate : affiLevel_candidates) {
                if (affiLevel_candidate.getId() == candId && affiLevel_candidate.getAffId() == affId) {
                    candidate = affiLevel_candidate;
                    candidate.setFirstName(firstName);
                    candidate.setLastName(lastName);
                    candidate.setGender(gender);
                    candidate.setLocalityName(localityName);
                    break;
                }
            }
            if (candidate == null) {
                Candidate newCandidate = new Candidate(candId, firstName, lastName);
                newCandidate.setGender(gender);
                newCandidate.setLocalityName(localityName);
                newCandidate.setAffId(affId);
                affiliation.addCandidate(newCandidate);
            }
        }
    }

    /**
     * Retrieves all the data of a specific election.
     */
    public Election getElectoralDataOf(String year) {
        return this.electionListMap.get(year);
    }
}
