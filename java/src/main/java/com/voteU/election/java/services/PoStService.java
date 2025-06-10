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
import java.util.Map;
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

    public Map<String, PollingStation> getMuniLevel_pollingStationsOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        Municipality municipality = muniService.getMunicipalityById(electionId, constId, munId);
        return municipality.getPollingStations();
    }

    public List<CompactPollingStation> getMuniLevel_pollingStationsOf_compact(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId) {
        Municipality municipality = muniService.getMunicipalityById(electionId, constId, munId);
        Map<String, PollingStation> pollingStations = municipality.getPollingStations();
        List<CompactPollingStation> CompactPollingStations = new ArrayList<>();
        for (PollingStation pollingStation : pollingStations.values()) {
            CompactPollingStations.add(new CompactPollingStation(pollingStation.getId(), pollingStation.getName(), pollingStation.getZipCode()));
        }
        return CompactPollingStations;
    }

    public PollingStation getPollingStationById(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        Municipality municipality = muniService.getMunicipalityById(electionId, constId, munId);
        return municipality.getPollingStations().get(poStId);
    }

    public Map<Integer, Affiliation> getPoStLevel_affiliationsOf(@PathVariable String electionId, @PathVariable int constId, @PathVariable String munId, @PathVariable String poStId) {
        PollingStation pollingStation = getPollingStationById(electionId, constId, munId, poStId);
        return pollingStation.getAffiliations();
    }
}
