package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.dtoCompact.CompactPollingStation;
import com.voteU.election.java.controller.electiondata.memory.PollingStationController;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.PollingStation;
import com.voteU.election.java.services.electiondata.memory.PollingStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PollingStationControllerTest {

    @Mock
    private PollingStationService pollingStationService;

    @InjectMocks
    private PollingStationController pollingStationController;

    private final String electionId = "2023";
    private final int constituencyId = 1;
    private final String authorityId = "auth123";
    private final String pollingStationId = "poll456";

    private PollingStation samplePollingStation;
    private CompactPollingStation sampleCompactPollingStation;
    private Map<String, PollingStation> pollingStationMap;
    private Map<Integer, Party> partyMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        samplePollingStation = new PollingStation("PS1", "Station A", "1234AB");
        samplePollingStation.setId(pollingStationId);
        samplePollingStation.setName("Station A");

        sampleCompactPollingStation = new CompactPollingStation("PS1", "Station A", "1234AB");
        sampleCompactPollingStation.setId(pollingStationId);
        sampleCompactPollingStation.setName("Station A");

        pollingStationMap = new HashMap<>();
        pollingStationMap.put(pollingStationId, samplePollingStation);

        partyMap = new HashMap<>();
        Party party = new Party(1, "VVD");
        party.setId(10);
        party.setName("Party X");
        partyMap.put(party.getId(), party);
    }

    @Test
    void testGetPollingStationsByAuthorityId() {
        when(pollingStationService.getPollingStationsByAuthorityId(electionId, constituencyId, authorityId))
                .thenReturn(pollingStationMap);

        Map<String, PollingStation> result = pollingStationController.getPollingStationsByAuthorityId(electionId, constituencyId, authorityId);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(pollingStationId));
        assertEquals("Station A", result.get(pollingStationId).getName());
        verify(pollingStationService, times(1)).getPollingStationsByAuthorityId(electionId, constituencyId, authorityId);
    }

    @Test
    void testGetPollingStationsByAuthorityIdCompact() {
        when(pollingStationService.getPollingStationsByAuthorityIdCompact(electionId, constituencyId, authorityId))
                .thenReturn(List.of(sampleCompactPollingStation));

        List<CompactPollingStation> result = pollingStationController.getPollingStationsByAuthorityIdCompact(electionId, constituencyId, authorityId);

        assertEquals(1, result.size());
        assertEquals(pollingStationId, result.getFirst().getId());
        verify(pollingStationService, times(1)).getPollingStationsByAuthorityIdCompact(electionId, constituencyId, authorityId);
    }

    @Test
    void testGetPollingStationById() {
        when(pollingStationService.getPollingStationById(electionId, constituencyId, authorityId, pollingStationId))
                .thenReturn(samplePollingStation);

        PollingStation result = pollingStationController.getPollingStationById(electionId, constituencyId, authorityId, pollingStationId);

        assertNotNull(result);
        assertEquals("Station A", result.getName());
        verify(pollingStationService, times(1)).getPollingStationById(electionId, constituencyId, authorityId, pollingStationId);
    }

    @Test
    void testGetPartiesByPollingStationId() {
        when(pollingStationService.getPartiesByPollingStationId(electionId, constituencyId, authorityId, pollingStationId))
                .thenReturn(partyMap);

        Map<Integer, Party> result = pollingStationController.getPartiesByPollingStationId(electionId, constituencyId, authorityId, pollingStationId);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(10));
        assertEquals("Party X", result.get(10).getName());
        verify(pollingStationService, times(1)).getPartiesByPollingStationId(electionId, constituencyId, authorityId, pollingStationId);
    }
}
