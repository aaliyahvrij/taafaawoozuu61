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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ProvinceService}.
 */
@ExtendWith(MockitoExtension.class)
class ProvinceServiceTest {

    @Mock
    private ElectionService electionService;

    @Mock
    private ProvinceRepository provinceRepository;

    private ProvinceService provinceService;

    @BeforeEach
    void setUp() {
        provinceService = new ProvinceService(electionService, provinceRepository);
    }

    // ---------- helpers -----------------------------------------------------

    private Province buildProvince(int id, String name, Party... parties) {
        Authority authority = new Authority(String.valueOf(1), "Authority-1");
        for (Party p : parties) {
            authority.getParties().put(p.getId(), p);
        }

        Constituency constituency = new Constituency(10, "Constituency-1");
        constituency.getAuthorities().put(authority.getId(), authority);

        Province province = new Province(id, name);
        province.getConstituencies().add(constituency);
        return province;
    }

    private Election buildElection2021() {
        Party p1 = new Party(111, "Alpha"); p1.setVotes(1_000);
        Party p2 = new Party(222, "Beta");  p2.setVotes(2_000);

        Province groningen = buildProvince(1, "Groningen", p1, p2);
        Election election = new Election("TK2021", "Tweede Kamer 2021", "2021-03-17");
        election.getProvinces().add(groningen);
        return election;
    }

    // ---------- getProvinces() ---------------------------------------------

    @Test
    void getProvinces_returns_list_when_election_found() {
        Election election = buildElection2021();
        when(electionService.getElection("TK2021")).thenReturn(election);

        List<Province> result = provinceService.getProvinces("TK2021");

        assertEquals(1, result.size());
        assertEquals("Groningen", result.getFirst().getName());
    }

    @Test
    void getProvinces_returns_empty_when_election_not_found() {
        when(electionService.getElection("TK404")).thenReturn(null);

        List<Province> result = provinceService.getProvinces("TK404");

        assertTrue(result.isEmpty());
    }

    // ---------- getCompactProvinces() --------------------------------------

    @Test
    void getCompactProvinces_maps_to_dto() {
        when(electionService.getElection("TK2021")).thenReturn(buildElection2021());

        List<CompactProvince> result = provinceService.getCompactProvinces("TK2021");

        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().getId());
        assertEquals("Groningen", result.getFirst().getName());
    }

    // ---------- getConstituenciesByProvinceId() ----------------------------

    @Test
    void getConstituenciesByProvinceId_returns_correct_list() {
        when(electionService.getElection("TK2021")).thenReturn(buildElection2021());

        List<Constituency> result = provinceService.getConstituenciesByProvinceId("TK2021", 1);

        assertEquals(1, result.size());
        assertEquals("Constituency-1", result.getFirst().getName());
    }

    @Test
    void getConstituenciesByProvinceId_returns_empty_when_province_missing() {
        when(electionService.getElection("TK2021")).thenReturn(buildElection2021());

        List<Constituency> result = provinceService.getConstituenciesByProvinceId("TK2021", 999);

        assertTrue(result.isEmpty());
    }

    // ---------- getCompactConstituenciesByProvinceId() ---------------------

    @Test
    void getCompactConstituenciesByProvinceId_maps_to_dto() {
        when(electionService.getElection("TK2021")).thenReturn(buildElection2021());

        List<CompactConstituency> result =
                provinceService.getCompactConstituenciesByProvinceId("TK2021", 1);

        assertEquals(1, result.size());
        assertEquals(10, result.getFirst().getId());
        assertEquals("Constituency-1", result.getFirst().getName());
    }

    // ---------- getTotalVotesPerParty(Province) ----------------------------

    @Test
    void getTotalVotesPerParty_calculates_votes_and_percentages() {
        Province province = buildProvince(
                1,
                "Groningen",
                new Party(1, "Alpha") {{ setVotes(1_000); }},
                new Party(2, "Beta")  {{ setVotes(3_000); }}
        );

        Map<Integer, Party> result = provinceService.getTotalVotesPerParty(province);

        assertEquals(2, result.size());
        assertEquals(1_000, result.get(1).getVotes());
        assertEquals(3_000, result.get(2).getVotes());

        double pctAlpha = result.get(1).getPercentage();
        double pctBeta  = result.get(2).getPercentage();
        assertEquals(25.0, pctAlpha, 0.0001);
        assertEquals(75.0, pctBeta,  0.0001);
    }

    // ---------- getTotalVotesPerParty(year, provinceId) --------------------

    @Test
    void getTotalVotesPerParty_with_year_and_id_delegates_correctly() {
        Election election = buildElection2021();
        when(electionService.getElection("TK2021")).thenReturn(election);

        Map<Integer, Party> result =
                provinceService.getTotalVotesPerParty("TK2021", 1);

        assertEquals(2, result.size());
        verify(electionService).getElection("TK2021");
    }

    @Test
    void getTotalVotesPerParty_returns_empty_map_when_province_not_found() {
        when(electionService.getElection("TK2021")).thenReturn(buildElection2021());

        Map<Integer, Party> result = provinceService.getTotalVotesPerParty("TK2021", 99);

        assertTrue(result.isEmpty());
    }

    // ---------- getAllProvinceNames() --------------------------------------

    @Test
    void getAllProvinceNames_returns_repository_result() {
        List<DropdownOptionDTO<Integer>> mockList = List.of(
                new DropdownOptionDTO<>(1, "Groningen"),
                new DropdownOptionDTO<>(2, "Friesland")
        );
        when(provinceRepository.getProvincesByElectionId("TK2021")).thenReturn(mockList);

        List<DropdownOptionDTO<Integer>> result = provinceService.getAllProvinceNames("TK2021");

        assertEquals(mockList, result);
        verify(provinceRepository).getProvincesByElectionId("TK2021");
    }
}
