package com.voteU.election.java.services;

import com.voteU.election.java.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ElectionDataPersister {

    private final JdbcTemplate jdbc;

    public ElectionDataPersister(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

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
            for (Candidate candidate : p.getCandidates()) {
                persistCandidate(p.getId(), candidate, election.getId());
            }
        }
    }

    private void persistElectionRow(Election election) {
        String sql = "INSERT INTO elections (id, name, date, votes) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, election.getId(), election.getName(), election.getDate(), election.getVotes());
    }

    private void persistConstituency(String electionId, Constituency c) {
        String sql = "INSERT INTO constituencies (id, election_id, name, votes) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, c.getId(), electionId, c.getName(), c.getVotes());
    }

    private void persistAuthority(int constituencyId, Authority authority) {
        String sql = "INSERT INTO authorities (id, constituency_id, name, votes) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, authority.getId(), constituencyId, authority.getName(), authority.getVotes());
    }

    private void persistPollingStation(String authorityId, PollingStation pollingStation) {
        String sql = "INSERT INTO pollingstations (id, authority_id, name, votes) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, pollingStation.getId(), authorityId, pollingStation.getName(), pollingStation.getVotes());
    }

    private void persistParty(String electionId, Party p) {
        String sql = "INSERT INTO parties (id, election_id, name) VALUES (?, ?, ?)";
        jdbc.update(sql, p.getId(), electionId, p.getName());
    }

    private void persistCandidate(int partyId, Candidate c, String electionId) {
        String sql = "INSERT INTO candidates (id, party_id, election_id, first_name, last_name, gender, locality_name) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                c.getId(),
                partyId,
                electionId,
                c.getFirstName(),
                c.getLastName(),
                c.getGender(),
                c.getLocalityName()
        );
    }


}
