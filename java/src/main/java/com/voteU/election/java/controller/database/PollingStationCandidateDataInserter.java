package com.voteU.election.java.controller.database;

import com.voteU.election.java.dto.PollingStationVoteFlat;
import com.voteU.election.java.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class PollingStationCandidateDataInserter {

    private static final Logger logger = LoggerFactory.getLogger(PollingStationCandidateDataInserter.class);
    private final JdbcTemplate jdbc;

    public PollingStationCandidateDataInserter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insertPollingStationCandidateVotesStreamed(Election election, int chunkSize) {
        logger.info("Streaming and inserting candidate votes for election {}", election.getId());

        List<Object[]> buffer = new ArrayList<>(chunkSize);

        flattenData(election, vote -> {
            buffer.add(new Object[]{
                    vote.getElectionId(),
                    vote.getPollingStationId(),
                    vote.getPartyId(),
                    vote.getCandidateId(),
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

        logger.info("Finished inserting all candidate votes.");
    }

    private void flushBatch(List<Object[]> batch) {
        String sql = "INSERT INTO pollingstation_candidate_votes " +
                "(election_id, pollingstation_id, party_id, candidate_id, votes) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbc.batchUpdate(sql, batch);
        logger.info("Inserted batch of size: {}", batch.size());
        batch.clear();
    }

    private void flattenData(Election election, Consumer<PollingStationVoteFlat> consumer) {
        String electionId = election.getId();

        for (Constituency constituency : election.getConstituencies().values()) {
            for (Authority authority : constituency.getAuthorities().values()) {
                for (PollingStation station : authority.getPollingStations().values()) {
                    String pollingStationId = station.getId();

                    for (Party party : station.getParties().values()) {
                        int partyId = party.getId();

                        for (Candidate candidate : party.getCandidates()) {
                            if (candidate.getId() > -1) {
                                consumer.accept(new PollingStationVoteFlat(
                                        electionId,
                                        pollingStationId,
                                        partyId,
                                        candidate.getId(),
                                        candidate.getVotes()
                                ));
                            }
                        }
                    }
                }
            }
        }
    }
}
