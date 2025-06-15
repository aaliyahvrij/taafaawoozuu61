package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.CompactDTO.CompactProvince;
import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.model.*;
import com.voteU.election.java.repositories.electiondata.ProvinceRepository;
import com.voteU.election.java.services.ElectionService;
import com.voteU.election.java.services.ProvinceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProvinceServiceTest {

    private ElectionService electionService;
    private ProvinceRepository provinceRepository;
    private ProvinceService provinceService;

    @BeforeEach
    void setUp() {
        electionService = mock(ElectionService.class);
        provinceRepository = mock(ProvinceRepository.class);
        provinceService = new ProvinceService(electionService, provinceRepository);
    }

    @Test
    void testGetProvinces_returnsCorrectProvinces() {
        String year = "2023";
        List<Province> provinces = List.of(new Province(1, "Noord-Holland"), new Province(2, "Zuid-Holland"));
        Election election = new Election("1", "tweede kwamer", "2023-07-17");
        election.setProvinces(provinces);

        when(electionService.getElection(year)).thenReturn(election);

        List<Province> result = provinceService.getProvinces(year);
        assertEquals(2, result.size());
        assertEquals("Noord-Holland", result.get(0).getName());
    }

    @Test
    void testGetCompactProvinces_returnsCorrectCompactData() {
        String year = "2023";
        List<Province> provinces = List.of(new Province(1, "Utrecht"), new Province(2, "Groningen"));
        Election election = new Election("1", "tweede kwamer", "2023-07-17");
        election.setProvinces(provinces);

        when(electionService.getElection(year)).thenReturn(election);

        List<CompactProvince> result = provinceService.getCompactProvinces(year);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Utrecht", result.get(0).getName());
    }

    @Test
    void testGetTotalVotesPerParty_returnsCorrectTotalsAndPercentages() {
        // Setup test data
        Party party1 = new Party(1, "Party A");
        party1.setVotes(100);

        Party party2 = new Party(2, "Party B");
        party2.setVotes(200);

        Map<Integer, Party> authorityParties = new HashMap<>();
        authorityParties.put(party1.getId(), party1);
        authorityParties.put(party2.getId(), party2);

        Authority authority = new Authority("1");
        authority.setParties(authorityParties);


        Map<String, Authority> authorities = new HashMap<>();
        authorities.put("1", authority);

        Constituency constituency = new Constituency(5, "Constituency A");
        constituency.setAuthorities(authorities);

        var province = new Province();
        province.setConstituencies(List.of(constituency));

        // Act
        Map<Integer, Party> result = provinceService.getTotalVotesPerParty(province);

        // Assert
        assertEquals(2, result.size());
        assertEquals(100, result.get(1).getVotes());
        assertEquals(200, result.get(2).getVotes());
        assertEquals(33.3333, result.get(1).getPercentage(), 0.1);
        assertEquals(66.6666, result.get(2).getPercentage(), 0.1);
    }

    @Test
    void testGetAllProvinceNames_callsRepository() {
        String year = "2021";
        List<DropdownOptionDTO<Integer>> mockList = List.of(
                new DropdownOptionDTO<>(1, "Drenthe"),
                new DropdownOptionDTO<>(2, "Flevoland")
        );

        when(provinceRepository.getProvincesByElectionId(year)).thenReturn(mockList);

        List<DropdownOptionDTO<Integer>> result = provinceService.getAllProvinceNames(year);
        assertEquals(2, result.size());
        assertEquals("Drenthe", result.getFirst().getName());
    }
}
