package com.voteU.election.java.services;

import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.models.Election;
import com.voteU.election.java.models.Constituency;
import com.voteU.election.java.models.Municipality;

import java.util.LinkedHashMap;

public class ConstiService {
    private final ElectionService electionService;

    public ConstiService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getConstiLevel_muniList_lhMap(String electionIdListString, int constId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, Municipality>> muniList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Election election = ElectionService.electionList_lhMap.get(electionId);
            if (election == null) {
                throw new ResourceNotFoundException("Election " + electionId + " not found");
            }
            Constituency electoralLevel_consti = election.getConstiList_lhMap().get(constId);
            if (electoralLevel_consti == null) {
                throw new ResourceNotFoundException("Consti " + constId + " not found in election " + electionId);
            }
            muniList_list_lhMap.put(electionId, electoralLevel_consti.getMuniList_lhMap());
        }
        return muniList_list_lhMap;
    }

    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getConstiLevel_compactMuniList_lhMap(String electionIdListString, int constId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, Municipality>> compactMuniList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Election election = ElectionService.electionList_lhMap.get(electionId);
            if (election == null) {
                throw new ResourceNotFoundException("Election " + electionId + " not found");
            }
            Constituency electoralLevel_consti = election.getConstiList_lhMap().get(constId);
            if (electoralLevel_consti == null) {
                throw new ResourceNotFoundException("Consti " + constId + " not found in election " + electionId);
            }
            LinkedHashMap<String, Municipality> constiLevel_muniList_lhMap = electoralLevel_consti.getMuniList_lhMap();
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
            Municipality constiLevel_muni = /*constiService.*/getConstiLevel_muniList_lhMap(electionId, constId).get(munId);
            if (constiLevel_muni == null) {
                throw new ResourceNotFoundException("Muni " + munId + " not found in consti " + constId);
            }
            constiLevel_muniList_lhMap.put(munId, constiLevel_muni);
        }
        return constiLevel_muniList_lhMap;
    }
}
