package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.dtoCompact.CompactConstituency;
import com.voteU.election.java.dtoCompact.CompactProvince;
import com.voteU.election.java.controller.electiondata.memory.ProvinceController;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.services.electiondata.memory.ProvinceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProvinceControllerTest {

    @Mock
    private ProvinceService provinceService;

    @InjectMocks
    private ProvinceController provinceController;

    private final String year = "2023";

    private Province province;
    private CompactProvince compactProvince;
    private Constituency constituency;
    private CompactConstituency compactConstituency;
    private Party party;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        province = new Province();
        province.setId(1);
        province.setName("Test Province");

        compactProvince = new CompactProvince(5, "Groningen");
        compactProvince.setId(1);
        compactProvince.setName("Compact Province");

        constituency = new Constituency(3, "Utrecht");
        constituency.setId(10);
        constituency.setName("Test Constituency");

        compactConstituency = new CompactConstituency(9,"Amsterdam");
        compactConstituency.setId(10);
        compactConstituency.setName("Compact Constituency");

        party = new Party(8, "VDD");
        party.setId(100);
        party.setName("Test Party");
    }

    @Test
    void testGetProvinces() {
        List<Province> provinces = Collections.singletonList(province);
        when(provinceService.getProvinces(year)).thenReturn(provinces);

        List<Province> result = provinceController.getProvinces(year);

        assertEquals(1, result.size());
        assertEquals("Test Province", result.get(0).getName());
    }

    @Test
    void testGetCompactProvinces() {
        List<CompactProvince> compactProvinces = Collections.singletonList(compactProvince);
        when(provinceService.getCompactProvinces(year)).thenReturn(compactProvinces);

        List<CompactProvince> result = provinceController.getCompactProvinces(year);

        assertEquals(1, result.size());
        assertEquals("Compact Province", result.getFirst().getName());
    }

    @Test
    void testGetConstituenciesByProvinceId() {
        List<Constituency> constituencies = Collections.singletonList(constituency);
        when(provinceService.getConstituenciesByProvinceId(year, 1)).thenReturn(constituencies);

        List<Constituency> result = provinceController.getConstituenciesByProvinceId(year, 1);

        assertEquals(1, result.size());
        assertEquals("Test Constituency", result.get(0).getName());
    }

    @Test
    void testGetCompactConstituenciesByProvinceId() {
        List<CompactConstituency> compactConstituencies = Collections.singletonList(compactConstituency);
        when(provinceService.getCompactConstituenciesByProvinceId(year, 1)).thenReturn(compactConstituencies);

        List<CompactConstituency> result = provinceController.getCompactConstituenciesByProvinceId(year, 1);

        assertEquals(1, result.size());
        assertEquals("Compact Constituency", result.get(0).getName());
    }

    @Test
    void testGetTotalVotesPerParty() {
        Map<Integer, Party> votesPerParty = new HashMap<>();
        votesPerParty.put(party.getId(), party);
        when(provinceService.getTotalVotesPerParty(year, 1)).thenReturn(votesPerParty);

        Map<Integer, Party> result = provinceController.getTotalVotesPerParty(year, 1);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(party.getId()));
        assertEquals("Test Party", result.get(party.getId()).getName());
    }
}
