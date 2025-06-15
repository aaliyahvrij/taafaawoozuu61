package com.voteU.election.java.services;

import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.models.Candidate;

import java.util.List;
import java.util.LinkedHashMap;

public class AffiService {
    private final MuniService muniService;

    public AffiService(MuniService muniService) {
        this.muniService = muniService;
    }

    public LinkedHashMap<String, List<Candidate>> getAffiLevel_candiList(String electionIdListString, Integer constId, String munId, Integer affId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, List<Candidate>> candiList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Affiliation affi = muniService.getMuniLevel_affi(electionId, constId, munId, affId);
            if (affi == null) {
                throw new ResourceNotFoundException("Affi " + affId + " not found");
            }
            candiList_list_lhMap.put(electionId, affi.getCandiList());
        }
        return candiList_list_lhMap;
    }
}
