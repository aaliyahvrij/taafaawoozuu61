package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.dtoCompact.CompactConstituency;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.services.electiondata.memory.ConstituencyService;
import com.voteU.election.java.services.electiondata.memory.ElectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConstituencyServiceTest {

    @Mock
    private ElectionService electionService;

    @InjectMocks
    private ConstituencyService constituencyService;

    private Election mockElection;
    private Constituency mockConstituency;

    @BeforeEach
    void setup() {
        mockConstituency = new Constituency(1, "Constituency A");

        Map<Integer, Constituency> constituencyMap = new HashMap<>();
        constituencyMap.put(1, mockConstituency);

        mockElection = mock(Election.class);
        when(mockElection.getConstituencies()).thenReturn(constituencyMap);
    }

    @Test
    void testGetConstituenciesByYear() {
        when(electionService.getElection("2021")).thenReturn(mockElection);

        Map<Integer, Constituency> result = constituencyService.getConstituenciesByYear("2021");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Constituency A", result.get(1).getName());
    }

    @Test
    void testGetConstituenciesByYearCompact() {
        when(electionService.getElection("2021")).thenReturn(mockElection);

        Map<Integer, CompactConstituency> result = constituencyService.getConstituenciesByYearCompact("2021");

        assertNotNull(result);
        assertEquals(1, result.size());
        CompactConstituency compact = result.get(1);
        assertEquals(1, compact.getId());
        assertEquals("Constituency A", compact.getName());
    }

    @Test
    void testGetConstituencyById() {
        when(electionService.getElection("2021")).thenReturn(mockElection);

        Constituency result = constituencyService.getConstituencyById("2021", 1);

        assertNotNull(result);
        assertEquals("Constituency A", result.getName());
    }

    @Test
    void testGetPartiesByConstituencyId() {
        Map<Integer, Party> partyMap = Map.of(1, new Party(1, "Party A"));
        mockConstituency.setParties(partyMap);

        Map<Integer, Constituency> constituencyMap = Map.of(1, mockConstituency);
        when(mockElection.getConstituencies()).thenReturn(constituencyMap);
        when(electionService.getElection("2021")).thenReturn(mockElection);

        Map<Integer, Party> result = constituencyService.getPartiesByConstituencyId("2021", 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Party A", result.get(1).getName());
    }
}
