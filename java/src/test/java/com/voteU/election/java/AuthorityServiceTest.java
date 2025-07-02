package com.voteU.election.java;

import com.voteU.election.java.model.Authority;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.reader.DutchElectionReader;
import com.voteU.election.java.services.electiondata.memory.AuthorityService;
import com.voteU.election.java.services.electiondata.memory.ConstituencyService;
import com.voteU.election.java.services.electiondata.memory.ElectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorityServiceTest {

    private AuthorityService authorityService;

    @BeforeEach
    void setUp() {
        DutchElectionReader reader = new DutchElectionReader();
        ElectionService electionService = new ElectionService(reader);
        electionService.readElection("TK2021");

        ConstituencyService constituencyService = new ConstituencyService(electionService);
        authorityService = new AuthorityService(electionService);
    }

    @Test
    void testGetAuthoritiesByConstituencyId() {
        Map<String, Authority> authorities = authorityService.getAuthoritiesByConstituencyId("TK2021", 1);

        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
    }

    @Test
    void testGetAuthorityById() {
        Authority authority = authorityService.getAuthorityById("TK2021", 1, "1");

        assertNotNull(authority);
        assertEquals("1", authority.getId());
    }

    @Test
    void testGetPartiesByAuthorityId() {
        Map<Integer, Party> parties = authorityService.getPartiesByAuthorityId("TK2021", 1, "1");

        assertNotNull(parties);
        assertFalse(parties.isEmpty());
    }

    @Test
    void testInvalidDataReturnsEmptyOrNull() {
        assertTrue(authorityService.getAuthoritiesByConstituencyId("XYZ", 1).isEmpty());
        assertNull(authorityService.getAuthorityById("XYZ", 1, "1"));
        assertTrue(authorityService.getPartiesByAuthorityId("XYZ", 1, "1").isEmpty());
    }
}
