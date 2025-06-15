package com.voteU.election.java.services;

import com.voteU.election.java.CompactDTO.CompactConstituency;
import com.voteU.election.java.CompactDTO.CompactProvince;
import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.repositories.electiondata.ProvinceRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for handling logic related to provinces during elections.
 * This includes fetching full and compact province data, constituencies,
 * and calculating total votes per party within a province.
 */
@Service
public class ProvinceService {

    private final ElectionService electionService;
    private final ProvinceRepository provinceRepository;

    /**
     * Constructor for ProvinceService.
     *
     * @param electionService Service to handle election-related logic.
     * @param provinceRepository Repository to access province data.
     */
    public ProvinceService(ElectionService electionService, ProvinceRepository provinceRepository) {
        this.electionService = electionService;
        this.provinceRepository = provinceRepository;
    }

    /**
     * Gets all provinces for a given election year.
     *
     * @param year The election year (e.g. "2023").
     * @return A list of {@link Province} objects.
     */
    public List<Province> getProvinces(String year) {
        Election election = electionService.getElection(year);
        if (election == null) {
            throw new ResourceNotFoundException("Election not found for year: " + year);
        }
        return election.getProvinces();
    }

    /**
     * Gets a list of provinces with only ID and name.
     *
     * @param year The election year.
     * @return A list of {@link CompactProvince} objects.
     */
    public List<CompactProvince> getCompactProvinces(String year) {
        Election election = electionService.getElection(year);
        if (election == null) {
            throw new ResourceNotFoundException("Election not found for year: " + year);
        }
        List<Province> provinceMap = election.getProvinces();
        List<CompactProvince> compactProvinces = new ArrayList<>();
        for (Province province : provinceMap) {
            compactProvinces.add(new CompactProvince(province.getId(), province.getName()));
        }
        return compactProvinces;
    }

    /**
     * Gets all constituencies in a given province and year.
     *
     * @param year The election year.
     * @param provinceId The ID of the province.
     * @return A list of {@link Constituency} objects.
     */
    public List<Constituency> getConstituenciesByProvinceId(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Province with ID " + provinceId + " not found"));

        return province.getConstituencies();
    }

    /**
     * Gets a compact list of constituencies (ID and name only) for a province and year.
     *
     * @param year The election year.
     * @param provinceId The ID of the province.
     * @return A list of {@link CompactConstituency} objects.
     */
    public List<CompactConstituency> getCompactConstituenciesByProvinceId(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Province with ID " + provinceId + " not found"));

        List<CompactConstituency> compactConstituencies = new ArrayList<>();
        for (Constituency constituency : province.getConstituencies()) {
            compactConstituencies.add(new CompactConstituency(constituency.getId(), constituency.getName()));
        }
        return compactConstituencies;
    }


    /**
     * Calculates the total votes and percentage per party in a given province.
     *
     * @param province The {@link Province} object.
     * @return A map where the key is party ID and the value is the {@link Party} object with total votes and percentage.
     */
    public Map<Integer, Party> getTotalVotesPerParty(Province province) {
        Map<Integer, Party> totalVotesPerParty = new HashMap<>();
        int totalVotes = 0;

        for (var constituency : province.getConstituencies()) {
            for (var authority : constituency.getAuthorities().values()) {
                for (var party : authority.getParties().values()) {
                    totalVotesPerParty.compute(party.getId(), (id, existingParty) -> {
                        if (existingParty == null) {
                            Party newParty = new Party(party.getId(), party.getName());
                            newParty.setVotes(party.getVotes());
                            return newParty;
                        } else {
                            existingParty.setVotes(existingParty.getVotes() + party.getVotes());
                            return existingParty;
                        }
                    });
                    totalVotes += party.getVotes();
                }
            }
        }

        for (Party party : totalVotesPerParty.values()) {
            double percentage = (totalVotes == 0) ? 0 : (party.getVotes() * 100.0 / totalVotes);
            party.setPercentage(percentage);
        }

        return totalVotesPerParty;
    }



    public Map<Integer, Party> getTotalVotesPerParty(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return Map.of();

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Province with ID " + provinceId + " not found"));

        return getTotalVotesPerParty(province);
    }

    public List<DropdownOptionDTO<Integer>> getAllProvinceNames(String year) {
        return provinceRepository.getProvincesByElectionId(year);
    }


}
