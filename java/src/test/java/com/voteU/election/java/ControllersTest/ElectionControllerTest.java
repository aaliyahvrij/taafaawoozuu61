package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.dtoCompact.CompactElection;
import com.voteU.election.java.controller.electiondata.memory.ElectionController;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.services.electiondata.memory.ElectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ElectionControllerTest {

    @Mock
    private ElectionService electionService;

    @InjectMocks
    private ElectionController electionController;

    private final String electionId = "2023";

    private Election sampleElection;
    private CompactElection sampleCompactElection;
    private Map<String, Election> electionMap;
    private Map<Integer, Party> partyMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleElection = new Election("TK2021", "Tweede Kamer 2021", "2021-03-17");
        sampleElection.setId(electionId);
        sampleElection.setName("General Election 2023");

        sampleCompactElection = new CompactElection("election-1", "Election Name", 1000, 5);
        sampleCompactElection.setStringId(electionId);
        sampleCompactElection.setName("General Election 2023");

        electionMap = new HashMap<>();
        electionMap.put(electionId, sampleElection);

        partyMap = new HashMap<>();
        Party party = new Party(5, "Party 4");
        party.setId(1);
        party.setName("Party A");
        partyMap.put(party.getId(), party);
    }

    @Test
    void testReadResults() {
        when(electionService.readElections()).thenReturn(true);

        boolean result = electionController.readResults();

        assertTrue(result);
        verify(electionService, times(1)).readElections();
    }

    @Test
    void testReadResultsElection() {
        when(electionService.readElection(electionId)).thenReturn(true);

        boolean result = electionController.readResultsElection(electionId);

        assertTrue(result);
        verify(electionService, times(1)).readElection(electionId);
    }

    @Test
    void testGetAllElections() {
        when(electionService.getAll()).thenReturn(electionMap);

        Map<String, Election> result = electionController.getAllElections();

        assertEquals(1, result.size());
        assertTrue(result.containsKey(electionId));
        assertEquals("General Election 2023", result.get(electionId).getName());
        verify(electionService, times(1)).getAll();
    }

    @Test
    void testGetElection() {
        when(electionService.getElection(electionId)).thenReturn(sampleElection);

        Election result = electionController.getElection(electionId);

        assertNotNull(result);
        assertEquals("General Election 2023", result.getName());
        verify(electionService, times(1)).getElection(electionId);
    }

    @Test
    void testGetCompactElection() {
        when(electionService.getCompactElection(electionId)).thenReturn(sampleCompactElection);

        CompactElection result = electionController.getCompactElection(electionId);

        assertNotNull(result);
        assertEquals("General Election 2023", result.getName());
        verify(electionService, times(1)).getCompactElection(electionId);
    }

    @Test
    void testGetAllPartiesByElection() {
        when(electionService.getAllPartiesByElection(electionId)).thenReturn(partyMap);

        Map<Integer, Party> result = electionController.getAllPartiesByElection(electionId);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(1));
        assertEquals("Party A", result.get(1).getName());
        verify(electionService, times(1)).getAllPartiesByElection(electionId);
    }
}
