package com.voteU.election.java.controller;

import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.PollingStation;
import com.voteU.election.java.services.AuthorityService;
import com.voteU.election.java.services.PollingStationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * The PollingStationController class provides API endpoints for managing polling stations
 * within a specific election, constituency, and authority. It serves as a REST controller
 * to handle HTTP requests related to polling stations.
 *
 * Endpoints include:
 * - Retrieving all polling stations for a specific authority.
 * - Retrieving a specific polling station by its ID within an authority.
 */
@RestController
@RequestMapping("/api/election/{electionId}/constituencies/{constituencyId}/authorities/{authorityId}/pollingStations")
public class PollingStationController {
    private final PollingStationService pollingStationService;

    public PollingStationController(PollingStationService pollingStationService) {
        this.pollingStationService = pollingStationService;
    }

    /**
     * Retrieves a map of polling stations associated with the specified authority
     * within a specific election and constituency.
     *
     * @param electionId The identifier of the election to which the polling stations belong.
     * @param constituencyId The identifier of the constituency to which the polling stations belong.
     * @param authorityId The identifier of the authority managing the polling stations.
     * @return A ResponseEntity containing a Map of polling stations, with the polling station ID as the key
     *         and the PollingStation object as the value, or a 404 response if no polling stations are found.
     */
    @GetMapping
    public ResponseEntity<Map<String, PollingStation>> getPollingStationsByAuthorityId(
            @PathVariable String electionId,
            @PathVariable int constituencyId,
            @PathVariable String authorityId) {
        Map<String, PollingStation> pollingStations = pollingStationService.getPollingStationsByAuthorityId(electionId, constituencyId, authorityId);
        if (pollingStations == null || pollingStations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(pollingStations);
    }

    @GetMapping("/{pollingStationId}")
    public PollingStation getPollingStationById(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
       return pollingStationService.getPollingStationById(electionId, constituencyId, authorityId, pollingStationId);
    }
}
