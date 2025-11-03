package com.voteU.election.java.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;

import java.util.LinkedHashMap;

@Slf4j
@Service
public class PoStService {
    public PoStService() {
    }

    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getAffiListLhMap(String electionIdListString, String poStId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> affiList_listLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            PollingStation poSt = ElectionService.electionListLhMap.get(electionId).getPoStListLhMap().get(poStId);
            affiList_listLhMap.put(electionId, poSt.getAffiListLhMap());
        }
        return affiList_listLhMap;
    }
}
