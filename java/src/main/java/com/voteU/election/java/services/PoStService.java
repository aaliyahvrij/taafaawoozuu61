package com.voteU.election.java.services;

import com.voteU.election.java.compactDTO.CompactPollingStation;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.models.Municipality;
import com.voteU.election.java.models.PollingStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Slf4j
@Service
public class PoStService {
    private final MuniService muniService;

    public PoStService(MuniService muniService) {
        this.muniService = muniService;
    }

    public LinkedHashMap<String, PollingStation> getMuniLevel_poStList_lhMap(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        Municipality muni = muniService.getConstiLevel_muni(electionId, constId, munId);
        return muni.getPoStList_lhMap();
    }

    public List<CompactPollingStation> getMuniLevel_compactPoStList(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        Municipality muni = muniService.getConstiLevel_muni(electionId, constId, munId);
        LinkedHashMap<String, PollingStation> muniLevel_poStList_lhMap = muni.getPoStList_lhMap();
        List<CompactPollingStation> compactPoStList = new ArrayList<>();
        for (PollingStation muniLevel_poSt : muniLevel_poStList_lhMap.values()) {
            compactPoStList.add(new CompactPollingStation(muniLevel_poSt.getId(), muniLevel_poSt.getName(), muniLevel_poSt.getZipCode()));
        }
        return compactPoStList;
    }

    public PollingStation getMuniLevel_poSt(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        Municipality muni = muniService.getConstiLevel_muni(electionId, constId, munId);
        return muni.getPoStList_lhMap().get(poStId);
    }

    public LinkedHashMap<Integer, Affiliation> getPoStLevel_affiList_lhMap(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        PollingStation poSt = getMuniLevel_poSt(electionId, constId, munId, poStId);
        return poSt.getAffiList_lhMap();
    }
}
