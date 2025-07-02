package com.voteU.election.java.services.electiondata.database;

import com.voteU.election.java.entities.electiondata.PollingStations;
import com.voteU.election.java.repositories.electiondata.PollingStationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollingStationsService {
    private PollingStationRepository pollingStationRepository;

    public PollingStationsService(PollingStationRepository pollingStationRepository) {
        this.pollingStationRepository = pollingStationRepository;
    }

    public List<PollingStations> getPollingStationsByZipCode(String zipcode, String electionId) {
        String cleanedZipcode = zipcode.replaceAll("\\s+", "").toUpperCase();

        if (cleanedZipcode.matches("^\\d{1,4}([A-Z]{0,2})?$")) {
            return pollingStationRepository.findByZipcodeStartingWithAndElectionId(cleanedZipcode, electionId);
        } else {
            throw new IllegalArgumentException("Invalid Dutch postcode format. Must start with up to 4 digits followed by up to 2 letters.");
        }
    }

    public Page<PollingStations> getPollingStations(String electionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return pollingStationRepository.findByElectionId(electionId, pageable);
    }
}
