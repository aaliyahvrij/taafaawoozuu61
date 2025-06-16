package com.voteU.election.java.services;

import com.voteU.election.java.models.Municipality;
import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class MuniService {
    public MuniService() {
    }

    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getPoStListLhMap(String electionIdListString, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, PollingStation>> poStListLhMap_listLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionListLhMap.get(electionId).getMuniListLhMap().get(munId);
            poStListLhMap_listLhMap.put(electionId, muni.getPoStListLhMap());
        }
        return poStListLhMap_listLhMap;
    }

    public LinkedHashMap<String, List<PollingStation>> getCompactPoStListLhMap(String electionIdListString, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, List<PollingStation>> compactPoStList_listLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionListLhMap.get(electionId).getMuniListLhMap().get(munId);
            LinkedHashMap<String, PollingStation> poStListLhMap = muni.getPoStListLhMap();
            List<PollingStation> compactPoStList = new ArrayList<>();
            for (PollingStation poSt : poStListLhMap.values()) {
                compactPoStList.add(new PollingStation(poSt.getId(), poSt.getName(), poSt.getZipCode()));
            }
            compactPoStList_listLhMap.put(electionId, compactPoStList);
        }
        return compactPoStList_listLhMap;
    }

    public LinkedHashMap<String, PollingStation> getPoSt(String electionIdListString, String munId, String poStId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, PollingStation> poStListLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionListLhMap.get(electionId).getMuniListLhMap().get(munId);
            poStListLhMap.put(electionId, muni.getPoStListLhMap().get(poStId));
        }
        return poStListLhMap;
    }

    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getAffiListLhMap(String electionIdListString, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> affiListLhMap_listLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionListLhMap.get(electionId).getMuniListLhMap().get(munId);
            affiListLhMap_listLhMap.put(electionId, muni.getAffiListLhMap());
        }
        return affiListLhMap_listLhMap;
    }

    public LinkedHashMap<String, Affiliation> getAffi(String electionIdListString, String munId, Integer affId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, Affiliation> affiListLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Affiliation affi = ElectionService.electionListLhMap.get(electionId).getMuniListLhMap().get(munId).getAffiListLhMap().get(affId);
            affiListLhMap.put(electionId, affi);
        }
        return affiListLhMap;
    }
}
