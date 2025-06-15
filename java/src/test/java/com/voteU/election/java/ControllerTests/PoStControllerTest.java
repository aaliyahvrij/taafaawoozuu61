package com.voteU.election.java.ControllerTests;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.controllers.PoStController;
import com.voteU.election.java.services.PoStService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PoStControllerTest {
    @Mock
    private PoStService poStService;
    @InjectMocks
    private PoStController poStController;
    private final String electionId = "tk2023";
    private final String poStId = "poSt456";
    private PollingStation samplePoSt;
    private LinkedHashMap<String, PollingStation> poStList_lhMap;
    private LinkedHashMap<Integer, Affiliation> affiList_lhMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        samplePoSt = new PollingStation(poStId, "poStA", "1234AB");
        poStList_lhMap = new LinkedHashMap<>();
        poStList_lhMap.put(poStId, samplePoSt);
        affiList_lhMap = new LinkedHashMap<>();
        Affiliation affi = new Affiliation(10, "affiX", 1447);
        affiList_lhMap.put(affi.getId(), affi);
    }

    @Test
    void test_getAffiList_lhMap() {
        when(poStService.getAffiList_lhMap(electionId, poStId)).thenReturn(affiList_lhMap);
        LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> result = poStController.getAffiList_lhMap(electionId, poStId);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(10));
        assertEquals("affiX", result.get(10).getName());
        verify(poStService, times(1)).getAffiList_lhMap(electionId, poStId);
    }
}
