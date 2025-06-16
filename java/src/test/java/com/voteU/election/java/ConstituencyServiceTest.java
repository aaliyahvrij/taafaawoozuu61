package com.voteU.election.java;

import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.reader.DutchElectionReader;
import com.voteU.election.java.services.ConstituencyService;
import com.voteU.election.java.services.ElectionService;
import com.voteU.election.java.CompactDTO.CompactConstituency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConstituencyServiceTest {

    private ConstituencyService constituencyService;

    @BeforeEach
    void setUp() {
        DutchElectionReader reader = new DutchElectionReader();
        ElectionService electionService = new ElectionService(reader);
        electionService.readElection("TK2021"); // zorg dat TK2021 in je data zit

        constituencyService = new ConstituencyService(electionService);
    }

    @Test
    void testGetConstituenciesByYear() {
        Map<Integer, Constituency> result = constituencyService.getConstituenciesByYear("TK2021");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetConstituencyById() {
        Constituency constituency = constituencyService.getConstituencyById("TK2021", 1); // Pas 1 aan als jouw ID anders is

        assertNotNull(constituency);
        assertEquals(1, constituency.getId()); // pas aan als ID anders is
    }

    @Test
    void testGetPartiesByConstituencyId() {
        Map<Integer, Party> parties = constituencyService.getPartiesByConstituencyId("TK2021", 1); // pas 1 aan als jouw ID anders is

        assertNotNull(parties);
        assertFalse(parties.isEmpty());
    }

    @Test
    void testGetConstituenciesByYearCompact() {
        Map<Integer, CompactConstituency> compact = constituencyService.getConstituenciesByYearCompact("TK2021");

        assertNotNull(compact);
        assertFalse(compact.isEmpty());
    }

    @Test
    void testInvalidYearReturnsNull() {
        assertNull(constituencyService.getConstituenciesByYear("GEENJAAR"));
        assertNull(constituencyService.getConstituencyById("GEENJAAR", 1));
        assertNull(constituencyService.getPartiesByConstituencyId("GEENJAAR", 1));
    }
}
