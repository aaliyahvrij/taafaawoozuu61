package com.voteU.election.java.services;

import com.voteU.election.java.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
       // batchInsertNationalPartyVotes(election.getId(), election.getParties().values());
       // batchInsertNationalCandidateVotes(election.getId(), election.getParties().values());

        // Batch insert parties and candidates
       // batchInsertParties(election.getId(), election.getParties().values());
        //batchInsertCandidates(election.getId(), election.getParties().values());

        // Batch insert constituencies and their votes
       // batchInsertConstituencies(election.getConstituencies().values());
       // batchInsertConstituencyVotes(election.getId(), election.getConstituencies().values());
        //batchInsertConstituencyPartyVotes(election.getId(), election.getConstituencies().values());
       // batchInsertConstituencyCandidateVotes(election.getId(), election.getConstituencies().values());

        // Batch insert authorities and their votes, party votes, polling stations etc.
      //  batchInsertAuthorities(election.getId(), election.getConstituencies().values());

       // batchInsertAuthorityCandidateVotes(election.getId(), election.getConstituencies().values());

        batchInsertPollingStationCandidateVotes(election.getId(), election.getConstituencies().values());




        logger.info("Finished insertion of election with id {}", election.getId());
    }

    private void insertElectionRow(Election election) {
        String sql = "INSERT IGNORE INTO elections (id, name, date, votes) VALUES (?, ?, ?, ?)";
        int rows = jdbc.update(sql, election.getId(), election.getName(), election.getDate(), election.getVotes());
        logger.info("Inserted election row for id {}. Rows affected: {}", election.getId(), rows);
    }

    private void batchInsertParties(String electionId, Iterable<Party> parties) {
        String sql = "INSERT IGNORE INTO parties (id, election_id, name) VALUES (?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Party p : parties) {
            batchArgs.add(new Object[]{p.getId(), electionId, p.getName()});
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted parties for election {}. Number of parties: {}, Batch update count: {}", electionId, batchArgs.size(), result.length);
    }

    private void batchInsertCandidates(String electionId, Iterable<Party> parties) {
        String sql = "INSERT IGNORE INTO candidates (id, party_id, election_id, first_name, last_name, gender, locality_name) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Party p : parties) {
            for (Candidate c : p.getCandidates()) {
                batchArgs.add(new Object[]{
                        c.getId(),
                        p.getId(),
                        electionId,
                        c.getFirstName(),
                        c.getLastName(),
                        c.getGender(),
                        c.getLocalityName()
                });
            }
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted candidates for election {}. Number of candidates: {}, Batch update count: {}", electionId, batchArgs.size(), result.length);
    }

    private void batchInsertConstituencies(Iterable<Constituency> constituencies) {
        String sql = "INSERT IGNORE INTO constituencies (id, province_id, name) VALUES (?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Constituency c : constituencies) {
            batchArgs.add(new Object[]{c.getId(), c.getProvinceId(), c.getName()});
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted constituencies. Number of constituencies: {}, Batch update count: {}", batchArgs.size(), result.length);
    }

    private void batchInsertConstituencyVotes(String electionId, Iterable<Constituency> constituencies) {
        String sql = "INSERT IGNORE INTO constituency_total_votes (election_id, constituency_id, votes) VALUES (?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Constituency c : constituencies) {
            batchArgs.add(new Object[]{electionId, c.getId(), c.getVotes()});
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted constituency total votes for election {}. Number of constituencies: {}, Batch update count: {}", electionId, batchArgs.size(), result.length);
    }

    private void batchInsertConstituencyPartyVotes(String electionId, Iterable<Constituency> constituencies) {
        String sql = "INSERT IGNORE INTO constituency_party_votes (election_id, constituency_id, party_id, votes) VALUES (?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        int count = 0;
        for (Constituency c : constituencies) {
            for (Party p : c.getParties().values()) {
                batchArgs.add(new Object[]{electionId, c.getId(), p.getId(), p.getVotes()});
                count++;
            }
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted constituency party votes for election {}. Number of votes: {}, Batch update count: {}", electionId, count, result.length);
    }

    private void batchInsertAuthorities(String electionId, Iterable<Constituency> constituencies) {
        String authoritySql = "INSERT IGNORE INTO authorities (id, constituency_id, name, election_id) VALUES (?, ?, ?, ?)";
        String authorityVotesSql = "INSERT IGNORE INTO authority_total_votes (election_id, authority_id, votes) VALUES (?, ?, ?)";
        String authorityPartyVotesSql = "INSERT IGNORE INTO authority_party_votes (election_id, authority_id, party_id, votes) VALUES (?, ?, ?, ?)";
        String pollingStationSql = "INSERT IGNORE INTO pollingstations (id, authority_id, name, zipcode, election_id) VALUES (?, ?, ?, ?, ?)";
        String pollingStationVotesSql = "INSERT IGNORE INTO pollingstation_total_votes (election_id, pollingstation_id, votes) VALUES (?, ?, ?)";
        String pollingStationPartyVotesSql = "INSERT IGNORE INTO pollingstation_party_votes (election_id, pollingstation_id, party_id, votes) VALUES (?, ?, ?, ?)";

         List<Object[]> authorityBatch = new ArrayList<>();
        List<Object[]> authorityVotesBatch = new ArrayList<>();
        List<Object[]> authorityPartyVotesBatch = new ArrayList<>();
        List<Object[]> pollingStationBatch = new ArrayList<>();
        List<Object[]> pollingStationVotesBatch = new ArrayList<>();
        List<Object[]> pollingStationPartyVotesBatch = new ArrayList<>();

        int authorityPartyVotesCount = 0;
        int pollingStationPartyVotesCount = 0;

        for (Constituency c : constituencies) {
            for (Authority authority : c.getAuthorities().values()) {
                authorityBatch.add(new Object[]{authority.getId(), c.getId(), authority.getName(), electionId});
                authorityVotesBatch.add(new Object[]{electionId, authority.getId(), authority.getVotes()});

                for (Party p : authority.getParties().values()) {
                    authorityPartyVotesBatch.add(new Object[]{electionId, authority.getId(), p.getId(), p.getVotes()});
                    authorityPartyVotesCount++;
                }

                for (PollingStation ps : authority.getPollingStations().values()) {
                    pollingStationBatch.add(new Object[]{ps.getId(), authority.getId(), ps.getName(), ps.getZipCode(), electionId});
                    pollingStationVotesBatch.add(new Object[]{electionId, ps.getId(), ps.getVotes()});

                    for (Party p : ps.getParties().values()) {
                        pollingStationPartyVotesBatch.add(new Object[]{electionId, ps.getId(), p.getId(), p.getVotes()});
                        pollingStationPartyVotesCount++;
                    }
                }
            }
        }

       // int[] authorityResult = jdbc.batchUpdate(authoritySql, authorityBatch);
       // int[] authorityVotesResult = jdbc.batchUpdate(authorityVotesSql, authorityVotesBatch);
       // int[] authorityPartyVotesResult = jdbc.batchUpdate(authorityPartyVotesSql, authorityPartyVotesBatch);

       // int[] pollingStationResult = jdbc.batchUpdate(pollingStationSql, pollingStationBatch);
        //int[] pollingStationVotesResult = jdbc.batchUpdate(pollingStationVotesSql, pollingStationVotesBatch);
        batchUpdateInChunks(pollingStationPartyVotesSql, pollingStationPartyVotesBatch, 1000);

        //logger.info("Batch inserted authorities. Count: {}, Batch update count: {}", authorityBatch.size(), authorityResult.length);
       // logger.info("Batch inserted authority total votes. Count: {}, Batch update count: {}", authorityVotesBatch.size(), authorityVotesResult.length);
        //logger.info("Batch inserted authority party votes. Count: {}, Batch update count: {}", authorityPartyVotesCount, authorityPartyVotesResult.length);
       // logger.info("Batch inserted polling stations. Count: {}, Batch update count: {}", pollingStationBatch.size(), pollingStationResult.length);
       // logger.info("Batch inserted polling station total votes. Count: {}, Batch update count: {}", pollingStationVotesBatch.size(), pollingStationVotesResult.length);

    }
    // Inside ElectionDataInserter

    // Constituency candidate votes
    private void batchInsertConstituencyCandidateVotes(String electionId, Iterable<Constituency> constituencies) {
        String sql = "INSERT IGNORE INTO constituency_candidate_votes (election_id, constituency_id, party_id, candidate_id, votes) VALUES (?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        int count = 0;
        for (Constituency c : constituencies) {
            for (Party p : c.getParties().values()) {
                List<Candidate> candidates = p.getCandidates();
                for (int i = 0; i < candidates.size(); i++) {
                    Candidate candidate = candidates.get(i);
                    int votes = candidate.getVotes();  // votes in constituency context
                    batchArgs.add(new Object[]{electionId, c.getId(), p.getId(), candidate.getId(), votes});
                    count++;
                }
            }
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted constituency candidate votes for election {}. Count: {}, Batch update count: {}", electionId, count, result.length);
    }

    // Authority candidate votes
    private void batchInsertAuthorityCandidateVotes(String electionId, Iterable<Constituency> constituencies) {
        String sql = "INSERT IGNORE INTO authority_candidate_votes (election_id, authority_id, party_id, candidate_id, votes) VALUES (?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        int count = 0;
        for (Constituency c : constituencies) {
            for (Authority a : c.getAuthorities().values()) {
                for (Party p : a.getParties().values()) {
                    List<Candidate> candidates = p.getCandidates();
                    for (Candidate candidate : candidates) {
                        int votes = candidate.getVotes(); // votes in authority context
                        batchArgs.add(new Object[]{electionId, a.getId(), p.getId(), candidate.getId(), votes});
                        count++;
                    }
                }
            }
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted authority candidate votes for election {}. Count: {}, Batch update count: {}", electionId, count, result.length);
    }

    private void batchInsertPollingStationCandidateVotes(String electionId, Iterable<Constituency> constituencies) {
        String sql = "INSERT IGNORE INTO pollingstation_candidate_votes (election_id, pollingstation_id, party_id, candidate_id, votes) VALUES (?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        int count = 0;
        for (Constituency c : constituencies) {
            for (Authority a : c.getAuthorities().values()) {
                for (PollingStation ps : a.getPollingStations().values()) {
                    for (Party p : ps.getParties().values()) {
                        List<Candidate> candidates = p.getCandidates();
                        for (Candidate candidate : candidates) {
                            int votes = candidate.getVotes(); // votes in polling station context
                            batchArgs.add(new Object[]{electionId, ps.getId(), p.getId(), candidate.getId(), votes});
                            count++;
                        }
                    }
                }
            }
        }
        batchUpdateInChunks(sql, batchArgs, 1000);
        logger.info("Batch inserted polling station candidate votes for election {}. Count: {}", electionId, count);
    }


    // National party votes
    private void batchInsertNationalPartyVotes(String electionId, Collection<Party> parties) {
        String sql = "INSERT IGNORE INTO national_party_votes (election_id, party_id, votes) VALUES (?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Party p : parties) {
            batchArgs.add(new Object[]{electionId, p.getId(), p.getVotes()});
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted national party votes for election {}. Count: {}, Batch update count: {}", electionId, parties.size(), result.length);
    }


    // National candidate votes
    private void batchInsertNationalCandidateVotes(String electionId, Iterable<Party> parties) {
        String sql = "INSERT IGNORE INTO national_candidate_votes (election_id, party_id, candidate_id, votes) VALUES (?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        int count = 0;
        for (Party p : parties) {
            List<Candidate> candidates = p.getCandidates();
            for (Candidate candidate : candidates) {
                int votes = candidate.getVotes(); // votes in national context for candidate
                batchArgs.add(new Object[]{electionId, p.getId(), candidate.getId(), votes});
                count++;
            }
        }
        int[] result = jdbc.batchUpdate(sql, batchArgs);
        logger.info("Batch inserted national candidate votes for election {}. Count: {}, Batch update count: {}", electionId, count, result.length);
    }

}
