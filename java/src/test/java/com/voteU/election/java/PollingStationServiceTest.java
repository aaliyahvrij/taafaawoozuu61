package com.voteU.election.java;

import com.voteU.election.java.model.PollingStation;
import com.voteU.election.java.reader.DutchElectionReader;
import com.voteU.election.java.services.AuthorityService;
import com.voteU.election.java.services.ElectionService;
import com.voteU.election.java.services.PollingStationService;
import com.voteU.election.java.CompactDTO.CompactPollingStation;
import com.voteU.election.java.model.Party;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PollingStationServiceTest {

    private PollingStationService pollingStationService;

    @BeforeEach
    void setUp() {
        DutchElectionReader reader = new DutchElectionReader();
        ElectionService electionService = new ElectionService(reader);
        electionService.readElection("TK2021");

        AuthorityService authorityService = new AuthorityService(electionService);
        pollingStationService = new PollingStationService(authorityService);
    }

    @Test
    void testGetPollingStationsByAuthorityId() {
        Map<String, PollingStation> stations = pollingStationService.getPollingStationsByAuthorityId("TK2021", 1, "1");

        assertNotNull(stations);
        assertFalse(stations.isEmpty());
    }

    @Test
    void testGetPollingStationsByAuthorityIdCompact() {
        List<CompactPollingStation> stations = pollingStationService.getPollingStationsByAuthorityIdCompact("TK2021", 1, "1");

        assertNotNull(stations);
        assertFalse(stations.isEmpty());
    }

    @Test
    void testGetPollingStationById() {
        PollingStation station = pollingStationService.getPollingStationById("TK2021", 1, "1", "1");

        assertNotNull(station);
        assertEquals("1", station.getId());
    }

    @Test
    void testGetPartiesByPollingStationId() {
        Map<Integer, Party> parties = pollingStationService.getPartiesByPollingStationId("TK2021", 1, "1", "1");

        assertNotNull(parties);
        assertFalse(parties.isEmpty());
    }
}
