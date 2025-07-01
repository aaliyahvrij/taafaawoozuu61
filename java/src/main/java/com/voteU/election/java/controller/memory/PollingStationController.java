package com.voteU.election.java.controller.memory;

import com.voteU.election.java.CompactDTO.CompactPollingStation;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.PollingStation;
import com.voteU.election.java.services.PollingStationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/election/{electionId}/constituencies/{constituencyId}/authorities/{authorityId}/pollingStations")
public class PollingStationController {
    private final PollingStationService pollingStationService;

    public PollingStationController(PollingStationService pollingStationService) {
        this.pollingStationService = pollingStationService;
    }

    @GetMapping
    public Map<String, PollingStation> getPollingStationsByAuthorityId(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId) {
        return pollingStationService.getPollingStationsByAuthorityId(electionId, constituencyId, authorityId);
    }

    @GetMapping("/compact")
    public List<CompactPollingStation> getPollingStationsByAuthorityIdCompact(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId) {
        return pollingStationService.getPollingStationsByAuthorityIdCompact(electionId, constituencyId, authorityId);
    }

    @GetMapping("/{pollingStationId}")
    public PollingStation getPollingStationById(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
       return pollingStationService.getPollingStationById(electionId, constituencyId, authorityId, pollingStationId);
    }

    @GetMapping("/{pollingStationId}/parties")
    public Map<Integer, Party> getPartiesByPollingStationId(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
        return pollingStationService.getPartiesByPollingStationId(electionId, constituencyId, authorityId, pollingStationId);
    }
}
