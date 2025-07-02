package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.model.*;
import com.voteU.election.java.services.electiondata.memory.AuthorityService;
import com.voteU.election.java.services.electiondata.memory.ElectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorityServiceTest {

    @Mock
    private ElectionService electionService;

    @InjectMocks
    private AuthorityService authorityService;

    private Election mockElection;
    private Constituency mockConstituency;
    private Party mockParty;

    @BeforeEach
    void setup() {
        Authority mockAuthority = new Authority("A1", "Authority A");
        mockParty = new Party(1, "Party A");

        mockAuthority.setParties(Map.of(1, mockParty));

        Constituency constituency = new Constituency(10, "Constituency X");
        constituency.setAuthorities(Map.of("A1", mockAuthority));

        mockElection = mock(Election.class);
        when(mockElection.getConstituencies()).thenReturn(Map.of(10, constituency));
    }

    @Test
    void testGetAuthoritiesByConstituencyId() {
        when(electionService.getElection("2021")).thenReturn(mockElection);

        Map<String, Authority> result = authorityService.getAuthoritiesByConstituencyId("2021", 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Authority A", result.get("A1").getName());
    }

    @Test
    void testGetAuthoritiesByConstituencyIdCompact() {
        when(electionService.getElection("2021")).thenReturn(mockElection);

        Map<String, Authority> result = authorityService.getAuthoritiesByConstituencyIdCompact("2021", 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Authority A", result.get("A1").getName());
    }

    @Test
    void testGetAuthorityById() {
        when(electionService.getElection("2021")).thenReturn(mockElection);

        Authority result = authorityService.getAuthorityById("2021", 10, "A1");

        assertNotNull(result);
        assertEquals("Authority A", result.getName());
    }

    @Test
    void testGetPartiesByAuthorityId() {
        when(electionService.getElection("2021")).thenReturn(mockElection);

        Map<Integer, Party> result = authorityService.getPartiesByAuthorityId("2021", 10, "A1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Party A", result.get(1).getName());
    }

    @Test
    void testGetPartyById() {
        when(electionService.getElection("2021")).thenReturn(mockElection);

        Party result = authorityService.getPartyById("2021", 10, "A1", 1);

        assertNotNull(result);
        assertEquals("Party A", result.getName());
    }

    @Test
    void testGetCandidatesByPartyId() {
        List<Candidate> candidates = List.of(new Candidate());
        mockParty.setCandidates(candidates);

        when(electionService.getElection("2021")).thenReturn(mockElection);

        List<Candidate> result = authorityService.getCandidatesByPartyId("2021", 10, "A1", 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getFirstName());
        assertEquals("Doe", result.getFirst().getLastName());

    }
}
