package com.voteU.election.java.services;

import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.models.Municipality;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.models.Candidate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.LinkedHashMap;

@Service
public class MuniService {
    private final ElectionService electionService;

    public MuniService(ElectionService electionService) {
        this.electionService = electionService;
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
