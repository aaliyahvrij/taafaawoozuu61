package com.voteU.election.java.controller.database;

import com.voteU.election.java.dto.PollingStationPartyVoteDTO;
import com.voteU.election.java.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@Service
public class PollingStationPartyDataInserter {

    private static final Logger logger = LoggerFactory.getLogger(PollingStationPartyDataInserter.class);
    private final JdbcTemplate jdbc;

    public PollingStationPartyDataInserter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insertPollingStationPartyVotesStreamed(Election election, int chunkSize) {
        logger.info("Streaming and inserting party votes for election {}", election.getId());

        List<Object[]> buffer = new ArrayList<>(chunkSize);

        flattenData(election, vote -> {
            buffer.add(new Object[]{
                    vote.getElectionId(),
                    vote.getPollingStationId(),
                    vote.getPartyId(),
                    vote.getVotes()
            });

            if (buffer.size() >= chunkSize) {
                flushBatch(buffer);
            }
        });

        // Final flush
        if (!buffer.isEmpty()) {
            flushBatch(buffer);
        }

        logger.info("Finished inserting all party votes.");
    }

    private void flushBatch(List<Object[]> batch) {
        String sql = "INSERT INTO pollingstation_party_votes " +
                "(election_id, pollingstation_id, party_id, votes) " +
                "VALUES (?, ?, ?, ?)";

        jdbc.batchUpdate(sql, batch);
        logger.info("Inserted batch of size: {}", batch.size());
        batch.clear();
    }

    private void flattenData(Election election, Consumer<PollingStationPartyVoteDTO> consumer) {
        String electionId = election.getId();

        for (Constituency constituency : election.getConstituencies().values()) {
            for (Authority authority : constituency.getAuthorities().values()) {
                for (PollingStation station : authority.getPollingStations().values()) {
                    String pollingStationId = station.getId();

                    for (Party party : station.getParties().values()) {
                        int partyId = party.getId();
                        consumer.accept(new PollingStationPartyVoteDTO(
                                electionId,
                                pollingStationId,
                                partyId,
                                party.getVotes()
                        ));


                    }
                }
            }
        }
    }
}

