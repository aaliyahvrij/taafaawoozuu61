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

    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getPoStList_lhMap(String electionIdListString, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, PollingStation>> poStList_list_LhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionList_lhMap.get(electionId).getMuniList_lhMap().get(munId);
            poStList_list_LhMap.put(electionId, muni.getPoStList_lhMap());
        }
        return poStList_list_LhMap;
    }

    public LinkedHashMap<String, List<PollingStation>> getCompactPoStList_lhMap(String electionIdListString, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, List<PollingStation>> compactPoStList_list_lhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionList_lhMap.get(electionId).getMuniList_lhMap().get(munId);
            LinkedHashMap<String, PollingStation> poStList_lhMap = muni.getPoStList_lhMap();
            List<PollingStation> compactPoStList = new ArrayList<>();
            for (PollingStation poSt : poStList_lhMap.values()) {
                compactPoStList.add(new PollingStation(poSt.getId(), poSt.getName(), poSt.getZipCode()));
            }
            compactPoStList_list_lhMap.put(electionId, compactPoStList);
        }
        return compactPoStList_list_lhMap;
    }

    public LinkedHashMap<String, PollingStation> getPoSt(String electionIdListString, String munId, String poStId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, PollingStation> poStList_lhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionList_lhMap.get(electionId).getMuniList_lhMap().get(munId);
            poStList_lhMap.put(electionId, muni.getPoStList_lhMap().get(poStId));
        }
        return poStList_lhMap;
    }

    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getAffiList_lhMap(String electionIdListString, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> affiList_list_lhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionList_lhMap.get(electionId).getMuniList_lhMap().get(munId);
            affiList_list_lhMap.put(electionId, muni.getAffiList_lhMap());
        }
        return affiList_list_lhMap;
    }

    public LinkedHashMap<String, Affiliation> getAffi(String electionIdListString, String munId, Integer affId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, Affiliation> affiList_lhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Affiliation affi = ElectionService.electionList_lhMap.get(electionId).getMuniList_lhMap().get(munId).getAffiList_lhMap().get(affId);
            affiList_lhMap.put(electionId, affi);
        }
        return affiList_lhMap;
    }
}
