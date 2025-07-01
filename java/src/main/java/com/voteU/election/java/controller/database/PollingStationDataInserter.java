package com.voteU.election.java.controller.database;

import com.voteU.election.java.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PollingStationDataInserter {

    private static final Logger logger = LoggerFactory.getLogger(PollingStationDataInserter.class);
    private final JdbcTemplate jdbc;

    public PollingStationDataInserter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insertPollingStations(Election election, int chunkSize) {
        logger.info("Streaming and inserting polling station metadata for election {}", election.getId());

        List<Object[]> buffer = new ArrayList<>(chunkSize);

        String electionId = election.getId();

        for (Constituency constituency : election.getConstituencies().values()) {
            for (Authority authority : constituency.getAuthorities().values()) {
                String authorityId = authority.getId();
                String authorityName = authority.getName(); // assuming Authority has getName()

                for (PollingStation station : authority.getPollingStations().values()) {
                    buffer.add(new Object[]{
                            station.getId(),
                            authorityId,
                            authorityName,
                            electionId,
                            station.getName(),
                            station.getVotes(),  // assuming getTotalVotes() exists
                            station.getZipCode()      // assuming getZipCode() exists
                    });

                    if (buffer.size() >= chunkSize) {
                        flushBatch(buffer);
                    }
                }
            }
        }

        if (!buffer.isEmpty()) {
            flushBatch(buffer);
        }

        logger.info("Finished inserting all polling stations.");
    }

    private void flushBatch(List<Object[]> batch) {
        String sql = "INSERT INTO pollingstations " +
                "(pollingstation_id, authority_id, authority_name, election_id, name, votes, zipcode) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbc.batchUpdate(sql, batch);
        logger.info("Inserted batch of polling stations: {}", batch.size());
        batch.clear();
    }
}
