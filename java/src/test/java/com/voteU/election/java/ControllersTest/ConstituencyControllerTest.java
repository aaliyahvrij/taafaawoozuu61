package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.CompactDTO.CompactConstituency;
import com.voteU.election.java.controller.ConstituencyController;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.services.ConstituencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConstituencyControllerTest {

    @Mock
    private ConstituencyService constituencyService;

    @InjectMocks
    private ConstituencyController constituencyController;

    private final String year = "2023";
    private final Integer constituencyId = 1;

    private Map<Integer, Constituency> constituencies;
    private Map<Integer, CompactConstituency> compactConstituencies;
    private Map<Integer, Party> parties;
    private Constituency sampleConstituency;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleConstituency = new Constituency(8, "Zwolle");
        sampleConstituency.setId(constituencyId);
        sampleConstituency.setName("Sample Constituency");

        constituencies = new HashMap<>();
        constituencies.put(constituencyId, sampleConstituency);

        compactConstituencies = new HashMap<>();
        CompactConstituency compact = new CompactConstituency(4, "Utrecht");
        compact.setId(constituencyId);
        compact.setName("Compact Sample");
        compactConstituencies.put(constituencyId, compact);

        parties = new HashMap<>();
        Party party = new Party(8, "Party Z");
        party.setId(10);
        party.setName("Party X");
        parties.put(party.getId(), party);
    }

    @Test
    void testGetConstituenciesByYear() {
        when(constituencyService.getConstituenciesByYear(year)).thenReturn(constituencies);

        Map<Integer, Constituency> result = constituencyController.getConstituenciesByYear(year);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(constituencyId));
        assertEquals("Sample Constituency", result.get(constituencyId).getName());
        verify(constituencyService, times(1)).getConstituenciesByYear(year);
    }

    @Test
    void testGetConstituenciesByYearCompact() {
        when(constituencyService.getConstituenciesByYearCompact(year)).thenReturn(compactConstituencies);

        Map<Integer, CompactConstituency> result = constituencyController.getConstituenciesByYearCompact(year);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(constituencyId));
        assertEquals("Compact Sample", result.get(constituencyId).getName());
        verify(constituencyService, times(1)).getConstituenciesByYearCompact(year);
    }

    @Test
    void testGetConstituencyById() {
        when(constituencyService.getConstituencyById(year, constituencyId)).thenReturn(sampleConstituency);

        Constituency result = constituencyController.getConstituencyById(year, constituencyId);

        assertNotNull(result);
        assertEquals("Sample Constituency", result.getName());
        verify(constituencyService, times(1)).getConstituencyById(year, constituencyId);
    }

    @Test
    void testGetPartiesByConstituencyId() {
        when(constituencyService.getPartiesByConstituencyId(year, constituencyId)).thenReturn(parties);

        Map<Integer, Party> result = constituencyController.getPartiesByConstituencyId(year, constituencyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(10));
        assertEquals("Party X", result.get(10).getName());
        verify(constituencyService, times(1)).getPartiesByConstituencyId(year, constituencyId);
    }
}
