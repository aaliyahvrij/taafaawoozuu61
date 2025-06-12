package com.voteU.election.java.ControllerTests;

import com.voteU.election.java.compactDTO.CompactPollingStation;
import com.voteU.election.java.controllers.PoStController;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.services.PoStService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PoStControllerTest {
    @Mock
    private PoStService poStService;
    @InjectMocks
    private PoStController poStController;
    private final String electionId = "tk2023";
    private final int constId = 1;
    private final String munId = "muni123";
    private final String poStId = "poSt456";
    private PollingStation samplePoSt;
    private CompactPollingStation sampleCompactPoSt;
    private LinkedHashMap<String, PollingStation> poStListMap;
    private LinkedHashMap<Integer, Affiliation> affiListMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        samplePoSt = new PollingStation(poStId, "poStA", "1234AB");
        sampleCompactPoSt = new CompactPollingStation(poStId, "poStA", "1234AB");
        poStListMap = new LinkedHashMap<>();
        poStListMap.put(poStId, samplePoSt);
        affiListMap = new LinkedHashMap<>();
        Affiliation affi = new Affiliation(10, "affiX", 1447);
        affiListMap.put(affi.getId(), affi);
    }

    @Test
    void test_getMuniLevel_poStListMap() {
        when(poStService.getMuniLevel_poStListMap(electionId, constId, munId)).thenReturn(poStListMap);
        Map<String, PollingStation> result = poStController.getMuniLevel_poStListMap(electionId, constId, munId);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(poStId));
        assertEquals("poStA", result.get(poStId).getName());
        verify(poStService, times(1)).getMuniLevel_poStListMap(electionId, constId, munId);
    }

    @Test
    void test_getMuniLevel_compactPoStList() {
        when(poStService.getMuniLevel_compactPoStList(electionId, constId, munId)).thenReturn(List.of(sampleCompactPoSt));
        List<CompactPollingStation> result = poStController.getMuniLevel_compactPoStList(electionId, constId, munId);
        assertEquals(1, result.size());
        assertEquals(poStId, result.getFirst().getId());
        verify(poStService, times(1)).getMuniLevel_compactPoStList(electionId, constId, munId);
    }

    @Test
    void test_getMuniLevel_poSt() {
        when(poStService.getMuniLevel_poSt(electionId, constId, munId, poStId)).thenReturn(samplePoSt);
        PollingStation result = poStController.getMuniLevel_poSt(electionId, constId, munId, poStId);
        assertNotNull(result);
        assertEquals("poStA", result.getName());
        verify(poStService, times(1)).getMuniLevel_poSt(electionId, constId, munId, poStId);
    }

    @Test
    void test_getPoStLevel_affiListMap() {
        when(poStService.getPoStLevel_affiListMap(electionId, constId, munId, poStId)).thenReturn(affiListMap);
        Map<Integer, Affiliation> result = poStController.getPoStLevel_affiListMap(electionId, constId, munId, poStId);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(10));
        assertEquals("affiX", result.get(10).getName());
        verify(poStService, times(1)).getPoStLevel_affiListMap(electionId, constId, munId, poStId);
    }
}
