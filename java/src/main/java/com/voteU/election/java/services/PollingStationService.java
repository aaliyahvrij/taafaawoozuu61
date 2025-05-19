package com.voteU.election.java.services;

import com.voteU.election.java.dto.Compact;
import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.PollingStation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

    public PollingStation getPollingStationById(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
        Authority authority = authorityService.getAuthorityById(electionId,constituencyId,authorityId);
        return authority.getPollingStations().get(pollingStationId);
    }

//    public Compact getPollingStationByIdCompact(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
//        Authority authority = authorityService.getAuthorityById(electionId,constituencyId,authorityId);
//        PollingStation pollingStation =  authority.getPollingStations().get(pollingStationId);
//        String id = pollingStation.getId();
//        String name = pollingStation.getName();
//        String zipCode = pollingStation.getZipCode();
//        int votes = pollingStation.getVotes();
//        //return new Compact()
//    }
}
