package com.voteU.election.java.utils.xml;

import com.voteU.election.java.models.*;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Organizes processed election data into Election objects.
 */
public class ElectionTransformer implements Transformer<Election> {
    @Getter
    private final LinkedHashMap<String, Election> electionListLhMap = new LinkedHashMap<>();

    @Override
    public void registerElectoralLevelData(LinkedHashMap<String, String> electionLhMap) {
        String electionId = electionLhMap.get("electionId");
        String electionName = electionLhMap.get("electionName");
        String electionDate = electionLhMap.get("electionDate");

        // Get or create an Election object
        Election election = this.electionListLhMap.get(electionId);
        if (election == null) {
            election = new Election(electionId, electionName, electionDate);
            this.electionListLhMap.put(electionId, election);
        }
    }

    @Override
    public void registerNationalLevel_affiData(LinkedHashMap<String, String> nationLhMap) {
        String electionId = nationLhMap.get("electionId");
        Election election = this.electionListLhMap.get(electionId);
        LinkedHashMap<Integer, Affiliation> affiListLhMap = election.getAffiListLhMap();
        int affId = Integer.parseInt(nationLhMap.get("affId"));
        Affiliation affi = affiListLhMap.get(affId);

        // Handle affiliation-specific data
        if (!nationLhMap.containsKey("candiShortCode")) {
            String affiName = nationLhMap.get("affiName");
            int affiVVCount = Integer.parseInt(nationLhMap.get("affiVVCount"));
            affi = new Affiliation(affId, affiName, affiVVCount);
            affiListLhMap.put(affId, affi);
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
    public void registerConstiLevelData(LinkedHashMap<String, String> constiLhMap) {
        // ee
    }

    @Override
    public void registerConstiLevel_affiData(LinkedHashMap<String, String> constiLhMap, LinkedHashMap<Integer, Affiliation> affiListLhMap, LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> candiLhMap) {
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
        Election election = this.electionListLhMap.get(electionId);
        LinkedHashMap<String, Municipality> muniListLhMap = election.getMuniListLhMap();
        Municipality muni = muniListLhMap.computeIfAbsent(munId, id -> {
            Municipality m = new Municipality(id, muniName);
            m.setConstId(constId);
            return m;
        });
        LinkedHashMap<Integer, Affiliation> affiListLhMap = muni.getAffiListLhMap();
        Affiliation affi = affiListLhMap.get(affId);
        if (affi == null) {
            int affiVVCount = Integer.parseInt(muniLhMap.get("affiVVCount"));
            affi = new Affiliation(affId, affiName, affiVVCount);
            affiListLhMap.put(affId, affi);
        }
        if (muniListLhMap.containsKey("candiVVCount")) {
            int candId = Integer.parseInt(muniLhMap.get("candId"));
            int candiVVCount = Integer.parseInt(muniLhMap.get("candiVVCount"));
            if (!affi.hasCandId(candId)) {
                Candidate candi = new Candidate(candId, candiVVCount);
                affi.addCandi(candi);
            }
        }
    }

    @Override
    public void registerPoStLevelData(LinkedHashMap<String, String> poStLhMap, LinkedHashMap<Integer, Affiliation> affiListLhMap) {
        String electionId = poStLhMap.get("electionId");
        Election election = this.electionListLhMap.get(electionId);
        LinkedHashMap<String, PollingStation> poStListLhMap = election.getPoStListLhMap();
        String poStId = poStLhMap.get("poStId");
        String poStName = poStLhMap.get("poStName");
        int poStVVCount = Integer.parseInt(poStLhMap.get("poStVVCount"));
        PollingStation poSt = new PollingStation(poStId, poStName, affiListLhMap, poStVVCount);
        poStListLhMap.put(poStId, poSt);
    }

    @Override
    public void registerAffiLevelData() {
        // ee
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
        Election election = this.electionListLhMap.get(electionId);
        LinkedHashMap<Integer, Constituency> constiListLhMap = election.getConstiListLhMap();
        Constituency consti = constiListLhMap.get(constId);
        if (consti != null) {
            // Update or insert a candidate in a constituencial level affiliation
            LinkedHashMap<Integer, Affiliation> constiLevel_affiListLhMap = consti.getAffiListLhMap();
            populateCandi(candId, firstName, lastName, gender, localityName, affId, constiLevel_affiListLhMap);

            // Update or insert a candidate in each municipal level affiliation
            LinkedHashMap<String, Municipality> constiLevel_muniListLhMap = consti.getMuniListLhMap();
            for (Municipality constiLevel_muni : constiLevel_muniListLhMap.values()) {
                LinkedHashMap<Integer, Affiliation> muniLevel_affiListLhMap = constiLevel_muni.getAffiListLhMap();
                populateCandi(candId, firstName, lastName, gender, localityName, affId, muniLevel_affiListLhMap);
                LinkedHashMap<String, PollingStation> muniLevel_poStListLhMap = constiLevel_muni.getPoStListLhMap();
                for (PollingStation muniLevel_poSt : muniLevel_poStListLhMap.values()) {
                    populateCandi(candId, firstName, lastName, gender, localityName, affId, muniLevel_poSt.getAffiListLhMap());
                }
            }
        }
    }

    private void populateCandi(int candId, String firstName, String lastName, String gender, String localityName, int affId, LinkedHashMap<Integer, Affiliation> affiListLhMap) {
        Affiliation affi = affiListLhMap.get(affId);
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
