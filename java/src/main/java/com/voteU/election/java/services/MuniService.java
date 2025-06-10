package com.voteU.election.java.services;

//import com.voteU.election.java.exceptions.AccessDeniedException;
//import com.voteU.election.java.exceptions.ResourceAlreadyExistsException;
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

    public LinkedHashMap<String, Municipality> getConstiLevel_muniListMapOf(String electionId, int constId) {
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

    public LinkedHashMap<String, Municipality> getConstiLevel_compactMuniListMapOf(String electionId, int constId) {
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

    public Municipality getMuniById(String electionId, Integer constId, String munId) {
        LinkedHashMap<String, Municipality> constiLevel_muniListMap = getConstiLevel_muniListMapOf(electionId, constId);
        Municipality muni = constiLevel_muniListMap.get(munId);
        if (muni == null) {
            throw new ResourceNotFoundException("Muni " + munId + " not found in consti " + constId);
        }
        return muni;
    }

    public LinkedHashMap<Integer, Affiliation> getMuniLevel_affiListMapOf(String electionId, Integer constId, String munId) {
        Municipality muni = getMuniById(electionId, constId, munId);
        if (muni == null) {
            throw new ResourceNotFoundException("Muni " + munId + " not found");
        }
        return muni.getAffiListMap();
    }

    public Affiliation getAffiById(String electionId, Integer constId, String munId, Integer affId) {
        LinkedHashMap<Integer, Affiliation> muniLevel_affiListMap = getMuniLevel_affiListMapOf(electionId, constId, munId);
        Affiliation affi = muniLevel_affiListMap.get(affId);
        if (affi == null) {
            throw new ResourceNotFoundException("Affiliation " + affId + " not found");
        }
        return affi;
    }

    public List<Candidate> getAffiLevel_candiListOf(String electionId, Integer constId, String munId, Integer affId) {
        Affiliation affi = getAffiById(electionId, constId, munId, affId);
        if (affi == null) {
            throw new ResourceNotFoundException("Affiliation " + affId + " not found");
        }
        return affi.getCandiList();
    }
}
