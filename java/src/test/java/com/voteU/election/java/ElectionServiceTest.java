package com.voteU.election.java;

import com.voteU.election.java.model.Election;
import com.voteU.election.java.reader.DutchElectionReader;
import com.voteU.election.java.services.ElectionService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ElectionServiceTest {
    @Test
    void testGetElection2021() {
        DutchElectionReader reader = new DutchElectionReader();
        ElectionService service = new ElectionService(reader);

        service.readElection("TK2021");
        Election e = service.getElection("TK2021");

        assertNotNull(e);
        assertEquals("TK2021", e.getId());
    }
@Test
    void testGetElection2023() {
        DutchElectionReader reader = new DutchElectionReader();
        ElectionService service = new ElectionService(reader);

        service.readElection("TK2023");
        Election e = service.getElection("TK2023");

        assertNotNull(e);
        assertEquals("TK2023", e.getId());
    }


}
