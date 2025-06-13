package com.voteU.election.java.services;

import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.models.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.LinkedHashMap;

@Service
public class MuniService {
    private final ElectionService electionService;

    public MuniService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public LinkedHashMap<String, Municipality> getConstiLevel_muniList_lhMap(String electionId, int constId) {
        Election election = electionService.getElectoralData(electionId);
        if (election == null) {
            throw new ResourceNotFoundException("Election " + electionId + " not found");
        }
        Constituency electoralLevel_consti = election.getConstiList_lhMap().get(constId);
        if (electoralLevel_consti == null) {
            throw new ResourceNotFoundException("Consti " + constId + " not found in election " + electionId);
        }
        return electoralLevel_consti.getMuniList_lhMap();
    }

    public LinkedHashMap<String, Municipality> getConstiLevel_compactMuniList_lhMap(String electionId, int constId) {
        Election election = electionService.getElectoralData(electionId);
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
        return compactMuniList_lhMap;
    }

    public Municipality getConstiLevel_muni(String electionId, Integer constId, String munId) {
        LinkedHashMap<String, Municipality> constiLevel_muniListMap = getConstiLevel_muniList_lhMap(electionId, constId);
        Municipality constiLevel_muni = constiLevel_muniListMap.get(munId);
        if (constiLevel_muni == null) {
            throw new ResourceNotFoundException("Muni " + munId + " not found in consti " + constId);
        }
        return constiLevel_muni;
    }

    public LinkedHashMap<Integer, Affiliation> getMuniLevel_affiList_lhMap(String electionId, Integer constId, String munId) {
        Municipality constiLevel_muni = getConstiLevel_muni(electionId, constId, munId);
        if (constiLevel_muni == null) {
            throw new ResourceNotFoundException("Muni " + munId + " not found");
        }
        return constiLevel_muni.getAffiList_lhMap();
    }

    public Affiliation getMuniLevel_affi(String electionId, Integer constId, String munId, Integer affId) {
        LinkedHashMap<Integer, Affiliation> muniLevel_affiList_lhMap = getMuniLevel_affiList_lhMap(electionId, constId, munId);
        Affiliation affi = muniLevel_affiList_lhMap.get(affId);
        if (affi == null) {
            throw new ResourceNotFoundException("Affi " + affId + " not found");
        }
        return affi;
    }

    public List<Candidate> getAffiLevel_candiList(String electionId, Integer constId, String munId, Integer affId) {
        Affiliation affi = getMuniLevel_affi(electionId, constId, munId, affId);
        if (affi == null) {
            throw new ResourceNotFoundException("Affi " + affId + " not found");
        }
        return affi.getCandiList();
    }
}
