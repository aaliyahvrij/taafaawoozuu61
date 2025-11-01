package com.voteU.election.java.services;

import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.models.Candidate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.LinkedHashMap;

@Service
public class AffiService {
    public AffiService() {
    }

    public LinkedHashMap<String, List<Candidate>> getCandiList(String electionIdListString, Integer affId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, List<Candidate>> candiList_listLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Affiliation affi = ElectionService.electionListLhMap.get(electionId).getAffiListLhMap().get(affId);
            candiList_listLhMap.put(electionId, affi.getCandiList());
        }
        return candiList_listLhMap;
    }
}
