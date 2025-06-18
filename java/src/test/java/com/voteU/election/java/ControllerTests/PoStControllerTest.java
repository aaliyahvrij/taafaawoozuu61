/*package com.voteU.election.java.ControllerTests;

import com.voteU.election.java.models.PollingStation;
import com.voteU.election.java.models.Affiliation;
import com.voteU.election.java.controllers.PoStController;
import com.voteU.election.java.services.PoStService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class PoStControllerTest {
    @Mock
    private PoStService poStService;
    @InjectMocks
    private PoStController poStController;
    private final String electionId = "tk2023";
    private final String poStId = "poSt456";
    private PollingStation samplePoSt;
    private LinkedHashMap<String, PollingStation> poStListLhMap;
    private LinkedHashMap<Integer, Affiliation> affiListLhMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        samplePoSt = new PollingStation(poStId, "poStA", "1234AB");
        poStListLhMap = new LinkedHashMap<>();
        poStListLhMap.put(poStId, samplePoSt);
        affiListLhMap = new LinkedHashMap<>();
        Affiliation affi = new Affiliation(10, "affiX", 1447);
        affiListLhMap.put(affi.getId(), affi);
    }

    @Test
    void test_getAffiListLhMap() {
        when(poStService.getAffiListLhMap(electionId, poStId)).thenReturn(affiListLhMap);
        LinkedHashMap<String, LinkedHashMap<Integer, Affiliation>> result = poStController.getAffiListLhMap(electionId, poStId);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(10));
        assertEquals("affiX", result.get(10).getName());
        verify(poStService, times(1)).getAffiListLhMap(electionId, poStId);
    }
}*/
