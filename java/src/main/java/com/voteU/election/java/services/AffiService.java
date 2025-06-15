package com.voteU.election.java.services;

import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.models.Candidate;

import java.util.List;
import java.util.LinkedHashMap;

public class AffiService {
    public AffiService() {
    }

    public LinkedHashMap<String, List<Candidate>> getCandiList(String electionIdListString, Integer affId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, List<Candidate>> candiList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Affiliation affi = ElectionService.electionList_lhMap.get(electionId).getAffiList_lhMap().get(affId);
            candiList_list_lhMap.put(electionId, affi.getCandiList());
        }
        return candiList_list_lhMap;
    }
}
