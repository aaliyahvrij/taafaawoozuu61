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
    private final LinkedHashMap<String, Election> electionList_lhMap = new LinkedHashMap<>();

    @Override
    public void registerElectoralLevelData(LinkedHashMap<String, String> prcsElectionLhMap) {
        String electionId = prcsElectionLhMap.get("electionId");
        String electionName = prcsElectionLhMap.get("electionName");
        String electionDate = prcsElectionLhMap.get("electionDate");

        // Get or create an Election object
        Election electionLhMap = this.electionList_lhMap.get(electionId);
        if (electionLhMap == null) {
            electionLhMap = new Election(electionId, electionName, electionDate);
            this.electionList_lhMap.put(electionId, electionLhMap);
        }
    }

    @Override
    public void registerNationalLevel_affiData(LinkedHashMap<String, String> nationLhMap) {
        String electionId = nationLhMap.get("electionId");
        Election election = this.electionList_lhMap.get(electionId);
        LinkedHashMap<Integer, Affiliation> affiList_lhMap = election.getAffiList_lhMap();
        Affiliation affi;
        int affId = Integer.parseInt(nationLhMap.get("affId"));
        affi = affiList_lhMap.get(affId);

        // Handle affiliation-specific data
        if (!nationLhMap.containsKey("candiShortCode")) {
            String affiName = nationLhMap.get("affiName");
            int affiVVCount = Integer.parseInt(nationLhMap.get("affiVVCount"));
            affi = new Affiliation(affId, affiName, affiVVCount);
            affiList_lhMap.put(affId, affi);
        }

        // Handle candidate-specific data
        else {
            String candiShortCode = nationLhMap.get("candiShortCode");
            int candiVVCount = Integer.parseInt(nationLhMap.get("candiVVCount"));
            if (!affi.hasCandiShortCode(candiShortCode)) {
                Candidate candi = new Candidate(candiShortCode, candiVVCount);
                affi.addCandi(candi);
            }
        }
    }

    @Override
    public void registerConstiLevel_affiData(LinkedHashMap<String, String> prcsConstiLhMap, List<String> affiNameList, List<Integer> affiVVCountList, LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> candiLhMap) {
        // ee
    }

    @Override
    public void registerMuniLevel_affiData(LinkedHashMap<String, String> muniLhMap) {
        int constId = Integer.parseInt(muniLhMap.get("constId"));
        String munId = muniLhMap.get("munId");
        String muniName = muniLhMap.get("muniName");
        int affId = Integer.parseInt(muniLhMap.get("affId"));
        String affiName = muniLhMap.get("affiName");
        String electionId = muniLhMap.get("electionId");
        Election election = this.electionList_lhMap.get(electionId);
        LinkedHashMap<String, Municipality> nationalLevel_muniList_lhMap = election.getMuniList_lhMap();
        Municipality nationalLevel_muni = nationalLevel_muniList_lhMap.computeIfAbsent(munId, id -> {
            Municipality muni = new Municipality(id, muniName);
            muni.setConstId(constId);
            return muni;
        });
        LinkedHashMap<Integer, Affiliation> muniLevel_affiList_lhMap = nationalLevel_muni.getAffiList_lhMap();
        Affiliation muniLevel_affi = muniLevel_affiList_lhMap.get(affId);
        if (muniLevel_affi == null) {
            int affiVVCount = Integer.parseInt(muniLhMap.get("affiVVCount"));
            muniLevel_affi = new Affiliation(affId, affiName, affiVVCount);
            muniLevel_affiList_lhMap.put(affId, muniLevel_affi);
        }
        if (nationalLevel_muniList_lhMap.containsKey("candiVVCount")) {
            int candId = Integer.parseInt(muniLhMap.get("candId"));
            int candiVVCount = Integer.parseInt(muniLhMap.get("candiVVCount"));
            if (!muniLevel_affi.hasCandId(candId)) {
                Candidate candi = new Candidate(candId, candiVVCount);
                muniLevel_affi.addCandi(candi);
            }
        }
    }

    @Override
    public void registerPoStLevelData(LinkedHashMap<String, String> poStLhMap, LinkedHashMap<Integer, Affiliation> poStLevel_affiList_lhMap) {
        String electionId = poStLhMap.get("electionId");
        Election election = this.electionList_lhMap.get(electionId);
        LinkedHashMap<String, PollingStation> nationalLevel_poStList_lhMap = election.getPoStList_lhMap();
        String poStId = poStLhMap.get("poStId");
        String poStName = poStLhMap.get("poStName");
        int poStVVCount = Integer.parseInt(poStLhMap.get("poStVVCount"));
        PollingStation poSt = new PollingStation(poStId, poStName, poStLevel_affiList_lhMap, poStVVCount);
        nationalLevel_poStList_lhMap.put(poStId, poSt);
    }

    @Override
    public void registerCandiLevelData(LinkedHashMap<String, String> candiLhMap) {
        int candId = Integer.parseInt(candiLhMap.get("candId"));
        String firstName = candiLhMap.get("firstName");
        String lastName = candiLhMap.get("lastName");
        String gender = candiLhMap.get("gender");
        String localityName = candiLhMap.get("localityName");
        int constId = Integer.parseInt(candiLhMap.get("constId"));
        int affId = Integer.parseInt(candiLhMap.get("affId"));
        String electionId = candiLhMap.get("electionId");
        Election election = this.electionList_lhMap.get(electionId);
        LinkedHashMap<Integer, Constituency> nationalLevel_constiList_lhMap = election.getConstiList_lhMap();
        Constituency nationalLevel_consti = nationalLevel_constiList_lhMap.get(constId);
        if (nationalLevel_consti != null) {
            // Update or insert a candidate in a constituencial level affiliation
            LinkedHashMap<Integer, Affiliation> constiLevel_affiList_lhMap = nationalLevel_consti.getAffiList_lhMap();
            populateCandi(candId, firstName, lastName, gender, localityName, affId, constiLevel_affiList_lhMap);

            // Update or insert a candidate in each municipal level affiliation
            LinkedHashMap<String, Municipality> constiLevel_muniList_lhMap = nationalLevel_consti.getMuniList_lhMap();
            for (Municipality constiLevel_muni : constiLevel_muniList_lhMap.values()) {
                LinkedHashMap<Integer, Affiliation> muniLevel_affiList_lhMap = constiLevel_muni.getAffiList_lhMap();
                populateCandi(candId, firstName, lastName, gender, localityName, affId, muniLevel_affiList_lhMap);
                LinkedHashMap<String, PollingStation> muniLevel_poStList_lhMap = constiLevel_muni.getPoStList_lhMap();
                for (PollingStation muniLevel_poSt : muniLevel_poStList_lhMap.values()) {
                    populateCandi(candId, firstName, lastName, gender, localityName, affId, muniLevel_poSt.getAffiList_lhMap());
                }
            }
        }
    }

    @Override
    public void registerCandiLevel_constiData(LinkedHashMap<String, String> constiLhMap) {

    }

    private void populateCandi(int candId, String firstName, String lastName, String gender, String localityName, int affId, LinkedHashMap<Integer, Affiliation> affiList_lhMap) {
        Affiliation affi = affiList_lhMap.get(affId);
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
}
