package com.voteU.election.java.services;

import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.models.Election;
import com.voteU.election.java.models.Constituency;
import com.voteU.election.java.models.Municipality;

import java.util.LinkedHashMap;

public class ConstiService {
    public ConstiService() {
    }

    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getConstiLevel_muniList_lhMap(String electionIdListString, int constId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, Municipality>> muniList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Election election = ElectionService.electionList_lhMap.get(electionId);
            Constituency consti = election.getConstiList_lhMap().get(constId);
            if (consti == null) {
                throw new ResourceNotFoundException("Consti " + constId + " not found in election " + electionId);
            }
            muniList_list_lhMap.put(electionId, consti.getMuniList_lhMap());
        }
        return muniList_list_lhMap;
    }

    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getConstiLevel_compactMuniList_lhMap(String electionIdListString, int constId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, Municipality>> compactMuniList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Election election = ElectionService.electionList_lhMap.get(electionId);
            Constituency consti = election.getConstiList_lhMap().get(constId);
            if (consti == null) {
                throw new ResourceNotFoundException("Consti " + constId + " not found in election " + electionId);
            }
            LinkedHashMap<String, Municipality> constiLevel_muniList_lhMap = consti.getMuniList_lhMap();
            LinkedHashMap<String, Municipality> compactMuniList_lhMap = new LinkedHashMap<>();
            for (Municipality constiLevel_muni : constiLevel_muniList_lhMap.values()) {
                compactMuniList_lhMap.put(constiLevel_muni.getId(), new Municipality(constiLevel_muni.getId(), constiLevel_muni.getName()));
            }
            compactMuniList_list_lhMap.put(electionId, compactMuniList_lhMap);
        }
        return compactMuniList_list_lhMap;
    }

    public LinkedHashMap<String, Municipality> getConstiLevel_muni(String electionIdListString, Integer constId, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, Municipality> constiLevel_muniList_lhMap = null;
        for (String electionId : electionIdList) {
            Municipality constiLevel_muni = ElectionService.electionList_lhMap.get(electionId).getConstiList_lhMap().get(constId).getMuniList_lhMap().get(munId);
            if (constiLevel_muni == null) {
                throw new ResourceNotFoundException("Muni " + munId + " not found in consti " + constId);
            }
            constiLevel_muniList_lhMap.put(munId, constiLevel_muni);
        }
        return constiLevel_muniList_lhMap;
    }
}
