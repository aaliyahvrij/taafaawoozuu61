package com.voteU.election.java.services.electiondata.database;

import com.voteU.election.java.entities.electiondata.PollingStations;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.repositories.electiondata.PollingStationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollingStationsService {
    private final PollingStationRepository pollingStationRepository;

    public PollingStationsService(PollingStationRepository pollingStationRepository) {
        this.pollingStationRepository = pollingStationRepository;
    }

    public List<PollingStations> getPollingStationsByZipCode(String zipcode, String electionId) {
        String cleanedZipcode = zipcode.replaceAll("\\s+", "").toUpperCase();

        if (!cleanedZipcode.matches("^\\d{1,4}([A-Z]{0,2})?$")) {
            throw new IllegalArgumentException("Invalid Dutch postcode format. Must start with up to 4 digits followed by up to 2 letters.");
        }

        List<PollingStations> stations = pollingStationRepository.findByZipcodeStartingWithAndElectionId(cleanedZipcode, electionId);
        if (stations.isEmpty()) {
            throw new ResourceNotFoundException("No polling stations found for the given zipcode and election.");
        }

        return stations;
    }

    public Page<PollingStations> getPollingStations(String electionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<PollingStations> pageResult = pollingStationRepository.findByElectionId(electionId, pageable);

        if (pageResult.isEmpty()) {
            throw new ResourceNotFoundException("No polling stations found for the specified election and page.");
        }

        return pageResult;
    }

    public PollingStations getPollingStationById(Long id) {
        PollingStations pollingStation = pollingStationRepository.findById(id);
        if (pollingStation == null) {
            throw new ResourceNotFoundException("Polling station with ID " + id + " not found.");
        }
        return pollingStation;
    }
}
