package com.voteU.election.java.services;

import com.voteU.election.java.model.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Service
public class ElectionDataCSV {



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

    @Transactional
    public void exportAuthorityCandidateVotesToCSV(Election election, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write CSV header
            writer.write("authority_id,election_id,party_id,candidate_id,votes");
            writer.newLine();

            for (Province province : election.getProvinces()) {
                for (Constituency constituency : province.getConstituencies()) {
                    for (Authority authority : constituency.getAuthorities().values()) {
                        for (Party party : authority.getParties().values()) {
                            for (Candidate candidate : party.getCandidates()) {
                                String line = String.format("%s,%s,%s,%s,%d",
                                        authority.getId(),
                                        election.getId(),
                                        party.getId(),
                                        candidate.getId(),
                                        candidate.getVotes()
                                );
                                writer.write(line);
                                writer.newLine();
                            }
                        }
                    }
                }
            }
        }
        log.info("Exported authority candidate votes for election {} to CSV file {}", election.getId(), filePath);
    }

}
