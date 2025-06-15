package com.voteU.election.java.services;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Slf4j
@Service
public class PoStService {
    public PoStService() {
    }

    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getPoStLevel_affiList_lhMap(String electionIdListString, String poStId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> affiList_list_lhMap = null;
        for (String electionId : electionIdList) {
            PollingStation muniLevel_poSt = ElectionService.electionList_lhMap.get(electionId).getPoStList_lhMap().get(poStId);
            affiList_list_lhMap.put(electionId, muniLevel_poSt.getAffiList_lhMap());
        }
        return affiList_list_lhMap;
    }
}
