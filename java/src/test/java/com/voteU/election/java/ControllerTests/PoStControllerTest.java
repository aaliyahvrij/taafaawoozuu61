package com.voteU.election.java.ControllerTests;

import com.voteU.election.java.compactDTO.CompactPollingStation;
import com.voteU.election.java.controllers.PoStController;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.services.PoStService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PollingStationControllerTest {
    @Mock
    private PoStService poStService;

    @InjectMocks
    private PoStController poStController;

    private final String electionId = "2023";
    private final int constId = 1;
    private final String munId = "auth123";
    private final String poStId = "poll456";

    private PollingStation samplePollingStation;
    private CompactPollingStation sampleCompactPollingStation;
    private Map<String, PollingStation> poStMap;
    private Map<Integer, Affiliation> affiMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        samplePollingStation = new PollingStation("PS1", "Station A", "1234AB");
        samplePollingStation.setId(poStId);
        samplePollingStation.setName("Station A");
        sampleCompactPollingStation = new CompactPollingStation("PS1", "Station A", "1234AB");
        sampleCompactPollingStation.setId(poStId);
        sampleCompactPollingStation.setName("Station A");
        poStMap = new HashMap<>();
        poStMap.put(poStId, samplePollingStation);
        affiMap = new HashMap<>();
        Affiliation affi = new Affiliation(10, "Party X", 1447);
        affiMap.put(affi.getId(), affi);
    }

    @Test
    void test_getMuniLevel_pollingStations() {
        when(poStService.getMuniLevel_pollingStationsOf(electionId, constId, munId)).thenReturn(poStMap);
        Map<String, PollingStation> result = poStController.getMuniLevel_pollingStationsOf(electionId, constId, munId);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(poStId));
        assertEquals("Station A", result.get(poStId).getName());
        verify(poStService, times(1)).getMuniLevel_pollingStationsOf(electionId, constId, munId);
    }

    @Test
    void test_getMuniLevel_pollingStations_compact() {
        when(poStService.getMuniLevel_pollingStationsOf_compact(electionId, constId, munId)).thenReturn(List.of(sampleCompactPollingStation));
        List<CompactPollingStation> result = poStController.getMuniLevel_pollingStationsOf_compact(electionId, constId, munId);
        assertEquals(1, result.size());
        assertEquals(poStId, result.getFirst().getId());
        verify(poStService, times(1)).getMuniLevel_pollingStations_compact(electionId, constId, munId);
    }

    @Test
    void test_getPollingStationById() {
        when(poStService.getPollingStationById(electionId, constId, munId, poStId)).thenReturn(samplePollingStation);
        PollingStation result = poStController.getPollingStationById(electionId, constId, munId, poStId);
        assertNotNull(result);
        assertEquals("Station A", result.getName());
        verify(poStService, times(1)).getPollingStationById(electionId, constId, munId, poStId);
    }

    @Test
    void test_getPoStLevel_affiliations() {
        when(poStService.getPoStLevel_affiliationsOf(electionId, constId, munId, poStId)).thenReturn(affiMap);
        Map<Integer, Affiliation> result = poStController.getPoStLevel_affiliationsOf(electionId, constId, munId, poStId);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(10));
        assertEquals("Party X", result.get(10).getName());
        verify(poStService, times(1)).getPoStLevel_affiliationsOf(electionId, constId, munId, poStId);
    }
}
