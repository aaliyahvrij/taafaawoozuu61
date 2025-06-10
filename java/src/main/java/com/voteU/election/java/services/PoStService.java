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
//import java.util.Map;
import java.util.LinkedHashMap;

@Slf4j
@Service
public class PoStService {
    //private final ElectionService electionService;
    private final MuniService muniService;

    /*public PoStService(ElectionService electionService) {
        this.electionService = electionService;
    }*/

    /**
     * Retrieves all the polling station data of a specific election.
     */
    /*public LinkedHashMap<String, PollingStation> getNationalLevel_pollingStationsOf(String electionId) {
        return this.electionService.getElectoralDataOf(electionId).getPollingStations();
    }*/
    public PoStService(MuniService muniService) {
        this.muniService = muniService;
    }

    public LinkedHashMap<String, PollingStation> getMuniLevel_poStListMapOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        Municipality muni = muniService.getMuniById(electionId, constId, munId);
        return muni.getPoStListMap();
    }

    public List<CompactPollingStation> getMuniLevel_compactPoStListOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        Municipality muni = muniService.getMuniById(electionId, constId, munId);
        LinkedHashMap<String, PollingStation> muniLevel_poStListMap = muni.getPoStListMap();
        List<CompactPollingStation> compactPoStList = new ArrayList<>();
        for (PollingStation muniLevel_poSt : muniLevel_poStListMap.values()) {
            compactPoStList.add(new CompactPollingStation(muniLevel_poSt.getId(), muniLevel_poSt.getName(), muniLevel_poSt.getZipCode()));
        }
        return compactPoStList;
    }

    public PollingStation getPoStById(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        Municipality muni = muniService.getMuniById(electionId, constId, munId);
        return muni.getPoStListMap().get(poStId);
    }

    public LinkedHashMap<Integer, Affiliation> getPoStLevel_affiListMapOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        PollingStation poSt = getPoStById(electionId, constId, munId, poStId);
        return poSt.getAffiListMap();
    }
}
