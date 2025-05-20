package com.voteU.election.java.services;

import com.voteU.election.java.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ElectionDataPersister {

    private final JdbcTemplate jdbc;

    /**
     * Constructs a new ElectionDataPersister with the specified JdbcTemplate.
     *
     * @param jdbc the JdbcTemplate to be used for database operations
     */
    public ElectionDataPersister(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Persists the given {@code Election} object along with its related data, such as constituencies,
     * authorities, polling stations, parties, and candidates, into the database.
     *
     * @param election the {@code Election} object containing all relevant data to be persisted,
     *                 including its constituencies, authorities, polling stations, parties, and candidates
     */
    public void persistElection(Election election) {
        persistElectionRow(election);
        for (Constituency c : election.getConstituencies().values()) {
            persistConstituency(election.getId(), c);

            // Persist authorities in this constituency
            for (Authority authority : c.getAuthorities().values()) {
                persistAuthority(c.getId(), authority);

                // Persist polling stations in this authority
                for (PollingStation pollingStation : authority.getPollingStations().values()) {
                    persistPollingStation(authority.getId(), pollingStation);
                }
            }
        }
        for (Party p : election.getParties().values()) {
            persistParty(election.getId(), p);

            int candidateIndex = 1;
            for (Candidate candidate : p.getCandidates()) {
                int generatedId = p.getId() * 1000 + candidateIndex;
                persistCandidate(generatedId, p.getId(), candidate, election.getId());
                candidateIndex++;
            }
        }
    }

    /**
     * Persists a given election object into the database by inserting its details
     * into the elections table.
     *
     * @param election the Election object containing details such as ID, name,
     *                 date, and total votes to be persisted.
     */
    private void persistElectionRow(Election election) {
        String sql = "INSERT INTO elections (id, name, date, votes) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, election.getId(), election.getName(), election.getDate(), election.getVotes());
    }

    /**
     * Persists a constituency record into the database.
     *
     * @param electionId the unique identifier of the election to which the constituency belongs
     * @param c the Constituency object containing details such as id, name, and votes to be persisted
     */
    private void persistConstituency(String electionId, Constituency c) {
        String sql = "INSERT INTO constituencies (id, election_id, name, votes) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, c.getId(), electionId, c.getName(), c.getVotes());
    }

    /**
     * Persists an Authority entity into the database for a given constituency.
     *
     * @param constituencyId the identifier of the constituency to which the authority belongs
     * @param authority the Authority object containing details to be persisted
     */
    private void persistAuthority(int constituencyId, Authority authority) {
        String sql = "INSERT INTO authorities (id, constituency_id, name, votes) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, authority.getId(), constituencyId, authority.getName(), authority.getVotes());
    }

    /**
     * Persists a given polling station associated with a specific authority ID into the database.
     *
     * @param authorityId the unique identifier of the authority to which the polling station belongs
     * @param pollingStation the polling station object containing the details to be persisted
     */
    private void persistPollingStation(String authorityId, PollingStation pollingStation) {
        String sql = "INSERT INTO pollingstations (id, authority_id, name, votes, zipcode) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, pollingStation.getId(), authorityId, pollingStation.getName(), pollingStation.getVotes(), pollingStation.getZipCode());
    }

    /**
     * Persists a Party into the database by inserting its id, election id, and name.
     *
     * @param electionId the identifier of the election to which this party belongs
     * @param p the Party object to be persisted, containing data such as id and name
     */
    private void persistParty(String electionId, Party p) {
        String sql = "INSERT INTO parties (id, election_id, name) VALUES (?, ?, ?)";
        jdbc.update(sql, p.getId(), electionId, p.getName());
    }

    /**
     * Persists a candidate's details into the database.
     *
     * @param partyId    the ID of the party to which the candidate belongs
     * @param c          the candidate object containing the details to be persisted
     * @param electionId the ID of the election in which the candidate is participating
     */
    private void persistCandidate(int candidateId, int partyId, Candidate c, String electionId) {
        String sql = "INSERT INTO candidates (id, party_id, election_id, first_name, last_name, gender, locality_name) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                candidateId,
                partyId,
                electionId,
                c.getFirstName(),
                c.getLastName(),
                c.getGender(),
                c.getLocalityName()
        );
    }


}
