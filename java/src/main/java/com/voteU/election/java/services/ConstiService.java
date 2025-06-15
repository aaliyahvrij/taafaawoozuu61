package com.voteU.election.java.services;

import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.models.Election;
import com.voteU.election.java.models.Constituency;
import com.voteU.election.java.models.Municipality;

import java.util.LinkedHashMap;

public class ConstiService {
    public ConstiService() {
    }

    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getMuniList_lhMap(String electionIdListString, int constId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, Municipality>> muniList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Election election = ElectionService.electionList_lhMap.get(electionId);
            Constituency consti = election.getConstiList_lhMap().get(constId);
            if (consti == null) {
                throw new ResourceNotFoundException("Election " + electionId + " > consti " + constId + " not found in election " + electionId);
            }
            muniList_list_lhMap.put(electionId, consti.getMuniList_lhMap());
        }
        return muniList_list_lhMap;
    }

    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getCompactMuniList_lhMap(String electionIdListString, int constId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, Municipality>> compactMuniList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Election election = ElectionService.electionList_lhMap.get(electionId);
            Constituency consti = election.getConstiList_lhMap().get(constId);
            if (consti == null) {
                throw new ResourceNotFoundException("Election " + electionId + " > consti " + constId + " not found in election " + electionId);
            }
            LinkedHashMap<String, Municipality> muniList_lhMap = consti.getMuniList_lhMap();
            LinkedHashMap<String, Municipality> compactMuniList_lhMap = new LinkedHashMap<>();
            for (Municipality muni : muniList_lhMap.values()) {
                compactMuniList_lhMap.put(muni.getId(), new Municipality(muni.getId(), muni.getName()));
            }
            compactMuniList_list_lhMap.put(electionId, compactMuniList_lhMap);
        }
        return compactMuniList_list_lhMap;
    }

    public LinkedHashMap<String, Municipality> getMuni(String electionIdListString, Integer constId, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, Municipality> muniList_lhMap = null;
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionList_lhMap.get(electionId).getConstiList_lhMap().get(constId).getMuniList_lhMap().get(munId);
            if (muni == null) {
                throw new ResourceNotFoundException("Election " + electionId + " > muni " + munId + " not found in consti " + constId);
            }
            muniList_lhMap.put(munId, muni);
        }
        return muniList_lhMap;
    }
}
