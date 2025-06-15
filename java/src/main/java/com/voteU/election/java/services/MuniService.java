package com.voteU.election.java.services;

import com.voteU.election.java.compactDTO.CompactPollingStation;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.models.Municipality;
import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class MuniService {
    private final ConstiService constiService;

    public MuniService(ConstiService constiService) {
        this.constiService = constiService;
    }

    public LinkedHashMap<String, LinkedHashMap<String, PollingStation>> getMuniLevel_poStList_lhMap(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<String, PollingStation>> poStList_list_LhMap = null;
        for (String electionId : electionIdList) {
            Municipality constiLevel_muni = ElectionService.electionList_lhMap.get(electionId).getConstiList_lhMap().get(constId).getMuniList_lhMap().get(munId);
            poStList_list_LhMap.put(electionId, constiLevel_muni.getPoStList_lhMap());
        }
        return poStList_list_LhMap;
    }

    public LinkedHashMap<String, List<CompactPollingStation>> getMuniLevel_compactPoStList(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, List<CompactPollingStation>> compactPoStList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Municipality constiLevel_muni = ElectionService.electionList_lhMap.get(electionId).getConstiList_lhMap().get(constId).getMuniList_lhMap().get(munId);
            LinkedHashMap<String, PollingStation> muniLevel_poStList_lhMap = constiLevel_muni.getPoStList_lhMap();
            List<CompactPollingStation> compactPoStList = new ArrayList<>();
            for (PollingStation muniLevel_poSt : muniLevel_poStList_lhMap.values()) {
                compactPoStList.add(new CompactPollingStation(muniLevel_poSt.getId(), muniLevel_poSt.getName(), muniLevel_poSt.getZipCode()));
            }
            compactPoStList_list_lhMap.put(electionId, compactPoStList);
        }
        return compactPoStList_list_lhMap;
    }

    public LinkedHashMap<String, PollingStation> getMuniLevel_poSt(@PathVariable String electionIdListString, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, PollingStation> poStList_lhMap = null;
        for (String electionId : electionIdList) {
            Municipality constiLevel_muni = ElectionService.electionList_lhMap.get(electionId).getConstiList_lhMap().get(constId).getMuniList_lhMap().get(munId);
            poStList_lhMap.put(electionId, constiLevel_muni.getPoStList_lhMap().get(poStId));
        }
        return poStList_lhMap;
    }

    public LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> getMuniLevel_affiList_lhMap(String electionIdListString, Integer constId, String munId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> affiList_list_lhMap = null;
        for (String electionId : electionIdList) {
            Municipality constiLevel_muni = ElectionService.electionList_lhMap.get(electionId).getConstiList_lhMap().get(constId).getMuniList_lhMap().get(munId);
            if (constiLevel_muni == null) {
                throw new ResourceNotFoundException("Muni " + munId + " not found");
            }
            affiList_list_lhMap.put(electionId, constiLevel_muni.getAffiList_lhMap());
        }
        return affiList_list_lhMap;
    }

    public LinkedHashMap<String, Affiliation> getMuniLevel_affi(String electionIdListString, Integer constId, String munId, Integer affId) {
        String[] electionIdList = electionIdListString.split("-");
        LinkedHashMap<String, Affiliation> affiList_lhMap = null;
        for (String electionId : electionIdList) {
            LinkedHashMap<Integer, Affiliation> muniLevel_affiList_lhMap = ElectionService.electionList_lhMap.get(electionId).getConstiList_lhMap().get(constId).getMuniList_lhMap().get(munId).getAffiList_lhMap();
            Affiliation muniLevel_affi = muniLevel_affiList_lhMap.get(affId);
            if (muniLevel_affi == null) {
                throw new ResourceNotFoundException("Affi " + affId + " not found");
            }
            affiList_lhMap.put(electionId, muniLevel_affi);
        }
        return affiList_lhMap;
    }
}
