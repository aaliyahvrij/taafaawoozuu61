package com.voteU.election.java.services;

import com.voteU.election.java.models.Municipality;
import com.voteU.election.java.models.PollingStation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public LinkedHashMap<String, PollingStation> getNationalLevel_pollingStationsOf(String electionId) {
        return this.electionService.getElectoralDataOf(electionId).getPollingStations();
    }

    public PollingStationService(MuniService muniService) {
        this.muniService = muniService;
    }

    public Map<String, PollingStation> getPollingStationsByAuthorityId(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId) {
        Municipality authority = muniService.getAuthorityById(electionId,constituencyId,authorityId);
        return authority.getPollingStations();
    }

    public List<CompactPollingStation> getPollingStationsByAuthorityIdCompact(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId) {
        Authority authority = authorityService.getAuthorityById(electionId,constituencyId,authorityId);
        Map<String, PollingStation> pollingStations = authority.getPollingStations();
        List<CompactPollingStation> CompactPollingStations = new ArrayList<>();

        for (PollingStation pollingStation : pollingStations.values()) {
            CompactPollingStations.add(new CompactPollingStation(pollingStation.getId(), pollingStation.getName(), pollingStation.getZipCode()));
        }
        return CompactPollingStations;
    }

    public PollingStation getPollingStationById(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
        Authority authority = authorityService.getAuthorityById(electionId,constituencyId,authorityId);
        return authority.getPollingStations().get(pollingStationId);
    }

    public Map<Integer, Party> getPartiesByPollingStationId(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
        PollingStation pollingStation = getPollingStationById(electionId,constituencyId,authorityId,pollingStationId);
        return pollingStation.getParties();
    }
}
