package com.voteU.election.java.services;

import org.springframework.stereotype.Service;
import com.voteU.election.java.models.Election;
import com.voteU.election.java.models.Constituency;
import com.voteU.election.java.models.Municipality;

import java.util.LinkedHashMap;

@Service
public class ConstiService {
    public ConstiService() {
    }

    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getMuniListLhMap(String electionIdListString, int constId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, Municipality>> muniList_listLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Election election = ElectionService.electionListLhMap.get(electionId);
            Constituency consti = election.getConstiListLhMap().get(constId);
            muniList_listLhMap.put(electionId, consti.getMuniListLhMap());
        }
        return muniList_listLhMap;
    }

    public LinkedHashMap<String, LinkedHashMap<String, Municipality>> getCompactMuniListLhMap(String electionIdListString, int constId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, Municipality>> compactMuniList_listLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Election election = ElectionService.electionListLhMap.get(electionId);
            Constituency consti = election.getConstiListLhMap().get(constId);
            LinkedHashMap<String, Municipality> muniListLhMap = consti.getMuniListLhMap();
            LinkedHashMap<String, Municipality> compactMuniListLhMap = new LinkedHashMap<>();
            for (Municipality muni : muniListLhMap.values()) {
                compactMuniListLhMap.put(muni.getId(), new Municipality(muni.getId(), muni.getName()));
            }
            compactMuniList_listLhMap.put(electionId, compactMuniListLhMap);
        }
        return compactMuniList_listLhMap;
    }

    public LinkedHashMap<String, Municipality> getMuni(String electionIdListString, Integer constId, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, Municipality> muniListLhMap = new LinkedHashMap<>();
        for (String electionId : electionIdList) {
            Municipality muni = ElectionService.electionListLhMap.get(electionId).getConstiListLhMap().get(constId).getMuniListLhMap().get(munId);
            muniListLhMap.put(munId, muni);
        }
        return muniListLhMap;
    }
}
