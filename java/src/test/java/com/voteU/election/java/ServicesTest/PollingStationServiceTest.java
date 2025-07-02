package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.dtoCompact.CompactPollingStation;
import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.PollingStation;
import com.voteU.election.java.services.electiondata.memory.AuthorityService;
import com.voteU.election.java.services.electiondata.memory.PollingStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class PollingStationServiceTest {

    @Mock
    private AuthorityService authorityService;

    @InjectMocks
    private PollingStationService pollingStationService;

    private Authority mockAuthority;
    private PollingStation mockStation;

    @BeforeEach
    void setup() {
        mockStation = new PollingStation("PS1", "Station A", "1234AB");
        Map<String, PollingStation> stationMap = new HashMap<>();
        stationMap.put("PS1", mockStation);

        mockAuthority = mock(Authority.class);
        when(mockAuthority.getPollingStations()).thenReturn(stationMap);
    }

    @Test
    void testGetPollingStationsByAuthorityId() {
        when(authorityService.getAuthorityById("TK2021", 1, "AUTH1")).thenReturn(mockAuthority);

        Map<String, PollingStation> result = pollingStationService.getPollingStationsByAuthorityId("TK2021", 1, "AUTH1");

        assertEquals(1, result.size());
        assertEquals("Station A", result.get("PS1").getName());
    }

    @Test
    void testGetPollingStationsByAuthorityIdCompact() {
        when(authorityService.getAuthorityById("TK2021", 1, "AUTH1")).thenReturn(mockAuthority);

        List<CompactPollingStation> result = pollingStationService.getPollingStationsByAuthorityIdCompact("TK2021", 1, "AUTH1");

        assertEquals(1, result.size());
        assertEquals("PS1", result.getFirst().getId());
        assertEquals("Station A", result.getFirst().getName());
        assertEquals("1234AB", result.getFirst().getZipCode());
    }

    @Test
    void testGetPollingStationById() {
        when(authorityService.getAuthorityById("TK2021", 1, "AUTH1")).thenReturn(mockAuthority);

        PollingStation result = pollingStationService.getPollingStationById("TK2021", 1, "AUTH1", "PS1");

        assertNotNull(result);
        assertEquals("Station A", result.getName());
    }

    @Test
    void testGetPartiesByPollingStationId() {
        Map<Integer, Party> partyMap = Map.of(1, new Party(1, "Party A"));
        mockStation.setParties(partyMap);

        Map<String, PollingStation> stationMap = Map.of("PS1", mockStation);
        when(mockAuthority.getPollingStations()).thenReturn(stationMap);
        when(authorityService.getAuthorityById("TK2021", 1, "AUTH1")).thenReturn(mockAuthority);

        Map<Integer, Party> result = pollingStationService.getPartiesByPollingStationId("TK2021", 1, "AUTH1", "PS1");

        assertEquals(1, result.size());
        assertTrue(result.containsKey(1));
        assertEquals("Party A", result.get(1).getName());
    }
}
