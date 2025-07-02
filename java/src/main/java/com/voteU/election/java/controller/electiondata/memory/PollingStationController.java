package com.voteU.election.java.controller.electiondata.memory;

import com.voteU.election.java.dtoCompact.CompactPollingStation;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.PollingStation;
import com.voteU.election.java.services.electiondata.memory.PollingStationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * The PollingStationController class serves as a RestController for handling
 * API requests related to polling stations within a specific election, constituency,
 * and authority. It provides endpoints for retrieving polling station details
 * and associated data.
 */
@RestController
@RequestMapping("/api/election/{electionId}/constituencies/{constituencyId}/authorities/{authorityId}/pollingStations")
public class PollingStationController {
    private final PollingStationService pollingStationService;

    /**
     * Constructor for PollingStationController.
     *
     * @param pollingStationService the service responsible for managing polling station operations
     */
    public PollingStationController(PollingStationService pollingStationService) {
        this.pollingStationService = pollingStationService;
    }

    /**
     * Retrieves a map of polling stations based on the given authority ID.
     *
     * @param electionId the unique identifier for the election
     * @param constituencyId the identifier for the constituency
     * @param authorityId the identifier for the authority managing the polling stations
     * @return a map where the key is the polling station ID and the value is the PollingStation object
     */
    @GetMapping
    public Map<String, PollingStation> getPollingStationsByAuthorityId(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId) {
        return pollingStationService.getPollingStationsByAuthorityId(electionId, constituencyId, authorityId);
    }

    /**
     * Retrieves a list of polling stations in a compact format for the specified authority.
     *
     * @param electionId the ID of the election for which polling stations are retrieved
     * @param constituencyId the ID of the constituency associated with the polling stations
     * @param authorityId the ID of the authority for which polling stations are retrieved
     * @return a list of CompactPollingStation objects matching the specified parameters
     */
    @GetMapping("/compact")
    public List<CompactPollingStation> getPollingStationsByAuthorityIdCompact(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId) {
        return pollingStationService.getPollingStationsByAuthorityIdCompact(electionId, constituencyId, authorityId);
    }

    /**
     * Retrieves a polling station by its unique identifier.
     *
     * @param electionId the ID of the election to which the polling station belongs
     * @param constituencyId the ID of the constituency to which the polling station belongs
     * @param authorityId the ID of the authority managing the polling station
     * @param pollingStationId the unique identifier of the polling station
     * @return the PollingStation corresponding to the given identifiers
     */
    @GetMapping("/{pollingStationId}")
    public PollingStation getPollingStationById(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
       return pollingStationService.getPollingStationById(electionId, constituencyId, authorityId, pollingStationId);
    }

    /**
     * Retrieves a map of parties associated with a given polling station ID.
     *
     * @param electionId the identifier of the election
     * @param constituencyId the identifier of the constituency
     * @param authorityId the identifier of the authority
     * @param pollingStationId the identifier of the polling station
     * @return a map where the key is the party identifier and the value is the Party object
     */
    @GetMapping("/{pollingStationId}/parties")
    public Map<Integer, Party> getPartiesByPollingStationId(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
        return pollingStationService.getPartiesByPollingStationId(electionId, constituencyId, authorityId, pollingStationId);
    }
}
