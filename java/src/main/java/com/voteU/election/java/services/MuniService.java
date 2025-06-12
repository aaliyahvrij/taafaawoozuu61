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

    public LinkedHashMap<String, Municipality> getConstiLevel_muniListMap(String electionId, int constId) {
        Election election = electionService.getElectoralDataOf(electionId);
        if (election == null) {
            throw new ResourceNotFoundException("Election " + electionId + " not found");
        }
        Constituency consti = election.getConstiListMap().get(constId);
        if (consti == null) {
            throw new ResourceNotFoundException("Consti " + constId + " not found in election " + electionId);
        }
        return consti.getMuniListMap();
    }

    public LinkedHashMap<String, Municipality> getConstiLevel_compactMuniListMap(String electionId, int constId) {
        Election election = electionService.getElectoralDataOf(electionId);
        if (election == null) {
            throw new ResourceNotFoundException("Election " + electionId + " not found");
        }
        Constituency constituency = election.getConstiListMap().get(constId);
        if (constituency == null) {
            throw new ResourceNotFoundException("Consti " + constId + " not found in election " + electionId);
        }
        LinkedHashMap<String, Municipality> constiLevel_muniListMap = constituency.getMuniListMap();
        LinkedHashMap<String, Municipality> compactMuniListMap = new LinkedHashMap<>();
        for (Municipality constiLevel_muni : constiLevel_muniListMap.values()) {
            compactMuniListMap.put(constiLevel_muni.getId(), new Municipality(constiLevel_muni.getId(), constiLevel_muni.getName()));
        }
        return compactMuniListMap;
    }

    public Municipality getConstiLevel_muni(String electionId, Integer constId, String munId) {
        LinkedHashMap<String, Municipality> constiLevel_muniListMap = getConstiLevel_muniListMap(electionId, constId);
        Municipality constiLevel_muni = constiLevel_muniListMap.get(munId);
        if (constiLevel_muni == null) {
            throw new ResourceNotFoundException("Muni " + munId + " not found in consti " + constId);
        }
        return constiLevel_muni;
    }

    public LinkedHashMap<Integer, Affiliation> getMuniLevel_affiListMap(String electionId, Integer constId, String munId) {
        Municipality muni = getConstiLevel_muni(electionId, constId, munId);
        if (muni == null) {
            throw new ResourceNotFoundException("Muni " + munId + " not found");
        }
        return muni.getAffiListMap();
    }

    public Affiliation getMuniLevel_affi(String electionId, Integer constId, String munId, Integer affId) {
        LinkedHashMap<Integer, Affiliation> muniLevel_affiListMap = getMuniLevel_affiListMap(electionId, constId, munId);
        Affiliation affi = muniLevel_affiListMap.get(affId);
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
