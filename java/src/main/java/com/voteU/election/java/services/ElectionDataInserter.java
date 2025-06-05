package com.voteU.election.java.services;

import com.voteU.election.java.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class ElectionDataInserter {

    private static final Logger logger = LoggerFactory.getLogger(ElectionDataInserter.class);

    private final JdbcTemplate jdbc;

    private void batchUpdateInChunks(String sql, List<Object[]> batchArgs, int chunkSize) {
        int total = batchArgs.size();
        for (int i = 0; i < total; i += chunkSize) {
            int end = Math.min(total, i + chunkSize);
            List<Object[]> chunk = batchArgs.subList(i, end);
            int[] result = jdbc.batchUpdate(sql, chunk);
            logger.info("Batch update executed for chunk {} - {} of {}, affected rows: {}", i, end, total, result.length);
        }
    }

    public ElectionDataInserter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insertElection(Election election) {
        logger.info("Starting insertion of election with id {}", election.getId());

        // Insert single election row (assuming elections table id is unique)
        // insertElectionRow(election);
        // batchInsertParties(election.getId(), election.getParties().values());
        //batchInsertCandidates(election.getId(), election.getParties().values());

        //batchInsertNationalPartyVotes(election.getId(), election.getParties().values());
        // batchInsertNationalCandidateVotes(election.getId(), election.getParties().values());

       // batchInsertConstituencies(election);
        //batchInsertConstituencyPartyVotes(election);
        //batchInsertConstituencyCandidateVotes(election);
        //batchInsertAuthorities(election);
        batchInsertAuthorityPartyVotes(election);


        logger.info("Finished insertion of election with id {}", election.getId());
    }

    private void insertElectionRow(Election election) {
        String sql = "INSERT IGNORE INTO elections (election_id, name, date, votes) VALUES (?, ?, ?, ?)";
        int rows = jdbc.update(sql, election.getId(), election.getName(), election.getDate(), election.getVotes());
        logger.info("Inserted election row for election_id {}. Rows affected: {}", election.getId(), rows);
    }

    private void batchInsertParties(String electionId, Iterable<Party> parties) {
        String sql = "INSERT IGNORE INTO parties (party_id, election_id, name) VALUES (?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Party p : parties) {
            batchArgs.add(new Object[]{p.getId(), electionId, p.getName()});
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted parties for election {}. Number of parties: {}, Batch update count: {}", electionId, batchArgs.size(), result.length);
    }


    private void batchInsertCandidates(String electionId, Iterable<Party> parties) {
        String sql = "INSERT IGNORE INTO candidates (candidate_id, party_id, election_id, first_name, last_name, gender, locality_name) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Party p : parties) {
            for (Candidate c : p.getCandidates()) {
                batchArgs.add(new Object[]{c.getId(), // candidate_id
                        p.getId(), // party_id
                        electionId, c.getFirstName(), c.getLastName(), c.getGender(), c.getLocalityName()});
            }
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted candidates for election {}. Number of candidates: {}, Batch update count: {}", electionId, batchArgs.size(), result.length);
    }

    private void batchInsertNationalPartyVotes(String electionId, Collection<Party> parties) {
        String sql = "INSERT IGNORE INTO national_party_votes (election_id, party_id, votes) VALUES (?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Party p : parties) {
            batchArgs.add(new Object[]{electionId, p.getId(), p.getVotes()});
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted national party votes for election {}. Count: {}, Batch update count: {}", electionId, parties.size(), result.length);
    }

    private void batchInsertNationalCandidateVotes(String electionId, Collection<Party> parties) {
        String sql = "INSERT IGNORE INTO national_candidate_votes (candidate_id, party_id, election_id, votes) VALUES (?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Party p : parties) {
            for (Candidate c : p.getCandidates()) {
                batchArgs.add(new Object[]{c.getId(),    // candidate_ref_id (references candidates.id)
                        p.getId(),    // party_id
                        electionId,   // election_id
                        c.getVotes()  // votes
                });
            }
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted national candidate votes for election {}. Number of entries: {}, Batch update count: {}", electionId, batchArgs.size(), result.length);
    }

    private void batchInsertConstituencies(Election election) {
        String sql = "INSERT INTO constituencies " + "(constituency_id, province_id, election_id, name, votes) VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (Province province : election.getProvinces()) {
            for (Constituency constituency : province.getConstituencies()) {
                batchArgs.add(new Object[]{constituency.getId(), province.getId(), election.getId(), constituency.getName(), constituency.getVotes()  // assuming this is the field in your model
                });
            }
        }

        batchUpdateInChunks(sql, batchArgs, 1000);
        logger.info("Inserted into 'constituencies' for election {}: {} rows", election.getId(), batchArgs.size());
    }

    private void batchInsertConstituencyPartyVotes(Election election) {
        String sql = "INSERT INTO constituency_party_votes " + "(constituency_id, election_id, party_id, votes) VALUES (?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (Province province : election.getProvinces()) {
            for (Constituency constituency : province.getConstituencies()) {
                for (Party party : constituency.getParties().values()) {
                    batchArgs.add(new Object[]{constituency.getId(), election.getId(), party.getId(), party.getVotes()});
                }
            }
        }

        batchUpdateInChunks(sql, batchArgs, 1000);
        logger.info("Inserted constituency_party_votes for election {}: {} rows", election.getId(), batchArgs.size());
    }

    private void batchInsertConstituencyCandidateVotes(Election election) {
        String sql = "INSERT INTO constituency_candidate_votes " + "(candidate_id, party_id, election_id, constituency_id, votes) VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (Province province : election.getProvinces()) {
            for (Constituency constituency : province.getConstituencies()) {
                for (Party party : constituency.getParties().values()) {
                    for (Candidate candidate : party.getCandidates()) {
                        batchArgs.add(new Object[]{candidate.getId(), party.getId(), election.getId(), constituency.getId(), candidate.getVotes()});
                    }
                }
            }
        }

        batchUpdateInChunks(sql, batchArgs, 1000);
        logger.info("Inserted constituency_candidate_votes for election {}: {} rows", election.getId(), batchArgs.size());
    }

    private void batchInsertAuthorities(Election election) {
        String sql = "INSERT IGNORE INTO authorities (authority_id, constituency_id, election_id, name, votes) VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (Province province : election.getProvinces()) {
            for (Constituency constituency : province.getConstituencies()) {
                for (Authority authority : constituency.getAuthorities().values()) {
                    batchArgs.add(new Object[]{
                            authority.getId(),
                            constituency.getId(),
                            election.getId(),
                            authority.getName(),
                            authority.getVotes()
                    });
                }
            }
        }

        batchUpdateInChunks(sql, batchArgs, 1000);
        logger.info("Batch inserted authorities for election {}. Total authorities: {}", election.getId(), batchArgs.size());
    }

    private void batchInsertAuthorityPartyVotes(Election election) {
        String sql = "INSERT IGNORE INTO authority_party_votes (authority_id, party_id, election_id, votes) VALUES (?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (Province province : election.getProvinces()) {
            for (Constituency constituency : province.getConstituencies()) {
                for (Authority authority : constituency.getAuthorities().values()) {
                    // Assuming Authority has a Map<Integer, Party> parties with votes
                    for (Party party : authority.getParties().values()) {
                        batchArgs.add(new Object[]{
                                authority.getId(),
                                party.getId(),
                                election.getId(),
                                party.getVotes()
                        });
                    }
                }
            }
        }

        batchUpdateInChunks(sql, batchArgs, 1000);
        logger.info("Batch inserted authority party votes for election {}. Total records: {}", election.getId(), batchArgs.size());
    }



    public void exportPollingStationCandidateVotesToCSV(Election election, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("election_id,pollingstation_id,party_id,candidate_id,votes");
            writer.newLine();
            for (Province province : election.getProvinces()) {
                for (Constituency c : province.getConstituencies()) {
                    for (Authority a : c.getAuthorities().values()) {
                        for (PollingStation ps : a.getPollingStations().values()) {
                            for (Party p : ps.getParties().values()) {
                                for (Candidate candidate : p.getCandidates()) {
                                    int votes = candidate.getVotes();
                                    String line = String.format("%s,%s,%s,%s,%d", election.getId(), ps.getId(), p.getId(), candidate.getId(), votes);
                                    writer.write(line);
                                    writer.newLine();
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
