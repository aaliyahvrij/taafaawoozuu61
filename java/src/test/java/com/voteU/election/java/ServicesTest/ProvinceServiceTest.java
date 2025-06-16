package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.CompactDTO.CompactConstituency;
import com.voteU.election.java.CompactDTO.CompactProvince;
import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.model.*;
import com.voteU.election.java.repositories.electiondata.ProvinceRepository;
import com.voteU.election.java.services.ElectionService;
import com.voteU.election.java.services.ProvinceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProvinceServiceTest {

    @Mock
    private ElectionService electionService;

    @Mock
    private ProvinceRepository provinceRepository;

    @InjectMocks
    private ProvinceService provinceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProvincesReturnsProvincesForYear() {
        String year = "2023";
        Election election = new Election("1","Tweede kamer", "2020-04-14");
        Province province = new Province(1, "TestProvince");
        election.setProvinces(List.of(province));

        when(electionService.getElection(year)).thenReturn(election);

        List<Province> result = provinceService.getProvinces(year);
        assertEquals(1, result.size());
        assertEquals("TestProvince", result.getFirst().getName());
    }

    @Test
    void testGetCompactProvincesReturnsCompactList() {
        String year = "2023";
        Province province = new Province(1, "Noord-Holland");
        Election election = new Election("1","Tweede kamer", "2020-04-14");
        election.setProvinces(List.of(province));

        when(electionService.getElection(year)).thenReturn(election);

        List<CompactProvince> result = provinceService.getCompactProvinces(year);
        assertEquals(1, result.size());
        assertEquals("Noord-Holland", result.getFirst().getName());
    }

    @Test
    void testGetCompactConstituenciesByProvinceId() {
        String year = "2023";
        Constituency c1 = new Constituency(10, "District A");
        Province province = new Province(1, "Utrecht");
        province.setConstituencies(List.of(c1));
        Election election = new Election("1","Tweede kamer", "2020-04-14");
        election.setProvinces(List.of(province));

        when(electionService.getElection(year)).thenReturn(election);

        List<CompactConstituency> result = provinceService.getCompactConstituenciesByProvinceId(year, 1);
        assertEquals(1, result.size());
        assertEquals("District A", result.getFirst().getName());
    }

    @Test
    void testGetTotalVotesForProvince() {
        String year = "2023";
        Party party = new Party(1, "Partij A");
        party.setVotes(123);
        Authority authority = new Authority("1", "Authority");
        authority.setParties(Map.of(1, party));
        Constituency constituency = new Constituency(1, "TestConst");
        constituency.setAuthorities(Map.of(String.valueOf(1), authority));
        Province province = new Province(1, "TestProv");
        province.setConstituencies(List.of(constituency));
        Election election = new Election("1","Tweede kamer", "2020-04-14");
        election.setProvinces(List.of(province));

        when(electionService.getElection(year)).thenReturn(election);

        int totalVotes = provinceService.getTotalVotesForProvince(year, 1);
        assertEquals(123, totalVotes);
    }

    @Test
    void testGetAllProvinceNames() {
        String year = "2023";
        List<DropdownOptionDTO<Integer>> expectedList = List.of(new DropdownOptionDTO<>(1, "Friesland"));

        when(provinceRepository.getProvincesByElectionId(year)).thenReturn(expectedList);

        List<DropdownOptionDTO<Integer>> result = provinceService.getAllProvinceNames(year);
        assertEquals(1, result.size());
        assertEquals("Friesland", result.getFirst().getName());
    }
}
