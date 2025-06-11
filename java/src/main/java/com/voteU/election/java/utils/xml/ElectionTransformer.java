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
    public void registerNationalLevel_affiData(LinkedHashMap<String, String> nationMap) {
        String electionId = nationMap.get("electionId");
        Election election = this.electionListMap.get(electionId);
        LinkedHashMap<Integer, Affiliation> affiListMap = election.getAffiListMap();
        Affiliation affi;
        int affId = Integer.parseInt(nationMap.get("affId"));
        affi = affiListMap.get(affId);

        // Handle affiliation-specific data
        if (!nationMap.containsKey("candiShortCode")) {
            String affiName = nationMap.get("affiName");
            int affiVVCount = Integer.parseInt(nationMap.get("affiVVCount"));
            affi = new Affiliation(affId, affiName, affiVVCount);
            affiListMap.put(affId, affi);
        }

        // Handle candidate-specific data
        else {
            String candiShortCode = nationMap.get("candiShortCode");
            int candiVVCount = Integer.parseInt(nationMap.get("candiVVCount"));
            if (!affi.hasCandiShortCode(candiShortCode)) {
                Candidate candi = new Candidate(candiShortCode, candiVVCount);
                affi.addCandi(candi);
            }
        }
    }

    @Override
    public void registerConstiLevel_affiData(LinkedHashMap<String, String> prcsConstiMap, List<String> affiNameList, List<Integer> affiVVCountList, LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> candiMap) {
        // ee
    }

    @Override
    public void registerMuniLevel_affiData(LinkedHashMap<String, String> muniMap) {
        int constId = Integer.parseInt(muniMap.get("constId"));
        String munId = muniMap.get("munId");
        String muniName = muniMap.get("muniName");
        int affId = Integer.parseInt(muniMap.get("affId"));
        String affiName = muniMap.get("affiName");
        String electionId = muniMap.get("electionId");
        Election election = this.electionListMap.get(electionId);
        LinkedHashMap<String, Municipality> nationalLevel_muniListMap = election.getMuniListMap();
        Municipality nationalLevel_muni = nationalLevel_muniListMap.computeIfAbsent(munId, id -> {
            Municipality muni = new Municipality(id);
            muni.setName(muniName);
            muni.setConstId(constId);
            return muni;
        });
        LinkedHashMap<Integer, Affiliation> muniLevel_affiListMap = nationalLevel_muni.getAffiListMap();
        Affiliation muniLevel_affi = muniLevel_affiListMap.get(affId);
        if (muniLevel_affi == null) {
            int affiVVCount = Integer.parseInt(muniMap.get("affiVVCount"));
            muniLevel_affi = new Affiliation(affId, affiName, affiVVCount);
            muniLevel_affiListMap.put(affId, muniLevel_affi);
        }
        if (nationalLevel_muniListMap.containsKey("candiVVCount")) {
            int candId = Integer.parseInt(muniMap.get("candId"));
            int candiVVCount = Integer.parseInt(muniMap.get("candiVVCount"));
            if (!muniLevel_affi.hasCandId(candId)) {
                Candidate candi = new Candidate(candId, candiVVCount);
                muniLevel_affi.addCandi(candi);
            }
        }
    }

    @Override
    public void registerPoStLevelData(LinkedHashMap<String, String> poStMap, LinkedHashMap<Integer, Affiliation> poStLevel_affiListMap) {
        String electionId = poStMap.get("electionId");
        Election election = this.electionListMap.get(electionId);
        LinkedHashMap<String, PollingStation> nationalLevel_poStListMap = election.getPoStListMap();
        String poStId = poStMap.get("poStId");
        String poStName = poStMap.get("poStName");
        int poStVVCount = Integer.parseInt(poStMap.get("poStVVCount"));
        PollingStation poSt = new PollingStation(poStId, poStName, poStLevel_affiListMap, poStVVCount);
        nationalLevel_poStListMap.put(poStId, poSt);
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
        LinkedHashMap<Integer, Constituency> nationalLevel_constiListMap = election.getConstiListMap();
        Constituency nationalLevel_consti = nationalLevel_constiListMap.get(constId);
        if (nationalLevel_consti != null) {
            // Update or insert a candidate in a constituencial level affiliation
            LinkedHashMap<Integer, Affiliation> constiLevel_affiListMap = nationalLevel_consti.getAffiListMap();
            populateCandi(candId, firstName, lastName, gender, localityName, affId, constiLevel_affiListMap);

            // Update or insert a candidate in each municipal level affiliation
            LinkedHashMap<String, Municipality> constiLevel_muniListMap = nationalLevel_consti.getMuniListMap();
            for (Municipality constiLevel_muni : constiLevel_muniListMap.values()) {
                LinkedHashMap<Integer, Affiliation> muniLevel_affiListMap = constiLevel_muni.getAffiListMap();
                populateCandi(candId, firstName, lastName, gender, localityName, affId, muniLevel_affiListMap);
                LinkedHashMap<String, PollingStation> muniLevel_poStListMap = constiLevel_muni.getPoStListMap();
                for (PollingStation muniLevel_poSt : muniLevel_poStListMap.values()) {
                    populateCandi(candId, firstName, lastName, gender, localityName, affId, muniLevel_poSt.getAffiListMap());
                }
            }
        }
    }

    @Override
    public void registerCandiLevel_constiData(LinkedHashMap<String, String> constiMap) {

    }

    private void populateCandi(int candId, String firstName, String lastName, String gender, String localityName, int affId, LinkedHashMap<Integer, Affiliation> affiListMap) {
        Affiliation affi = affiListMap.get(affId);
        if (affi != null) {
            List<Candidate> affiLevel_candiList = affi.getCandiList();
            Candidate candi = null;
            for (Candidate affiLevel_candi : affiLevel_candiList) {
                if (affiLevel_candi.getId() == candId && affiLevel_candi.getAffId() == affId) {
                    candi = affiLevel_candi;
                    candi.setFirstName(firstName);
                    candi.setLastName(lastName);
                    candi.setGender(gender);
                    candi.setLocalityName(localityName);
                    break;
                }
            }
            if (candi == null) {
                candi = new Candidate(candId, firstName, lastName);
                candi.setGender(gender);
                candi.setLocalityName(localityName);
                candi.setAffId(affId);
                affi.addCandi(candi);
            }
        }
    }

    /**
     * Retrieves all the data of a specific election.
     */
    public Election getElectoralDataOf(String electionId) {
        return this.electionListMap.get(electionId);
    }
}
