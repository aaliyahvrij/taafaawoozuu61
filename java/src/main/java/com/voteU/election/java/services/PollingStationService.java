package com.voteU.election.java.services;

import com.voteU.election.java.CompactDTO.CompactPollingStation;
import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.PollingStation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PollingStationService {
    private final AuthorityService authorityService;

    public PollingStationService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    public Map<String, PollingStation> getPollingStationsByAuthorityId(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId) {
        Authority authority = authorityService.getAuthorityById(electionId,constituencyId,authorityId);
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
