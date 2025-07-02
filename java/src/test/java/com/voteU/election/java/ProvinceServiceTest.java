package com.voteU.election.java;

import com.voteU.election.java.dtoCompact.CompactConstituency;
import com.voteU.election.java.dtoCompact.CompactProvince;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.reader.DutchElectionReader;
import com.voteU.election.java.repositories.electiondata.ProvinceRepository;
import com.voteU.election.java.services.electiondata.memory.ElectionService;
import com.voteU.election.java.services.electiondata.memory.ProvinceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProvinceServiceTest {

    private ProvinceService provinceService;
    private ProvinceRepository provinceRepository;

    @BeforeEach
    void setUp() {
        DutchElectionReader reader = new DutchElectionReader();
        ElectionService electionService = new ElectionService(reader);{
        }
        electionService.readElection("TK2021");

        provinceService = new ProvinceService(electionService, provinceRepository);
    }

    @Test
    void testGetProvinces() {
        List<Province> provinces = provinceService.getProvinces("TK2021");

        assertNotNull(provinces);
        assertFalse(provinces.isEmpty());
    }

    @Test
    void testGetCompactProvinces() {
        List<CompactProvince> provinces = provinceService.getCompactProvinces("TK2021");

        assertNotNull(provinces);
        assertFalse(provinces.isEmpty());
    }

    @Test
    void testGetConstituenciesByProvinceId() {
        List<Constituency> constituencies = provinceService.getConstituenciesByProvinceId("TK2021", 1);

        assertNotNull(constituencies);
        assertFalse(constituencies.isEmpty());
    }

    @Test
    void testGetCompactConstituenciesByProvinceId() {
        List<CompactConstituency> compact = provinceService.getCompactConstituenciesByProvinceId("TK2021", 1);

        assertNotNull(compact);
        assertFalse(compact.isEmpty());
    }

    @Test
    void testGetTotalVotesPerParty() {
        Map<Integer, Party> parties = provinceService.getTotalVotesPerParty("TK2021", 1);

        assertNotNull(parties);
        assertFalse(parties.isEmpty());
    }


    @Test
    void testInvalidDataReturnsEmpty() {
        assertTrue(provinceService.getProvinces("XYZ").isEmpty());
        assertTrue(provinceService.getCompactProvinces("XYZ").isEmpty());
        assertTrue(provinceService.getConstituenciesByProvinceId("XYZ", 1).isEmpty());
        assertTrue(provinceService.getCompactConstituenciesByProvinceId("XYZ", 1).isEmpty());
        assertTrue(provinceService.getTotalVotesPerParty("XYZ", 1).isEmpty());
    }
}
