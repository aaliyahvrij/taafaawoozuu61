package com.voteU.election.java.services;

import com.voteU.election.java.CompactDTO.CompactConstituency;
import com.voteU.election.java.CompactDTO.CompactProvince;
import com.voteU.election.java.dto.DropdownOptionDTO;
import com.voteU.election.java.model.Constituency;
import com.voteU.election.java.model.Election;
import com.voteU.election.java.model.Party;
import com.voteU.election.java.model.Province;
import com.voteU.election.java.repositories.electiondata.ProvinceRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class responsible for handling province-related logic,
 * including fetching province and constituency data,
 * as well as aggregating vote totals.
 */
@Service
public class ProvinceService {

    private final ElectionService electionService;
    private final ProvinceRepository provinceRepository;

    /**
     * Constructs a new ProvinceService with the required dependencies.
     *
     * @param electionService     the service used to fetch election data
     * @param provinceRepository  the repository used to fetch province metadata
     */
    public ProvinceService(ElectionService electionService, ProvinceRepository provinceRepository) {
        this.electionService = electionService;
        this.provinceRepository = provinceRepository;
    }

    /**
     * Retrieves all provinces for a given election year.
     *
     * @param year the election year
     * @return list of {@link Province} objects or empty list if not found
     */
    public List<Province> getProvinces(String year) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();
        return election.getProvinces();
    }

    /**
     * Retrieves a compact list of provinces (ID and name) for a given election year.
     *
     * @param year the election year
     * @return list of {@link CompactProvince} DTOs
     */
    public List<CompactProvince> getCompactProvinces(String year) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();
        List<Province> provinceMap = election.getProvinces();
        List<CompactProvince> compactProvinces = new ArrayList<>();
        for (Province province : provinceMap) {
            compactProvinces.add(new CompactProvince(province.getId(), province.getName()));
        }
        return compactProvinces;
    }

    /**
     * Retrieves all constituencies belonging to a province by ID and election year.
     *
     * @param year       the election year
     * @param provinceId the ID of the province
     * @return list of {@link Constituency} objects
     */
    public List<Constituency> getConstituenciesByProvinceId(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElse(null);

        if (province == null) return new ArrayList<>();

        return province.getConstituencies();
    }

    /**
     * Retrieves compact list of constituencies (ID and name only) for a given province and year.
     *
     * @param year       the election year
     * @param provinceId the ID of the province
     * @return list of {@link CompactConstituency} DTOs
     */
    public List<CompactConstituency> getCompactConstituenciesByProvinceId(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return new ArrayList<>();

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElse(null);

        if (province == null) return new ArrayList<>();

        List<CompactConstituency> compactConstituencies = new ArrayList<>();
        for (Constituency constituency : province.getConstituencies()) {
            compactConstituencies.add(new CompactConstituency(constituency.getId(), constituency.getName()));
        }

        return compactConstituencies;
    }


    /**
     * Aggregates the total number of votes per party in a given province.
     *
     * @param province the {@link Province} object
     * @return map of party ID to {@link Party} with vote totals and percentages
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

    /**
     * Aggregates total votes per party for a specific province and year.
     *
     * @param year       the election year
     * @param provinceId the ID of the province
     * @return map of party ID to {@link Party} with vote totals and percentages
     */
    public Map<Integer, Party> getTotalVotesPerParty(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return Map.of();

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElse(null);

        if (province == null) return Map.of();

        return getTotalVotesPerParty(province);
    }

    /**
     * Retrieves all province names as dropdown options for a given election year.
     *
     * @param year the election year
     * @return list of {@link DropdownOptionDTO} containing province IDs and names
     */
    public List<DropdownOptionDTO<Integer>> getAllProvinceNames(String year) {
        return provinceRepository.getProvincesByElectionId(year);
    }

    /**
     * Calculates the total number of votes cast in a specific province for a given election year.
     *
     * @param year       the election year
     * @param provinceId the ID of the province
     * @return total number of votes
     */
    public int getTotalVotesForProvince(String year, int provinceId) {
        Election election = electionService.getElection(year);
        if (election == null) return 0;

        Province province = election.getProvinces().stream()
                .filter(p -> p.getId() == provinceId)
                .findFirst()
                .orElse(null);

        if (province == null) return 0;

        int totalVotes = 0;

        for (var constituency : province.getConstituencies()) {
            for (var authority : constituency.getAuthorities().values()) {
                for (var party : authority.getParties().values()) {
                    totalVotes += party.getVotes();
                }
            }
        }

        return totalVotes;
    }


}
