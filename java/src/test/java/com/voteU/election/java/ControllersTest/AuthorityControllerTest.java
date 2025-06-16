package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.controller.AuthorityController;
import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.Candidate;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.services.AuthorityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorityControllerTest {

    @Mock
    private AuthorityService authorityService;

    @InjectMocks
    private AuthorityController authorityController;

    private final String year = "2023";
    private final Integer constituencyId = 1;
    private final String authorityId = "auth123";
    private final Integer partyId = 10;

    private Map<String, Authority> authorities;
    private Map<Integer, Party> parties;
    private Authority sampleAuthority;
    private Party sampleParty;
    private List<Candidate> candidates;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleAuthority = new Authority("someId");
        sampleAuthority.setId(authorityId);
        sampleAuthority.setName("Authority Name");

        authorities = new HashMap<>();
        authorities.put(authorityId, sampleAuthority);

        sampleParty = new Party(6,"Party fb");
        sampleParty.setId(partyId);
        sampleParty.setName("Party Name");

        parties = new HashMap<>();
        parties.put(partyId, sampleParty);

        Candidate candidate = new Candidate();
        candidate.setId(100);
        candidate.setFirstName("Candidate");
        candidate.setLastName("One");

        candidates = new ArrayList<>();
        candidates.add(candidate);
    }

    @Test
    void testGetAuthoritiesByConstituencyId() {
        when(authorityService.getAuthoritiesByConstituencyId(year, constituencyId)).thenReturn(authorities);

        Map<String, Authority> result = authorityController.getAuthoritiesByConstituencyId(year, constituencyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(authorityId));
        assertEquals("Authority Name", result.get(authorityId).getName());
        verify(authorityService, times(1)).getAuthoritiesByConstituencyId(year, constituencyId);
    }

    @Test
    void testGetAuthoritiesByConstituencyIdThrowsException() {
        when(authorityService.getAuthoritiesByConstituencyId(year, constituencyId)).thenThrow(new RuntimeException("DB error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authorityController.getAuthoritiesByConstituencyId(year, constituencyId));
        assertEquals(500, exception.getStatusCode().value());
        assertTrue(exception.getReason().contains("Failed to fetch authorities"));
    }

    @Test
    void testGetAuthoritiesByConstituencyIdCompact() {
        when(authorityService.getAuthoritiesByConstituencyIdCompact(year, constituencyId)).thenReturn(authorities);

        Map<String, Authority> result = authorityController.getAuthoritiesByConstituencyIdCompact(year, constituencyId);

        assertNotNull(result);
        verify(authorityService, times(1)).getAuthoritiesByConstituencyIdCompact(year, constituencyId);
    }

    @Test
    void testGetAuthorityByIdFound() {
        when(authorityService.getAuthorityById(year, constituencyId, authorityId)).thenReturn(sampleAuthority);

        Authority result = authorityController.getAuthorityById(year, constituencyId, authorityId);

        assertNotNull(result);
        assertEquals("Authority Name", result.getName());
        verify(authorityService, times(1)).getAuthorityById(year, constituencyId, authorityId);
    }

    @Test
    void testGetAuthorityByIdNotFound() {
        when(authorityService.getAuthorityById(year, constituencyId, authorityId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authorityController.getAuthorityById(year, constituencyId, authorityId));
        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Authority not found", exception.getReason());
    }

    @Test
    void testGetPartiesByAuthorityId() {
        when(authorityService.getPartiesByAuthorityId(year, constituencyId, authorityId)).thenReturn(parties);

        Map<Integer, Party> result = authorityController.getPartiesByAuthorityId(year, constituencyId, authorityId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(partyId));
        verify(authorityService, times(1)).getPartiesByAuthorityId(year, constituencyId, authorityId);
    }

    @Test
    void testGetPartiesByAuthorityIdThrowsException() {
        when(authorityService.getPartiesByAuthorityId(year, constituencyId, authorityId)).thenThrow(new RuntimeException("DB error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authorityController.getPartiesByAuthorityId(year, constituencyId, authorityId));
        assertEquals(500, exception.getStatusCode().value());
        assertTrue(exception.getReason().contains("Failed to fetch parties"));
    }

    @Test
    void testGetPartyByIdFound() {
        when(authorityService.getPartyById(year, constituencyId, authorityId, partyId)).thenReturn(sampleParty);

        Party result = authorityController.getPartyById(year, constituencyId, authorityId, partyId);

        assertNotNull(result);
        assertEquals("Party Name", result.getName());
        verify(authorityService, times(1)).getPartyById(year, constituencyId, authorityId, partyId);
    }

    @Test
    void testGetPartyByIdNotFound() {
        when(authorityService.getPartyById(year, constituencyId, authorityId, partyId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authorityController.getPartyById(year, constituencyId, authorityId, partyId));
        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Party not found", exception.getReason());
    }

    @Test
    void testGetCandidatesByPartyId() {
        when(authorityService.getCandidatesByPartyId(year, constituencyId, authorityId, partyId)).thenReturn(candidates);

        List<Candidate> result = authorityController.getCandidatesByPartyId(year, constituencyId, authorityId, partyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Candidate", result.getFirst().getFirstName());
        verify(authorityService, times(1)).getCandidatesByPartyId(year, constituencyId, authorityId, partyId);
    }

    @Test
    void testGetCandidatesByPartyIdThrowsException() {
        when(authorityService.getCandidatesByPartyId(year, constituencyId, authorityId, partyId)).thenThrow(new RuntimeException("DB error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authorityController.getCandidatesByPartyId(year, constituencyId, authorityId, partyId));
        assertEquals(500, exception.getStatusCode().value());
        assertTrue(exception.getReason().contains("Failed to fetch candidates"));
    }
}
