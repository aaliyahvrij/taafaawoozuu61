package com.voteU.election.java.services;

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

    private boolean isValidDutchPostcode(String zipcode) {
        return zipcode != null && zipcode.matches("^\\d{4}[A-Za-z]{2}$");
    }

    public List<PollingStations> getPollingStationsByZipCode(String zipcode) {
        if(!isValidDutchPostcode(zipcode)){
            throw new IllegalArgumentException("Invalid Dutch postcode format. Expected 4 digits followed by 2 letters.");
        }
        return pollingStationRepository.findByZipcode(zipcode.toUpperCase());
    }

    public Page<PollingStations> getPollingStations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return pollingStationRepository.findAll(pageable);
    }
}
