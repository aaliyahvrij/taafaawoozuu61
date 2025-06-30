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

    /**
     * Constructs a PollingStationService with the provided AuthorityService.
     *
     * @param authorityService the service used to fetch authority-related data
     */
    public PollingStationService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * Retrieves a map of polling stations based on the specified authority ID within a given election and constituency.
     *
     * @param electionId the unique identifier of the election
     * @param constituencyId the unique identifier of the constituency
     * @param authorityId the unique identifier of the authority
     * @return a map of polling stations where the keys are station identifiers and the values are PollingStation objects
     */
    public Map<String, PollingStation> getPollingStationsByAuthorityId(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId) {
        Authority authority = authorityService.getAuthorityById(electionId,constituencyId,authorityId);
        return authority.getPollingStations();
    }

    /**
     * Retrieves a list of compact polling stations associated with the given election ID, constituency ID, and authority ID.
     *
     * @param electionId the unique identifier of the election
     * @param constituencyId the unique identifier of the constituency
     * @param authorityId the unique identifier of the authority
     * @return a list of compact polling stations, each containing minimal details such as ID, name, and zip code
     */
    public List<CompactPollingStation> getPollingStationsByAuthorityIdCompact(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId) {
        Authority authority = authorityService.getAuthorityById(electionId,constituencyId,authorityId);
        Map<String, PollingStation> pollingStations = authority.getPollingStations();
        List<CompactPollingStation> CompactPollingStations = new ArrayList<>();

        for (PollingStation pollingStation : pollingStations.values()) {
            CompactPollingStations.add(new CompactPollingStation(pollingStation.getId(), pollingStation.getName(), pollingStation.getZipCode()));
        }
        return CompactPollingStations;
    }

    /**
     * Retrieves a polling station by its identifier.
     *
     * @param electionId The identifier of the election to which the polling station belongs.
     * @param constituencyId The identifier of the constituency in which the polling station is located.
     * @param authorityId The identifier of the authority that manages the polling station.
     * @param pollingStationId The unique identifier of the polling station to be retrieved.
     * @return The {@code PollingStation} object corresponding to the specified identifiers.
     */
    public PollingStation getPollingStationById(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
        Authority authority = authorityService.getAuthorityById(electionId,constituencyId,authorityId);
        return authority.getPollingStations().get(pollingStationId);
    }

    /**
     * Retrieves a map of parties associated with a specific polling station.
     *
     * @param electionId the unique identifier for the election
     * @param constituencyId the unique identifier for the constituency
     * @param authorityId the unique identifier for the electoral authority
     * @param pollingStationId the unique identifier for the polling station
     * @return a map where the key is the party ID (Integer) and the value is the Party object
     */
    public Map<Integer, Party> getPartiesByPollingStationId(@PathVariable String electionId, @PathVariable int constituencyId, @PathVariable String authorityId, @PathVariable String pollingStationId) {
        PollingStation pollingStation = getPollingStationById(electionId,constituencyId,authorityId,pollingStationId);
        return pollingStation.getParties();
    }


}
