package com.voteU.election.java.services;

import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.models.Candidate;

import java.util.List;
import java.util.LinkedHashMap;

public class AffiService {
    public AffiService() {
    }

    public LinkedHashMap<String, List<Candidate>> getAffiLevel_candiList(String electionIdListString, Integer constId, String munId, Integer affId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, List<Candidate>> candiList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Affiliation muniLevel_affi = ElectionService.electionList_lhMap.get(electionId).getConstiList_lhMap().get(constId).getMuniList_lhMap().get(munId).getAffiList_lhMap().get(affId);
            if (muniLevel_affi == null) {
                throw new ResourceNotFoundException("Affi " + affId + " not found");
            }
            candiList_list_lhMap.put(electionId, muniLevel_affi.getCandiList());
        }
        return candiList_list_lhMap;
    }
}
